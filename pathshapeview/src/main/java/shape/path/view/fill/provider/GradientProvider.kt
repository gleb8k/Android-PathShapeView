package shape.path.view.fill.provider

import android.graphics.*
import org.json.JSONObject
import shape.path.view.point.converter.PointConverter
import shape.path.view.utils.JSONUtils
import shape.path.view.utils.MathUtils

/**
 * Created by root on 1/10/18.
 */
class GradientProvider {

    private var type: Type = Type.LINEAR
    private var angle: Float = 0f
    private var colorList: ArrayList<Int> = arrayListOf()
    private var percentageColorPositions: ArrayList<Float> = arrayListOf()
    private var startPoint: PointF = PointF(0f, 0f)
    private var isStartPointSet: Boolean = false
    private var endPoint: PointF = PointF(0f, 0f)
    internal var length: Float = 0f

    companion object {
        internal fun fromJson(json: JSONObject?): GradientProvider? {
            if (json == null) return null
            val gradient = GradientProvider()
            val type = Type.fromString(json.optString("type"))
            type?.let { gradient.setType(it) }
            val angle = json.optDouble("angle", 0.0).toFloat()
            gradient.setAngle(angle)
            val length = json.optDouble("length", 0.0).toFloat()
            gradient.setLength(length)
            val startPoint = JSONUtils.jsonToPoint(json.optJSONObject("startPoint"), PointF(0f, 0f))
            gradient.setStartPoint(startPoint!!)
            val colors = json.optJSONArray("colors")
            colors?.let {
                for (i in 0 until it.length()) {
                    val item = it.optJSONObject(i)
                    if (item == null) continue
                    val color = Color.parseColor(item.optString("color"))
                    val percentage = item.optDouble("position")
                    if (percentage == Double.NaN) {
                        gradient.addColor(color)
                    }
                    else {
                        gradient.addColor(color, percentage.toFloat())
                    }
                }
            }
            return gradient
        }
    }

    enum class Type {
        LINEAR,
        RADIAL,
        SWEEP;

        companion object {
            fun fromString(type: String?): Type? {
                if (type.isNullOrEmpty()) return null
                Type.values().forEach {
                    if (it.toString() == type) {
                        return it
                    }
                }
                return null
            }
        }
    }

    fun setType(type: Type): GradientProvider {
        this.type = type
        return this
    }

    fun setAngle(angle: Float): GradientProvider {
        this.angle = angle
        return this
    }

    fun setLength(length: Float): GradientProvider {
        this.length = length
        return this
    }

    fun setStartPoint(startPoint: PointF): GradientProvider {
        this.startPoint = startPoint
        isStartPointSet = true
        return this
    }

    fun addColor(color: Int): GradientProvider {
        colorList.add(color)
        return this
    }

    fun addColor(color: Int, colorPosition: Float): GradientProvider {
        colorList.add(color)
        percentageColorPositions.add(colorPosition)
        return this
    }

    internal fun build(converter: PointConverter, tileMode: Shader.TileMode): Shader {
        convertAllParams(converter)
        val percentage: FloatArray? = if (percentageColorPositions.size < colorList.size) null else percentageColorPositions.toFloatArray()
        return when(type) {
            Type.LINEAR -> {
                updateLinearValues(converter.screenWidth , converter.screenHeight)
                LinearGradient(startPoint.x, startPoint.y, endPoint.x, endPoint.y, colorList.toIntArray(), percentage, tileMode)
            }
            Type.RADIAL -> {
                updateRadialValues(converter.screenWidth, converter.screenHeight)
                RadialGradient(startPoint.x, startPoint.y, length, colorList.toIntArray(), percentage, tileMode)
            }
            Type.SWEEP -> {
                updateRadialValues(converter.screenWidth, converter.screenHeight)
                SweepGradient(startPoint.x, startPoint.y, colorList.toIntArray(), percentage)
            }
        }
    }

    private fun updateRadialValues(width: Float, height: Float) {
        if (!isStartPointSet) {
            startPoint.x = width / 2
            startPoint.y = height / 2
        }
        if (length == 0f) {
            length = MathUtils.getLength(startPoint.x, startPoint.y, width, height)
        }
    }

    private fun updateLinearValues(width: Float, height: Float) {
        if (length == 0f) {
            val p = MathUtils.getVectorEndPoint(angle, width, height)
            length = MathUtils.getLength(0f, 0f, p.x, p.y)
        }
        endPoint = MathUtils.getVectorEndPoint(angle, length)
    }

    private fun convertAllParams(converter: PointConverter) {
        if (isStartPointSet) {
            startPoint = converter.convertPoint(startPoint)
        }
    }
}