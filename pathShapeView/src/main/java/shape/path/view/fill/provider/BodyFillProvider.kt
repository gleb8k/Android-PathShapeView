package shape.path.view

import android.graphics.Paint

/**
 * Created by root on 1/10/18.
 */
class BodyFillProvider : FillProvider() {

    init {
        paint.style = Paint.Style.FILL
        //paint.strokeWidth = 100f
    }

    /*internal fun setStrokeWidth(width: Float) {
        paint.strokeWidth = width
    }*/
}