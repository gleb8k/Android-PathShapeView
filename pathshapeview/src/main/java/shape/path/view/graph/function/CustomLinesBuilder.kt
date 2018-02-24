package shape.path.view.graph.function

import android.graphics.Path
import android.graphics.PointF
import android.graphics.RectF
import shape.path.view.utils.Utils

/**
 * Created by root on 2/19/18.
 */
class CustomLinesBuilder {

    private var points = arrayListOf<PointF>()
    private var isClosed = false

    companion object {
        val DEFAULT_STEP_COUNT = 200
    }

    fun addPoint(x: Float, y: Float) {
        points.add(PointF(x, y))
    }

    fun addPoint(p: PointF) {
        points.add(p)
    }

    fun addGraphPoints(minX: Float, maxX: Float, minY: Float, maxY: Float, function: GraphFunction) {
        this.addGraphPoints(minX, maxX, minY, maxY, DEFAULT_STEP_COUNT, function)
    }

    fun addGraphPoints(minX: Float, maxX: Float, minY: Float, maxY: Float, stepCount: Int, function: GraphFunction) {
        val stepValue = (maxX - minX) / stepCount
        var x = minX

        while (x < maxX) {
            var p = function.getTransformFunctionPoint(x, stepValue, stepCount)
            if (function.originY in minY..maxY) {
                addPoint(p)
            }
            else {
                if (function.originY > maxY || function.originY < minY) {
                    val cur = PointF(function.originX, function.originY)
                    val f = function.onFunctionGetValue(x - stepValue, stepValue, stepCount)
                    val prev = PointF(x - stepValue, f)
                    val res = Utils.getBoundsIntersection(RectF(minX, minY, maxX, maxY), prev, cur)
                    if (res != null) {
                        p = function.getTransformPoint(res.x, res.y)
                    }
                }
                addPoint(p)
                break
            }
            x += stepValue
        }
    }

    fun setClosed(isClosed: Boolean) {
        this.isClosed = isClosed
    }

    internal fun getPath() : Path {
        val p = Path()
        if (points.isNotEmpty()) {
            p.moveTo(points[0].x, points[0].y)
            for (i in 1 until points.size) {
                p.lineTo(points[i].x, points[i].y)
            }
            if (isClosed) {
                p.close()
            }
        }
        return p
    }
}