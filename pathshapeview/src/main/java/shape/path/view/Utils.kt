package shape.path.view

import android.graphics.PointF

/**
 * Created by Gleb on 1/3/18.
 */
class Utils {

    companion object {
        fun convertToBezier(points: ArrayList<PointF>, epsilon: Float): ArrayList<PointF> {
            val n = points.size
            if (n < 3) {
                // Cannot create bezier with less than 3 points
                return points
            }
            val result = arrayOfNulls<PointF>(2 * (n - 2))
            var paX: Float
            var paY: Float
            var pbX = points[0].x
            var pbY = points[0].y
            var pcX = points[1].x
            var pcY = points[1].y
            for (i in 0 until n - 2) {
                paX = pbX
                paY = pbY
                pbX = pcX
                pbY = pcY
                pcX = points[i + 2].x
                pcY = points[i + 2].y
                val abX = pbX - paX
                val abY = pbY - paY
                var acX = pcX - paX
                var acY = pcY - paY
                val lac = Math.sqrt((acX * acX + acY * acY).toDouble()).toFloat()
                acX /= lac
                acY /= lac

                var proj = abX * acX + abY * acY
                proj = if (proj < 0) -proj else proj
                var apX = proj * acX
                var apY = proj * acY

                val p1X = pbX - epsilon * apX
                val p1Y = pbY - epsilon * apY
                result[2 * i] = PointF(p1X, p1Y)

                acX = -acX
                acY = -acY
                val cbX = pbX - pcX
                val cbY = pbY - pcY
                proj = cbX * acX + cbY * acY
                proj = if (proj < 0) -proj else proj
                apX = proj * acX
                apY = proj * acY

                val p2X = pbX - epsilon * apX
                val p2Y = pbY - epsilon * apY
                result[2 * i + 1] = PointF(p2X, p2Y)
            }
            return result.toMutableList() as ArrayList<PointF>
        }
    }

}