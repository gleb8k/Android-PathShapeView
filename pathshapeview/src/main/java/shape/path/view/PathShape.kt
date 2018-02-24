package shape.path.view

import android.content.Context
import android.graphics.*
import shape.path.view.fill.provider.BodyFillProvider
import shape.path.view.fill.provider.ContourFillProvider
import shape.path.view.mark.Mark
import shape.path.view.point.converter.DefaultPointConverter
import shape.path.view.point.converter.PointConverter

/**
 * Created by Gleb on 1/3/18.
 */
class PathShape private constructor() {

    internal var body: BodyFillProvider? = null
    internal var contour: ContourFillProvider? = null
    internal var pathProvider: PathProvider? = null
    private var pointConverter: PointConverter = DefaultPointConverter()
    private var marks: ArrayList<Mark> = arrayListOf()
    private var isBuilt: Boolean = false

    fun setPath(provider: PathProvider): PathShape {
        this.pathProvider = provider
        return this
    }

    fun fillBody(provider: BodyFillProvider): PathShape {
        this.body = provider
        return this
    }

    fun fillContour(provider: ContourFillProvider): PathShape {
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

    internal fun build(context: Context, screenWidth: Float, screenHeight: Float): Boolean {
        if (!isBuilt) {
            isBuilt = true
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
            return true
        }
        return false
    }

    internal fun draw(canvas: Canvas) {
        if (pathProvider != null) {
            if (body != null) {
                canvas.drawPath(pathProvider!!.path, body!!.paint)
            }
            if (contour != null && pathProvider!!.hasContourPath()) {
                canvas.drawPath(pathProvider!!.contourPath, contour!!.paint)
            }
        }
        marks.forEach { it.draw(canvas) }
    }

    companion object {
       fun create(): PathShape {
           return PathShape()
       }
    }


}