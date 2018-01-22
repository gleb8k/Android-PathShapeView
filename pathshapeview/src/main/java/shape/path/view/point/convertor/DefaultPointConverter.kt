package shape.path.view.point.convertor

import android.graphics.Matrix
import android.graphics.PointF

/**
 * Created by Gleb on 1/22/18.
 */
class DefaultPointConverter : PointConverter() {

    override fun getMatrix(): Matrix {
        return matrix
    }

    override fun convertPoint(originPoint: PointF): PointF {
        return originPoint
    }

}