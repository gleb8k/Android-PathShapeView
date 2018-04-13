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
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initFromAttrs(context, attrs)
    }

    private var pathShape: PathShape? = null
    private var onMarkClickListener: OnMarkClickListener? = null

    interface OnMarkClickListener {
        fun onMarkClick(markId: Int, markItem: MarkItem)
    }

    private fun initFromAttrs(context: Context?, attrs: AttributeSet?) {
        val a = context?.theme?.obtainStyledAttributes(attrs, R.styleable.PathShapeView, 0, 0)
        a?.let {
            try {
                val fileName = it.getString(R.styleable.PathShapeView_assetShapeResource)
                if (!fileName.isNullOrEmpty()) {
                    pathShape = PathShape.fromAsset(context, fileName)
                }
            }
            finally {
                it.recycle()
            }
        }
    }

    fun setPath(pathShape: PathShape) {
        this.pathShape = pathShape
        updateView()
    }

    private fun updateView() {
        if (width != 0 && height != 0) {
            update()
        }
        else {
            post {
                update()
            }
        }
    }

    private fun update() {
        pathShape?.let {
            it.build(context, width.toFloat(), height.toFloat())
            if (it.hasEffects() && layerType != View.LAYER_TYPE_SOFTWARE) {
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

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        pathShape?.let {
            it.build(context, w.toFloat(), h.toFloat())
            if (it.hasEffects() && layerType != View.LAYER_TYPE_SOFTWARE) {
                setLayerType(View.LAYER_TYPE_SOFTWARE, null)
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        pathShape?.draw(canvas!!)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (onMarkClickListener != null && pathShape != null) {
            when(event!!.action) {
                MotionEvent.ACTION_UP -> {
                    pathShape!!.marks.forEach {
                        val markItem = it.onItemClick(event.x, event.y)
                        if (markItem != null) {
                            if (it.hasSelectedDrawable()) {
                                invalidate()
                            }
                            onMarkClickListener!!.onMarkClick(it.getId(), markItem)
                            return true
                        }
                    }
                }
            }
            super.onTouchEvent(event)
            return true
        }
        return super.onTouchEvent(event)
    }
}