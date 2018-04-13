package shape.path.view.parser

import android.content.Context
import android.graphics.PointF
import org.json.JSONObject
import shape.path.view.PathProvider
import shape.path.view.TextConfigurator
import shape.path.view.graph.function.CustomLinesBuilder
import shape.path.view.utils.JSONUtils

/**
 * Created by root on 3/15/18.
 */
class PathParser {
    enum class ShapeType {
        LINES,
        ARC,
        OVAL,
        CIRCLE,
        POLY,
        STAR,
        TEXT,
        RECT,
        ROUND_RECT,
        CUSTOM_SHAPE;

        companion object {
            fun fromString(type: String?): ShapeType? {
                if (type.isNullOrEmpty()) return null
                ShapeType.values().forEach {
                    if (it.toString() == type) {
                        return it
                    }
                }
                return null
            }
        }
    }

    companion object {
        internal fun fromJson(context: Context, json: JSONObject?): PathProvider? {
            if (json == null) return null
            val parser = PathParser()
            return parser.toPathProvider(context, json)
        }
    }

    internal fun toPathProvider(context: Context, json: JSONObject): PathProvider {
        val result = PathProvider()
        val shapes = json.optJSONArray("shapes")
        shapes?.let {
            for (i in 0 until shapes.length()) {
                putShape(context, result, shapes.optJSONObject(i))
            }
        }
        return result
    }

    private fun putShape(context: Context, pathProvider: PathProvider, json: JSONObject?) {
        if (json == null) return
        val type = ShapeType.fromString(json.optString("shapeType"))
        val operation = PathProvider.PathOperation.fromString(json.optString("pathOperation"))
        operation?.let {
            when (type) {
                ShapeType.LINES -> {
                    val array = json.optJSONArray("lines") ?: return
                    val lines = arrayListOf<PointF>()
                    for (i in 0 until array.length()) {
                        val point = JSONUtils.jsonToPoint(array.optJSONObject(i))
                        point?.let { lines.add(point) }
                    }
                    val isClosed = json.optBoolean("isClosed", false)
                    pathProvider.putLines(lines, isClosed, operation)
                }
                ShapeType.ARC -> {
                    val centerPoint = JSONUtils.jsonToPoint(json.optJSONObject("centerPoint")) ?: return
                    val w = json.optDouble("width", 0.0).toFloat()
                    val h = json.optDouble("height", 0.0).toFloat()
                    val startAngle = json.optDouble("startAngle", 0.0).toFloat()
                    val sweepAngle = json.optDouble("sweepAngle", 0.0).toFloat()
                    pathProvider.putArc(centerPoint, w, h, startAngle, sweepAngle, operation)
                }
                ShapeType.OVAL -> {
                    val centerPoint = JSONUtils.jsonToPoint(json.optJSONObject("centerPoint")) ?: return
                    val w = json.optDouble("width", 0.0).toFloat()
                    val h = json.optDouble("height", 0.0).toFloat()
                    pathProvider.putOval(centerPoint, w, h, operation)
                }
                ShapeType.CIRCLE -> {
                    val centerPoint = JSONUtils.jsonToPoint(json.optJSONObject("centerPoint")) ?: return
                    val r = json.optDouble("radius", 0.0).toFloat()
                    pathProvider.putCircle(centerPoint, r, operation)
                }
                ShapeType.POLY -> {
                    val centerPoint = JSONUtils.jsonToPoint(json.optJSONObject("centerPoint")) ?: return
                    val r = json.optDouble("radius", 0.0).toFloat()
                    val a = json.optDouble("angleRotation", 0.0).toFloat()
                    val c = json.optInt("sidesCount", 0)
                    pathProvider.putPoly(centerPoint, r, a, c, operation)
                }
                ShapeType.STAR -> {
                    val centerPoint = JSONUtils.jsonToPoint(json.optJSONObject("centerPoint")) ?: return
                    val innerR = json.optDouble("innerRadius", 0.0).toFloat()
                    val outerR = json.optDouble("outerRadius", 0.0).toFloat()
                    val a = json.optDouble("angleRotation", 0.0).toFloat()
                    val c = json.optInt("sidesCount", 0)
                    pathProvider.putStar(centerPoint, outerR, innerR, a, c, operation)
                }
                ShapeType.RECT -> {
                    val centerPoint = JSONUtils.jsonToPoint(json.optJSONObject("centerPoint")) ?: return
                    val w = json.optDouble("width", 0.0).toFloat()
                    val h = json.optDouble("height", 0.0).toFloat()
                    pathProvider.putRect(centerPoint, w, h, operation)
                }
                ShapeType.ROUND_RECT -> {
                    val centerPoint = JSONUtils.jsonToPoint(json.optJSONObject("centerPoint")) ?: return
                    val w = json.optDouble("width", 0.0).toFloat()
                    val h = json.optDouble("height", 0.0).toFloat()
                    val r = json.optDouble("cornerRadius", 0.0).toFloat()
                    pathProvider.putRoundRect(centerPoint, w, h, r, operation)
                }
                ShapeType.TEXT -> {
                    val centerPoint = JSONUtils.jsonToPoint(json.optJSONObject("centerPoint")) ?: return
                    val w = json.optDouble("width", 0.0).toFloat()
                    val h = json.optDouble("height", 0.0).toFloat()
                    val text = json.optString("text")
                    val tc = TextConfigurator.fromJson(context, json.optJSONObject("textConfigurator"))
                    if (tc == null || text.isNullOrEmpty()) return
                    pathProvider.putText(centerPoint, w, h, text, tc, operation)
                }
                ShapeType.CUSTOM_SHAPE -> {
                    val linesBuilder = CustomLinesBuilder.fromJson(json)
                    if (linesBuilder == null) return
                    pathProvider.putCustomShape(linesBuilder, operation)
                }
            }
        }
    }


}
