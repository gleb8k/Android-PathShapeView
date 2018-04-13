package shape.path.view.fill.provider

import android.content.Context
import android.graphics.*
import org.json.JSONObject
import shape.path.view.point.converter.PointConverter
import shape.path.view.utils.BitmapUtils
import shape.path.view.utils.JSONUtils
import shape.path.view.utils.MathUtils

/**
 * Created by root on 1/10/18.
 */
abstract class FillProvider {
    internal val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)

    private var gradient: GradientProvider? = null
    private var roundedCornerRadius: Float = 0f
    private var fillType: FillType = FillType.MIRROR
    private var imageResId: Int = -1
    private var bitmap: Bitmap? = null
    private var imageWidth: Float = 0f
    private var imageHeight: Float = 0f
    private var convertImageSize: Boolean = false
    private var hasShadowLayer: Boolean = false

    internal open var shaderChanged = false
    internal open var pathEffectChanged = false

    enum class FillType private constructor(internal var mode: Shader.TileMode) {
        REPEAT(Shader.TileMode.REPEAT),
        MIRROR(Shader.TileMode.MIRROR),
        CLAMP(Shader.TileMode.CLAMP);

        companion object {
            fun fromString(type: String?): FillType? {
                if (type.isNullOrEmpty()) return null
                FillType.values().forEach {
                    if (it.toString() == type) {
                        return it
                    }
                }
                return null
            }
        }
    }

    enum class GlowType private constructor(internal var type: BlurMaskFilter.Blur){
        NORMAL(BlurMaskFilter.Blur.NORMAL),
        SOLID(BlurMaskFilter.Blur.SOLID),
        OUTER(BlurMaskFilter.Blur.OUTER),
        INNER(BlurMaskFilter.Blur.INNER);

        companion object {
            fun fromString(type: String?): GlowType? {
                if (type.isNullOrEmpty()) return null
                GlowType.values().forEach {
                    if (it.toString() == type) {
                        return it
                    }
                }
                return null
            }
        }
    }

    enum class EmbossType private constructor(internal var z: Float, internal var ambient: Float,
                                              internal var specular: Float, internal var radius: Float) {
        NORMAL(1f, 0.3f, 10f, 10f),
        EXTRUDE(0.5f, 0.8f, 13f, 7f);

        companion object {
            fun fromString(type: String?): EmbossType? {
                if (type.isNullOrEmpty()) return null
                EmbossType.values().forEach {
                    if (it.toString() == type) {
                        return it
                    }
                }
                return null
            }
        }
    }

    fun setColor(color: Int) {
        paint.color = color
    }

    fun setGradient(gradient: GradientProvider) {
        this.gradient = gradient
        shaderChanged = true
    }

    fun setFillType(fillType: FillType) {
        this.fillType = fillType
        shaderChanged = true
    }

    fun setTexture(resId: Int) {
        clearBitmap()
        this.imageResId = resId
        shaderChanged = true
    }

    fun setTexture(bitmap: Bitmap) {
        clearBitmap()
        this.bitmap = bitmap
        shaderChanged = true
    }

    fun fitTextureToSize(width: Float, height: Float) {
        this.fitTextureToSize(width, height, false)
        shaderChanged = true
    }

    fun fitTextureToSize(width: Float, height: Float, convertWithPointConverter: Boolean) {
        this.imageWidth = width
        this.imageHeight = height
        this.convertImageSize = convertWithPointConverter
        shaderChanged = true
    }

    fun setRoundedCorners(radius: Float) {
        roundedCornerRadius = radius
        pathEffectChanged = true
    }

    fun setGlowEffect(radius: Float, glowType: GlowType) {
        clearShadow()
        paint.maskFilter = BlurMaskFilter(radius, glowType.type)
    }

    fun setEmbossEffect(angle: Float, embossType: EmbossType) {
        clearShadow()
        val p = MathUtils.getVectorEndPoint(angle, 1f)
        paint.maskFilter = EmbossMaskFilter(floatArrayOf(p.x, p.y, embossType.z), embossType.ambient,
                embossType.specular, embossType.radius)
    }

    fun setShadow(radius: Float, dx: Float, dy: Float, color: Int) {
        paint.maskFilter = null
        paint.setShadowLayer(radius, dx, dy, color)
        hasShadowLayer = true
    }

    fun clearAllEffects() {
        clearShadow()
        paint.maskFilter = null
    }

    private fun clearShadow() {
        if (hasShadowLayer) {
            paint.clearShadowLayer()
            hasShadowLayer = false
        }
    }

    internal fun hasEffects(): Boolean {
        return paint.maskFilter != null || hasShadowLayer
    }

    private fun clearBitmap() {
        if (bitmap != null) {
            bitmap!!.recycle()
            bitmap = null
        }
    }

    protected open fun buildEffect(): PathEffect? {
        if (roundedCornerRadius != 0f) {
            return CornerPathEffect(roundedCornerRadius)
        }
        return null
    }

    internal fun build(context: Context, converter: PointConverter) {
        if (pathEffectChanged) {
            paint.pathEffect = buildEffect()
            pathEffectChanged = false
        }
        if (shaderChanged) {
            paint.shader = buildShader(context, converter)
            shaderChanged = false
        }
    }

    private fun buildShader(context: Context, converter: PointConverter): Shader? {
        var imageSize = PointF(imageWidth, imageHeight)
        if (convertImageSize) {
            imageSize = converter.convertPoint(imageSize)
        }
        bitmap = BitmapUtils.loadBitmap(context, bitmap, imageResId, imageSize.x, imageSize.y)
        var bitmapShader: Shader? = null
        bitmap?.let {
            bitmapShader = BitmapShader(it, fillType.mode, fillType.mode)
        }
        val gradientShader = gradient?.build(converter, fillType.mode)
        if (bitmapShader == null) {
            return gradientShader
        }
        if (gradientShader == null) {
            return bitmapShader
        }
        return ComposeShader(bitmapShader, gradientShader, PorterDuff.Mode.MULTIPLY)
    }

    protected open fun fromJson(context: Context, json: JSONObject) {
        val c = json.optString("color", null)
        c?.let { paint.color = Color.parseColor(it) }
        gradient = GradientProvider.fromJson(json.optJSONObject("gradient"))
        val texture = json.optJSONObject("texture")
        texture?.let {
            val w = it.optDouble("width")
            val h = it.optDouble("height")
            setTexture(JSONUtils.stringToDrawableResId(context, it.optString("resource")))
            if (w != Double.NaN && h != Double.NaN) {
                fitTextureToSize(w.toFloat(), h.toFloat())
            }
        }
        fillType = FillType.fromString(json.optString("fillType")) ?: FillType.MIRROR
        roundedCornerRadius = json.optDouble("roundedCorners", 0.0).toFloat()
        val glow = json.optJSONObject("glow")
        glow?.let {
            val type = GlowType.fromString(it.optString("type"))
            val radius = it.optDouble("radius", 0.0).toFloat()
            if (type != null && radius > 0) {
                setGlowEffect(radius, type)
            }
        }
        val emboss = json.optJSONObject("emboss")
        emboss?.let {
            val type = EmbossType.fromString(it.optString("type"))
            val angle = it.optDouble("angle", 0.0).toFloat()
            type?.let { setEmbossEffect(angle, it) }
        }
        val shadow = json.optJSONObject("shadow")
        shadow?.let {
            val color = Color.parseColor(it.optString("color"))
            val radius = it.optDouble("radius", 0.0).toFloat()
            val offset = JSONUtils.jsonToPoint(it.optJSONObject("offset"), PointF(0f,0f))!!
            setShadow(radius, offset.x, offset.y, color)
        }
    }
}