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
        this.pathShape = pathShape
        updateView()
    }

    fun updateView() {
        post {
            pathShape.build(context, width.toFloat(), height.toFloat())
            invalidate()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        pathShape.draw(canvas!!)
    }
}