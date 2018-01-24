package shape.path.view.point.converter

import android.graphics.Matrix
import android.graphics.PointF

/**
 * Created by root on 1/9/18.
 */
abstract class PointConverter {
    internal var screenWidth: Float = 0f
    internal var screenHeight: Float = 0f
    protected var matrix: Matrix = Matrix()

    open fun setScreenSize(screenWidth: Float, screenHeight: Float) {
        this.screenWidth = screenWidth
        this.screenHeight = screenHeight
    }

    open fun convertPoint(originPoint: PointF): PointF {
        val src = floatArrayOf(originPoint.x, originPoint.y)
        val dst = floatArrayOf(0f, 0f)
        matrix.mapPoints(dst, src)
        return PointF(dst[0], dst[1])
    }

    abstract internal fun getMatrix(): Matrix
}