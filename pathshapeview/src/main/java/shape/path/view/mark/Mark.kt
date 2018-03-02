package shape.path.view.mark

import android.content.Context
import android.graphics.Canvas
import android.graphics.PointF
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.support.v4.content.res.ResourcesCompat
import shape.path.view.utils.DrawableUtils
import shape.path.view.TextConfigurator
import shape.path.view.point.converter.PointConverter

/**
 * Created by root on 1/25/18.
 */
class Mark {

    private var id: Int = 0
    private var touchWidth = 0f
    private var touchHeight = 0f
    internal var items: ArrayList<MarkItem> = arrayListOf()
    internal var width: Float = 0f
    internal var height: Float = 0f
    internal var textConfigurator: TextConfigurator? = null
    internal var drawable: Drawable? = null
    internal var drawableResId: Int = -1

    private var itemsChanged = false
    private var drawableChanged = false

    fun addPosition(point: PointF) {
        this.addPosition(point, null)
    }

    fun addPosition(point: PointF, label: String?) {
        val item = MarkItem()
        item.position = point
        item.label = label
        item.index = items.size
        items.add(item)
        itemsChanged = true
    }

    fun addPositions(points: List<PointF>) {
        this.addPositions(points, arrayListOf())
    }

    fun addPositions(points: List<PointF>, labels: List<String>) {
        for (i in 0 until points.size) {
            if (i < labels.size) {
                addPosition(points[i], labels[i])
            }
            else {
                addPosition(points[i])
            }
        }
    }

    fun resetItems() {
        items.clear()
        itemsChanged = true
    }

    fun setTextConfigurator(configurator: TextConfigurator) {
        this.textConfigurator = configurator
    }

    fun setDrawable(resId: Int) {
        drawableResId = resId
        drawable = null
        drawableChanged = true
    }

    fun setDrawable(drawable: Drawable) {
        this.drawable = drawable
        drawableChanged = true
    }

    fun fitDrawableToSize(width: Float, height: Float) {
        this.width = width
        this.height = height
        drawableChanged = true
    }

    fun setId(id: Int) {
        this.id = id
    }

    fun getId(): Int {
        return id
    }

    fun setTouchBounds(width: Float, height: Float) {
        this.touchWidth = width
        this.touchHeight = height
    }

    internal fun build(context: Context, pointConverter: PointConverter) {
        if (itemsChanged) {
            items.forEach { it.build(pointConverter, touchWidth, touchHeight) }
            itemsChanged = false
        }
        if (drawableChanged) {
            drawable = DrawableUtils.getScaledDrawable(context, drawable, drawableResId, width, height)
            drawableChanged = false
        }
    }

    internal fun draw(canvas: Canvas) {
        items.forEach {
            updatePositionDrawableAndDraw(it.position, canvas)
            updateLableAndDraw(it, canvas)
        }
    }

    private fun updatePositionDrawableAndDraw(point: PointF, canvas: Canvas) {
        drawable?.let {
            val w = it.bounds.width()
            val h = it.bounds.height()
            val left = (point.x - w / 2).toInt()
            val top = (point.y - h / 2).toInt()
            val right = (point.x + w / 2).toInt()
            val bottom = (point.y + h / 2).toInt()
            it.setBounds(left, top, right, bottom)
            it.draw(canvas)
        }
    }

    private fun updateLableAndDraw(markItem: MarkItem, canvas: Canvas) {
        if (markItem.label != null) {
            textConfigurator?.let {
                val x = markItem.position.x + it.textOffset.x
                val y = markItem.position.y + it.textOffset.y
                canvas.drawText(markItem.label, x, y, it.paint)
            }
        }
    }

    internal fun onItemClick(x: Float, y: Float): MarkItem? {
        if (touchWidth > 0f && touchHeight > 0f) {
            items.forEach {
                if (it.isInBounds(x, y)) {
                    return it
                }
            }
        }
        return null
    }
}