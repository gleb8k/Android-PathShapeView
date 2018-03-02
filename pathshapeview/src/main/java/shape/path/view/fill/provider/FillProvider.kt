package shape.path.view.fill.provider

import android.content.Context
import android.graphics.*
import shape.path.view.point.converter.PointConverter
import shape.path.view.utils.BitmapUtils
import shape.path.view.utils.Utils

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

    }

    enum class GlowType private constructor(internal var type: BlurMaskFilter.Blur){
        NORMAL(BlurMaskFilter.Blur.NORMAL),
        SOLID(BlurMaskFilter.Blur.SOLID),
        OUTER(BlurMaskFilter.Blur.OUTER),
        INNER(BlurMaskFilter.Blur.INNER)
    }

    enum class EmbossType private constructor(internal var z: Float, internal var ambient: Float,
                                              internal var specular: Float, internal var radius: Float) {
        NORMAL(1f, 0.3f, 10f, 10f),
        EXTRUDE(0.5f, 0.8f, 13f, 7f);
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
        paint.maskFilter = BlurMaskFilter(radius, glowType.type)
    }

    fun setEmbossEffect(angle: Float, embossType: EmbossType) {
        val p = Utils.getVectorEndPoint(angle, 1f)
        paint.maskFilter = EmbossMaskFilter(floatArrayOf(p.x, p.y, embossType.z), embossType.ambient,
                embossType.specular, embossType.radius)
    }

    fun setShadow(radius: Float, dx: Float, dy: Float, color: Int) {
        paint.setShadowLayer(radius, dx, dy, color)
        hasShadowLayer = true
    }

    fun clearAllEffects() {
        if (hasShadowLayer) {
            paint.clearShadowLayer()
        }
        paint.maskFilter = null
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

}