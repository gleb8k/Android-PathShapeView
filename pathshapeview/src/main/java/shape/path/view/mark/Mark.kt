package shape.path.view.mark

import android.content.Context
import android.graphics.Canvas
import android.graphics.PointF
import android.graphics.drawable.Drawable
import android.support.v4.content.res.ResourcesCompat
import shape.path.view.utils.DrawableUtils
import shape.path.view.TextConfigurator
import shape.path.view.point.converter.PointConverter

/**
 * Created by root on 1/25/18.
 */
class Mark {

    internal var items: ArrayList<MarkItem> = arrayListOf()
    internal var width: Float = 0f
    internal var height: Float = 0f
    internal var textConfigurator: TextConfigurator? = null
    internal var drawable: Drawable? = null
    internal var drawableResId: Int = -1

    fun addPosition(point: PointF) {
        this.addPosition(point, null)
    }

    fun addPosition(point: PointF, label: String?) {
        val item = MarkItem()
        item.position = point
        item.label = label
        items.add(item)
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

    fun setTextConfigurator(configurator: TextConfigurator) {
        this.textConfigurator = configurator
    }

    fun setDrawable(resId: Int) {
        drawableResId = resId
        drawable = null
    }

    fun setDrawable(drawable: Drawable) {
        this.drawable = drawable
    }

    fun fitDrawableToSize(width: Float, height: Float) {
        this.width = width
        this.height = height
    }

    internal fun build(context: Context, pointConverter: PointConverter) {
        items.forEach { it.build(pointConverter) }
        textConfigurator?.build(pointConverter)
        //updateDrawable(context)
        drawable = DrawableUtils.getScaledDrawable(context, drawable, drawableResId, width, height)
    }

    internal fun draw(canvas: Canvas) {
        items.forEach {
            updatePositionDrawableAndDraw(it.position, canvas)
            updateLableAndDraw(it, canvas)
        }
    }

    private fun updatePositionDrawableAndDraw(point: PointF, canvas: Canvas) {
        drawable?.let {
            val w = it.bounds.right - it.bounds.left
            val h = it.bounds.bottom - it.bounds.top
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
}