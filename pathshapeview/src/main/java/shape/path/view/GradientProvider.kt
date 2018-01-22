package shape.path.view

import android.graphics.*
import shape.path.view.point.convertor.PointConverter

/**
 * Created by root on 1/10/18.
 */
class GradientProvider {

    private var type: Type = Type.LINEAR
    private var angle: Float = 0f
    private var colorList: ArrayList<Int> = arrayListOf()
    private var percentageColorPositions: ArrayList<Float> = arrayListOf()
    private var startPoint: PointF = PointF(Float.MIN_VALUE, Float.MIN_VALUE)
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
                updateLinearValues(angle, length)
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
        if (startPoint.x == Float.MIN_VALUE) {
            startPoint.x = width / 2
        }
        if (startPoint.y == Float.MIN_VALUE) {
            startPoint.y = height / 2
        }
        if (length == 0f) {
            length = getLength(startPoint.x, startPoint.y, width, height)
        }
    }

    private fun updateLinearValues(width: Float, height: Float) {
        if (startPoint.x == Float.MIN_VALUE) {
            startPoint.x = 0f
        }
        if (startPoint.y == Float.MIN_VALUE) {
            startPoint.y = 0f
        }
        if (length == 0f) {
            val angleInRadians = Math.toRadians(angle.toDouble())
            val x: Float
            val y: Float
            if (height > width) {
                y = height
                x = (height / Math.tan(angleInRadians)).toFloat()
            }
            else  {
                x = width
                y = (Math.tan(angleInRadians) * width).toFloat()
            }
            length = getLength(0f, 0f, x, y)
        }
        getLinearEndPoint(angle, length)
    }

    private fun getLinearEndPoint(angle: Float, length: Float) {
        val angleInRadians = Math.toRadians(angle.toDouble())
        val endX = Math.cos(angleInRadians) * length
        val endY = Math.sin(angleInRadians) * length
        endPoint.x = endX.toFloat()
        endPoint.y = endY.toFloat()
    }

    private fun getLength(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        return Math.sqrt(((x2 - x1)*(x2 - x1) + (y2 - y1)*(y2 - y1)).toDouble()).toFloat()
    }

    private fun convertAllParams(converter: PointConverter) {
        if (startPoint.x != Float.MIN_VALUE && startPoint.y != Float.MIN_VALUE) {
            startPoint = converter.convertPoint(startPoint)
        }
    }
}