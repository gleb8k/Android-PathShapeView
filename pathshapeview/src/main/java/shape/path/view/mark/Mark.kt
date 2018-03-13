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
    private var items: ArrayList<MarkItem> = arrayListOf()
    private var width: Float = 0f
    private var height: Float = 0f
    private var textConfigurator: TextConfigurator? = null
    private var drawable: Drawable? = null
    private var drawableResId: Int = -1
    private var selectedDrawable: Drawable? = null
    private var selectedDrawableResId: Int = -1

    private var multiSelectionModeEnabled = false
    private var isCheckable = false

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

    fun setSelectedDrawable(drawable: Drawable) {
        this.selectedDrawable = drawable
        drawableChanged = true
    }

    fun setSelectedDrawable(resId: Int) {
        selectedDrawableResId = resId
        selectedDrawable = null
        drawableChanged = true
    }

    fun setDrawable(drawable: Drawable) {
        this.drawable = drawable
        drawableChanged = true
    }

    fun setMultiSelectionModeEnabled(enabled: Boolean){
        multiSelectionModeEnabled = enabled
    }

    fun setCheckable(isCheckable: Boolean) {
        this.isCheckable = isCheckable
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
            selectedDrawable = DrawableUtils.getScaledDrawable(context, selectedDrawable, selectedDrawableResId, width, height)
            drawableChanged = false
        }
    }

    internal fun draw(canvas: Canvas) {
        items.forEach {
            updatePositionDrawableAndDraw(it, canvas)
            updateLableAndDraw(it, canvas)
        }
    }

    private fun updatePositionDrawableAndDraw(markItem: MarkItem, canvas: Canvas) {
        drawable?.let {
            val w = it.bounds.width()
            val h = it.bounds.height()
            val left = (markItem.position.x - w / 2).toInt()
            val top = (markItem.position.y - h / 2).toInt()
            val right = (markItem.position.x + w / 2).toInt()
            val bottom = (markItem.position.y + h / 2).toInt()
            if (markItem.isSelected && selectedDrawable != null) {
                selectedDrawable!!.setBounds(left, top, right, bottom)
                selectedDrawable!!.draw(canvas)
            }
            else {
                it.setBounds(left, top, right, bottom)
                it.draw(canvas)
            }

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
            for(i in 0 until items.size) {
                val it = items[i]
                if (it.isInBounds(x, y)) {
                    if (!multiSelectionModeEnabled) {
                        unselectAllExceptItem(i)
                    }
                    if (isCheckable) {
                        it.isSelected = !it.isSelected
                    }
                    else {
                        it.isSelected = true
                    }
                    return it
                }
            }
        }
        return null
    }

    private fun unselectAllExceptItem(itemIndex: Int) {
        for(i in 0 until items.size) {
            if (i != itemIndex) {
                items[i].isSelected = false
            }
        }
    }

    internal fun hasSelectedDrawable(): Boolean {
        return selectedDrawable != null
    }
}