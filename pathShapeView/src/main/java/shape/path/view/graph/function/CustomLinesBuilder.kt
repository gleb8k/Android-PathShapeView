package shape.path.view

import android.graphics.Matrix
import android.graphics.Path
import android.graphics.PointF
import android.graphics.RectF
import shape.path.view.graph.function.GraphFunction
import shape.path.view.utils.Utils

/**
 * Created by root on 2/19/18.
 */
class CustomShapeBuilder {

    var points = arrayListOf<PointF>()
    private var isClosed = false

    companion object {
        val DEFAULT_STEP = 4.0f
    }

    fun addPoint(x: Float, y: Float) {
        points.add(PointF(x, y))
    }

    fun addPoint(p: PointF) {
        points.add(p)
    }

    fun addGraphPoints(width: Float, height: Float, function: GraphFunction) {
        this.addGraphPoints(width, height, DEFAULT_STEP, function)
    }

    fun addGraphPoints(width: Float, height: Float, stepValue: Float, function: GraphFunction) {
        val maxStepCount = (width / stepValue).toInt()
        var stepX = 0f
        var p = function.getTransformFunctionPoint(stepX, stepValue, maxStepCount)
        addPoint(p)

        while (stepX <= width) {
            stepX += stepValue
            p = function.getTransformFunctionPoint(stepX, stepValue, maxStepCount)
            if (function.originY < height && function.originY > 0f) {
                addPoint(p)
            }
            else {
                if (function.originY > height || function.originY < 0f) {
                    val cur = PointF(function.originX, function.originY)
                    val f = function.onFunctionGetValue(stepX - stepValue, stepValue, maxStepCount)
                    val prev = PointF(stepX - stepValue, f)
                    val res = Utils.getBoundsIntersection(RectF(0f, 0f, width, height), prev, cur)
                    if (res != null) {
                        p = function.getTransformPoint(res.x, res.y)
                    }
                }
                addPoint(p)
                break
            }
        }
    }

    fun setShapeClosed(isClosed: Boolean) {
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