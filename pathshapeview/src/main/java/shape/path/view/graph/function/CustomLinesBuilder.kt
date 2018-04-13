package shape.path.view.graph.function

import android.content.Context
import android.graphics.Path
import android.graphics.PointF
import android.graphics.RectF
import org.json.JSONObject
import shape.path.view.utils.JSONUtils
import shape.path.view.utils.MathUtils

/**
 * Created by root on 2/19/18.
 */
class CustomLinesBuilder {

    private var points = arrayListOf<PointF>()
    private var isClosed = false

    companion object {

        val DEFAULT_STEP_COUNT = 200

        internal fun fromJson(json: JSONObject?): CustomLinesBuilder? {
            if (json == null) return null
            val array = json.optJSONArray("lines") ?: return null
            val result = CustomLinesBuilder()
            for (i in 0 until array.length()) {
                val item = array.optJSONObject(i)
                if (item == null) continue
                val type = WaveFunction.WaveType.fromString(item.optString("type"))
                if (type == null) {
                    val point = JSONUtils.jsonToPoint(item)
                    if (point == null) continue
                    result.addPoint(point)
                }
                else {
                    val waveWidth = item.optDouble("waveWidth", 0.0).toFloat()
                    val waveHeight = item.optDouble("waveHeight", 0.0).toFloat()
                    val offset = JSONUtils.jsonToPoint(item.optJSONObject("offset"))
                    val skew = JSONUtils.jsonToPoint(item.optJSONObject("skew"))
                    val rotate = item.optDouble("rotate", 0.0).toFloat()
                    val f = WaveFunction(waveWidth, waveHeight, type)
                    offset?.let { f.offset(offset.x, offset.y) }
                    skew?.let { f.skew(skew.x, skew.y) }
                    if (rotate != 0f) f.rotate(rotate)
                    val minPoint = JSONUtils.jsonToPoint(item.optJSONObject("minPoint")) ?: PointF(0f,0f)
                    val maxPoint = JSONUtils.jsonToPoint(item.optJSONObject("maxPoint")) ?: PointF(0f,0f)
                    val stepCount = item.optInt("stepCount", DEFAULT_STEP_COUNT)
                    result.addGraphPoints(minPoint.x, maxPoint.x, minPoint.y, maxPoint.y, stepCount, f)
                }
            }
            val isClosed = json.optBoolean("isClosed", false)
            result.setClosed(isClosed)
            return result
        }
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
                    val res = MathUtils.getBoundsIntersection(RectF(minX, minY, maxX, maxY), prev, cur)
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