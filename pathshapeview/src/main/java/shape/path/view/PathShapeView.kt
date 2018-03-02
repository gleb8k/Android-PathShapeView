package shape.path.view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import shape.path.view.mark.MarkItem


/**
 * Created by root on 12/19/17.
 */
class PathShapeView : View {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    private var pathShape: PathShape = PathShape.create()
    private var onMarkClickListener: OnMarkClickListener? = null

    interface OnMarkClickListener {
        fun onMarkClick(markId: Int, markItem: MarkItem)
    }

    init {

    }

    fun setPath(pathShape: PathShape) {
        this.pathShape = pathShape
        updateView()
    }

    fun updateView() {
        post {
            pathShape.build(context, width.toFloat(), height.toFloat())
            if (pathShape.hasEffects() && layerType != View.LAYER_TYPE_SOFTWARE) {
                setLayerType(View.LAYER_TYPE_SOFTWARE, null)
            }
            else {
                invalidate()
            }
        }
    }

    fun setOnMarkClickListener(onMarkClickListener: OnMarkClickListener) {
        this.onMarkClickListener = onMarkClickListener
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        pathShape.draw(canvas!!)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (onMarkClickListener != null) {
            when(event!!.action) {
                MotionEvent.ACTION_UP,
                MotionEvent.ACTION_CANCEL -> {
                    pathShape.marks.forEach {
                        val markItem = it.onItemClick(event.x, event.y)
                        if (markItem != null) {
                            onMarkClickListener!!.onMarkClick(it.getId(), markItem)
                            return true
                        }
                    }
                }
            }
        }

        return super.onTouchEvent(event)
    }
}