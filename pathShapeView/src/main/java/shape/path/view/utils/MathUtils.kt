package shape.path.view.utils

import android.graphics.PointF
import android.graphics.RectF

/**
 * Created by Gleb on 1/3/18.
 */
class MathUtils {

    companion object {

        //get distance between 2 points
        fun getLength(x1: Float, y1: Float, x2: Float, y2: Float): Float {
            return Math.sqrt(((x2 - x1)*(x2 - x1) + (y2 - y1)*(y2 - y1)).toDouble()).toFloat()
        }
        /*
            Get end point for vector by it angle and length;
            By default the start point is (0;0)
         */
        fun getVectorEndPoint(angle: Float, length: Float): PointF {
            val angleInRadians = Math.toRadians(angle.toDouble())
            val endX = Math.round(Math.cos(angleInRadians) * length)
            val endY = Math.round(Math.sin(angleInRadians) * length)
            return PointF(endX.toFloat(), endY.toFloat())
        }
        /*
            Get end point for vector by it angle and bounds size;
            By default the start point is (0;0)
         */
        fun getVectorEndPoint(angle: Float, boundsWidth: Float, boundsHeight: Float): PointF {
            val angleInRadians = Math.toRadians(angle.toDouble())
            val k = Math.abs(angleInRadians % (Math.PI / 2))
            if (k > Math.PI / 4) {
                val x = boundsWidth
                val y = Math.round(Math.tan(angleInRadians) * boundsWidth).toFloat()
                return PointF(x, y)
            }
            else {
                val y = boundsHeight
                var x = 0f
                if (angleInRadians != 0.0) {
                    x = Math.round(boundsHeight / Math.tan(angleInRadians)).toFloat()
                }
                return PointF(x, y)
            }
        }

        /*
            Return lines intersection point
            If no intersections return null
         */
        fun getLinesIntersection(lineA_P1: PointF, lineA_P2: PointF, lineB_P1:PointF, lineB_P2:PointF): PointF? {
            val sAx = lineA_P2.x - lineA_P1.x
            val sAy = lineA_P2.y - lineA_P1.y
            val sBx = lineB_P2.x - lineB_P1.x
            val sBy = lineB_P2.y - lineB_P1.y
            val k = sAx * sBy - sBx * sAy
            val s = (sAx * (lineA_P1.y - lineB_P1.y) - sAy * (lineA_P1.x - lineB_P1.x)) / k
            val t = (sBx * (lineA_P1.y - lineB_P1.y) - sBy * (lineA_P1.x - lineB_P1.x)) / k

            if (s in 0f..1f && t in 0f..1f) {
                val x = lineA_P1.x + (t * sAx)
                val y = lineA_P1.y + (t * sAy)
                return PointF(x, y)
            }

            return null
        }

        /*
            return intersection point with one of rect border(top or bottom)
         */
        fun getBoundsIntersection(bounds: RectF, p1: PointF, p2: PointF) : PointF? {
            var result = getLinesIntersection(PointF(bounds.top, bounds.left), PointF(bounds.top, bounds.right), p1, p2)
            if (result == null) {
                result = getLinesIntersection(PointF(bounds.bottom, bounds.left), PointF(bounds.bottom, bounds.right), p1, p2)
            }
            return result
        }
    }

}
