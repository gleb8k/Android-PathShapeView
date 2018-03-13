package shape.path.view.mark

import android.graphics.PointF
import android.graphics.RectF
import shape.path.view.point.converter.PointConverter

/**
 * Created by root on 2/1/18.
 */
class MarkItem {
    var index: Int = 0
    var position: PointF = PointF(0f, 0f)
    var label: String? = null
    var bounds = RectF()
    var isSelected = false

    internal fun build(pointConverter: PointConverter, touchWidth: Float,touchHeight: Float) {
        position = pointConverter.convertPoint(position)
        setBounds(touchWidth, touchHeight)
    }

    private fun setBounds(width: Float, height: Float) {
        if (width > 0f && height > 0f) {
            bounds.left = position.x - width / 2
            bounds.right = position.x + width / 2
            bounds.top = position.y - height / 2
            bounds.bottom = position.y + height / 2
        }
    }

    internal fun isInBounds(x: Float, y: Float): Boolean {
        return bounds.contains(x, y)
    }
}