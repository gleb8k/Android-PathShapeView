package shape.path.view

import android.graphics.*
import shape.path.view.point.converter.PointConverter

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


    enum class Type {
        LINEAR,
        RADIAL,
        SWEEP
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

    fun addColor(color: Int, percentage: Float): GradientProvider {
        colorList.add(color)
        percentageColorPositions.add(percentage)
        return this
    }

    internal fun build(converter: PointConverter): Shader {
        convertAllParams(converter)
        val percentage: FloatArray? = if (percentageColorPositions.size < colorList.size) null else percentageColorPositions.toFloatArray()
        return when(type) {
            Type.LINEAR -> {
                updateLinearValues(converter.screenWidth , converter.screenHeight)
                LinearGradient(startPoint.x, startPoint.y, endPoint.x, endPoint.y, colorList.toIntArray(), percentage, Shader.TileMode.MIRROR)
            }
            Type.RADIAL -> {
                updateRadialValues(converter.screenWidth, converter.screenHeight)
                RadialGradient(startPoint.x, startPoint.y, length, colorList.toIntArray(), percentage, Shader.TileMode.MIRROR)
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
            length = Utils.getLength(startPoint.x, startPoint.y, width, height)
        }
    }

    private fun updateLinearValues(width: Float, height: Float) {
        if (length == 0f) {
            val p = Utils.getVectorEndPoint(angle, width, height)
            length = Utils.getLength(0f, 0f, p.x, p.y)
        }
        endPoint = Utils.getVectorEndPoint(angle, length)
    }

    private fun convertAllParams(converter: PointConverter) {
        if (isStartPointSet) {
            startPoint = converter.convertPoint(startPoint)
        }
    }
}