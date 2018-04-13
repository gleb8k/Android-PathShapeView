package shape.path.view

import android.content.Context
import android.graphics.*
import shape.path.view.fill.provider.BodyFillProvider
import shape.path.view.fill.provider.ContourFillProvider
import shape.path.view.mark.Mark
import shape.path.view.parser.*
import shape.path.view.point.converter.DefaultPointConverter
import shape.path.view.point.converter.PointConverter
import shape.path.view.utils.JSONUtils

/**
 * Created by Gleb on 1/3/18.
 */
class PathShape private constructor() {

    internal var body: BodyFillProvider? = null
    internal var contour: ContourFillProvider? = null
    internal var pathProvider: PathProvider? = null
    internal var marks: ArrayList<Mark> = arrayListOf()
    private var pointConverter: PointConverter = DefaultPointConverter()

    fun setPath(provider: PathProvider?): PathShape {
        this.pathProvider = provider
        return this
    }

    fun fillBody(provider: BodyFillProvider?): PathShape {
        this.body = provider
        return this
    }

    fun fillContour(provider: ContourFillProvider?): PathShape {
        this.contour = provider
        return this
    }

    fun setPointConverter(pointConverter: PointConverter): PathShape {
        this.pointConverter = pointConverter
        return this
    }

    fun addMark(mark: Mark): PathShape {
        marks.add(mark)
        return this
    }

    internal fun hasEffects(): Boolean {
        return contour?.hasEffects() ?: false || body?.hasEffects() ?: false
    }

    internal fun build(context: Context, screenWidth: Float, screenHeight: Float) {
        pointConverter.setScreenSize(screenWidth, screenHeight)
        var strokeWidth = 0f
        contour?.let {
            it.build(context, pointConverter)
            strokeWidth = it.paint.strokeWidth
        }
        body?.let {
            it.build(context, pointConverter)
        }
        pathProvider?.build(pointConverter, strokeWidth)
        marks.forEach { it.build(context, pointConverter) }
    }

    internal fun draw(canvas: Canvas) {
        if (pathProvider != null && pathProvider!!.hasPath()) {
            if (body != null) {
                canvas.drawPath(pathProvider!!.shapePath, body!!.paint)
            }
            if (contour != null) {
                canvas.drawPath(pathProvider!!.shapePath, contour!!.paint)
            }
        }
        marks.forEach { it.draw(canvas) }
    }

    companion object {

        fun create(): PathShape {
           return PathShape()
        }

        fun fromAsset(context: Context, fileName: String): PathShape {
            val shape = PathShape()
            val json = JSONUtils.loadJsonFromAsset(context, fileName)
            json?.let {
                val path = it.optJSONObject("pathProvider")
                shape.setPath(PathParser.fromJson(context, path))
                val body = it.optJSONObject("fillBody")
                shape.fillBody(BodyFillProvider.fromJson(context, body))
                val contour = it.optJSONObject("fillContour")
                shape.fillContour(ContourFillProvider.fromJson(context, contour))
                val marks = json.optJSONArray("marks")
                marks?.let {
                    for(i in 0 until marks.length()) {
                        val mark = Mark.fromJson(context, marks.optJSONObject(i))
                        mark?.let {
                            shape.addMark(mark)
                        }
                    }
                }
                val converter = it.optJSONObject("pointConverter")
                shape.setPointConverter(PointConverterParser.fromJson(converter))
            }
            return shape
        }
    }


}