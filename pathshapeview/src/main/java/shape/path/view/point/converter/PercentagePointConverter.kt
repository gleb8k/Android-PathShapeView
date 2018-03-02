package shape.path.view.point.converter

import android.graphics.Matrix

/**
 * Created by root on 1/9/18.
 */
class PercentagePointConverter: PointConverter() {

    override fun setScreenSize(screenWidth: Float, screenHeight: Float) {
        super.setScreenSize(screenWidth, screenHeight)
        matrix.setScale(screenWidth, screenHeight)
    }

    override fun getMatrix(): Matrix {
        return matrix
    }
}