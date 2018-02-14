package shape.path.view.utils

import android.graphics.PointF

/**
 * Created by Gleb on 1/3/18.
 */
class Utils {

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
            val x: Float
            val y: Float
            if (boundsHeight >= boundsWidth && (angleInRadians % Math.PI) != 0.0) {
                y = boundsHeight
                x = Math.round(boundsHeight / Math.tan(angleInRadians)).toFloat()
            }
            else  {
                x = boundsWidth
                y = Math.round(Math.tan(angleInRadians) * boundsWidth).toFloat()
            }
            return PointF(x, y)
        }
    }

}