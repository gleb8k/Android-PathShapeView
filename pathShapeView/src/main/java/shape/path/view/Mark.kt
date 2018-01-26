package shape.path.view

import android.graphics.PointF

/**
 * Created by root on 1/25/18.
 */
class Mark {

    internal val positions: ArrayList<PointF> = arrayListOf()
    internal var width: Float = 0f
    internal var height: Float = 0f

    fun addPosition(point: PointF) {
        positions.add(point)
    }

    fun addPositions(points: List<PointF>) {
        positions.addAll(points)
    }

    fun setSize(width: Float, height: Float) {
        this.width = width
        this.height = height
    }
}