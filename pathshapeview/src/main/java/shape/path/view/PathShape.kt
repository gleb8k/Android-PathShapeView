package shape.path.view

import android.content.Context
import android.graphics.*
import co.test.path.pathtest.ContourFillProvider
import shape.path.view.mark.Mark
import shape.path.view.point.converter.DefaultPointConverter
import shape.path.view.point.converter.PointConverter

/**
 * Created by Gleb on 1/3/18.
 */
class PathShape private constructor() {

    internal var body:BodyFillProvider? = null
    internal var contour:ContourFillProvider? = null
    internal var pathProvider: PathProvider? = null
    private var pointConverter: PointConverter = DefaultPointConverter()
    private var marks: ArrayList<Mark> = arrayListOf()

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

    internal fun build(context: Context, screenWidth: Float, screenHeight: Float) {
        pointConverter.setScreenSize(screenWidth, screenHeight)
        var strokeWidth = 0f
        contour?.let {
            it.build(pointConverter)
            strokeWidth = it.paint.strokeWidth
        }
        body?.let {
            it.build(pointConverter)
        }
        pathProvider?.build(pointConverter, strokeWidth)
        marks.forEach { it.build(context, pointConverter) }
    }

    internal fun draw(canvas: Canvas) {
        if (pathProvider != null) {
            if (body != null) {
                canvas.drawPath(pathProvider!!.path, body!!.paint)
            }
            if (contour != null && pathProvider!!.hasContourPath()) {
                canvas.drawPath(pathProvider!!.contourPath, contour!!.paint)
            }
            //canvas.dr
        }
        marks.forEach { it.draw(canvas) }
    }

    companion object {
       fun create(): PathShape {
           return PathShape()
       }
    }


}