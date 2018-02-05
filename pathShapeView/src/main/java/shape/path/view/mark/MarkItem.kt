package shape.path.view

import android.graphics.PointF
import shape.path.view.point.converter.PointConverter

/**
 * Created by root on 2/1/18.
 */
class MarkItem {
    var position: PointF = PointF(0f, 0f)
    var label: String? = null

    internal fun build(pointConverter: PointConverter) {
        position = pointConverter.convertPoint(position)
    }
}