package shape.path.view.point.convertor

import android.graphics.Matrix
import android.graphics.PointF

/**
 * Created by root on 1/9/18.
 */
class PercentagePointConverter() : PointConverter() {

    /*override fun convertPoint(originPoint: PointF): PointF {
        val x = getValue(originPoint.x) * screenWidth
        val y = getValue(originPoint.y) * screenHeight
        return PointF(x, y)
    }*/

    private fun getValue(value: Float): Float {
        return when {
            value > 1f -> 1f
            value in 0f..1f -> value
            else -> 0f
        }
    }

    override fun setScreenSize(screenWidth: Float, screenHeight: Float) {
        super.setScreenSize(screenWidth, screenHeight)
        matrix.setScale(screenWidth, screenHeight)
    }

    override fun getMatrix(): Matrix {
        return matrix
    }
}