package shape.path.view

import android.graphics.*
import co.test.path.pathtest.ContourFillProvider
import shape.path.view.point.convertor.DefaultPointConverter
import shape.path.view.point.convertor.PointConverter

/**
 * Created by Gleb on 1/3/18.
 */
class PathShape private constructor() {

    internal var body:BodyFillProvider? = null
    internal var contour:ContourFillProvider? = null
    internal var pathProvider: PathProvider? = null
    private var pointConverter: PointConverter = DefaultPointConverter()


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

    internal fun build(screenWidth: Float, screenHeight: Float) {
       /* if (body != null && contour != null) {
            body!!.paint.strokeWidth = contour!!.paint.strokeWidth
        }*/
        pointConverter.setScreenSize(screenWidth, screenHeight)
        contour?.let {
            it.build(pointConverter)
            pathProvider?.build(pointConverter, it.paint.strokeWidth)
        }
        body?.let {
            it.build(pointConverter)
        }
    }

    internal fun draw(canvas: Canvas) {
        if (pathProvider != null) {
            if (body != null) {
                canvas.drawPath(pathProvider!!.path, body!!.paint)
            }
            if (contour != null) {
                canvas.drawPath(pathProvider!!.contourPath, contour!!.paint)
            }
        }
    }

    companion object {
       fun create(): PathShape {
           return PathShape()
       }
    }


}