package shape.path.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup

/**
 * Created by root on 12/19/17.
 */
class PathShapeView : View {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    private var pathShape: PathShape = PathShape.create()

    fun setPath(pathShape: PathShape) {
        post {
            val p = getClearViewSize()
            pathShape.build(context, p.x, p.y)
            this.pathShape = pathShape
            invalidate()
        }
    }

    private fun getClearViewSize(): PointF {
        var w = width //- (paddingLeft + paddingRight)
        var h = height //- (paddingTop + paddingBottom)
        /*if (layoutParams is ViewGroup.MarginLayoutParams) {
            val p = layoutParams as ViewGroup.MarginLayoutParams
            w -= (p.leftMargin + p.rightMargin)
            h -= (p.topMargin + p.bottomMargin)
        }*/
        return PointF(w.toFloat(), h.toFloat())
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        pathShape.draw(canvas!!)
    }
}