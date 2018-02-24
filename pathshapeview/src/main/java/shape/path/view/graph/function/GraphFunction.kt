package shape.path.view.graph.function

import android.graphics.Matrix
import android.graphics.PointF

/**
 * Created by root on 2/15/18.
 */
abstract class GraphFunction {

    open val matrix = Matrix()
    var originX = 0f
    var originY = 0f

    abstract fun onFunctionGetValue(xValue: Float, stepValue: Float, maxStepCount: Int): Float

    fun offset(dx: Float, dy: Float) {
        matrix.postTranslate(dx, dy)
    }

    fun rotate(angle: Float) {
        matrix.postRotate(angle)
    }

    fun skew(kx: Float, ky: Float) {
        matrix.postSkew(kx, ky)
    }

    open fun getTransformFunctionPoint(xValue: Float, stepValue: Float, maxStepCount: Int): PointF {
        originX = xValue
        originY = onFunctionGetValue(xValue, stepValue, maxStepCount)
        return getTransformPoint(originX, originY)
    }

    fun getTransformPoint(x: Float, y: Float): PointF {
        if (matrix.isIdentity) {
            return PointF(x, y)
        }
        val array = FloatArray(2)
        matrix.mapPoints(array, floatArrayOf(x, y))
        return PointF(array[0], array[1])
    }
 }