package demo.shape.path.view

import android.graphics.Color
import android.graphics.PointF
import co.test.path.pathtest.ContourFillProvider
import shape.path.view.BodyFillProvider
import shape.path.view.GradientProvider
import shape.path.view.PathProvider
import shape.path.view.PathShape
import shape.path.view.point.converter.CoordinateConverter
import shape.path.view.point.converter.PercentagePointConverter
import java.util.*

/**
 * Created by Gleb on 1/29/18.
 */
class ShapeManager {

    fun getShapes(sample: Sample): List<PathShape> {
        when(sample) {
            Sample.SIMPLE_SHAPES -> return getSampleShapes()
            Sample.CONTOUR_SAMPLE -> return getContourSamples()
            Sample.GRADIENT_SAMPLE -> return getGradientSamples()
            Sample.POINT_CONVERTER_SAMPLE -> return getPointConverterSamples()
        }
    }

    private fun getSampleShapes(): List<PathShape> {
        //1st
        var list = arrayListOf<PathShape>()
        var pathProvider = PathProvider()
        var points = ArrayList<PointF>()
        points.add(PointF(0.1f, 0.9f))
        points.add(PointF(0.5f, 0.1f))
        points.add(PointF(0.9f, 0.9f))
        pathProvider.putLines(points, true, PathProvider.PathOperation.ADD)
        var body = BodyFillProvider()
        body.setColor(Color.GRAY)
        var contour = ContourFillProvider()
        contour.setColor(Color.BLACK)
        contour.setWidth(10f)
        list.add(PathShape.create()
                .setPath(pathProvider)
                .fillBody(body)
                .fillContour(contour)
                .setPointConverter(PercentagePointConverter()))
         //2nd
        pathProvider = PathProvider()
        pathProvider.putRect(PointF(0.5f, 0.5f), 0.9f, 0.9f, PathProvider.PathOperation.ADD)
        contour = ContourFillProvider()
        contour.setColor(Color.BLACK)
        contour.setWidth(10f)
        list.add(PathShape.create()
                .setPath(pathProvider)
                .fillContour(contour)
                .setPointConverter(PercentagePointConverter()))
        //3rd
        pathProvider = PathProvider()
        pathProvider.putOval(PointF(0.5f, 0.5f), 0.9f, 0.9f, PathProvider.PathOperation.ADD)
        body = BodyFillProvider()
        body.setColor(Color.DKGRAY)
        list.add(PathShape.create()
                .setPath(pathProvider)
                .fillBody(body)
                .setPointConverter(PercentagePointConverter()))
        //4th
        pathProvider = PathProvider()
        pathProvider.putArc(PointF(0.5f, 0.5f), 0.9f, 0.7f, 30f, 230f, PathProvider.PathOperation.ADD)
        contour = ContourFillProvider()
        contour.setColor(Color.LTGRAY)
        contour.setWidth(10f)
        body = BodyFillProvider()
        body.setColor(Color.BLUE)
        list.add(PathShape.create()
                .setPath(pathProvider)
                .fillBody(body)
                .fillContour(contour)
                .setPointConverter(PercentagePointConverter()))

        return list
    }

    private fun getContourSamples(): List<PathShape> {
        var list = arrayListOf<PathShape>()

        //1st
        var pathProvider = PathProvider()
        pathProvider.putRect(PointF(0.5f, 0.5f), 0.9f, 0.9f, PathProvider.PathOperation.ADD)

        var body = BodyFillProvider()
        body.setColor(Color.GRAY)
        var contour = ContourFillProvider()
        contour.setColor(Color.BLACK)
        contour.setWidth(20f)
        contour.addDotParams(20f, 20f)
        list.add(PathShape.create()
                .setPath(pathProvider)
                .fillBody(body)
                .fillContour(contour)
                .setPointConverter(PercentagePointConverter()))
        //2nd
        pathProvider = PathProvider()
        pathProvider.putRect(PointF(0.5f, 0.5f), 0.9f, 0.9f, PathProvider.PathOperation.ADD)
        contour = ContourFillProvider()
        contour.setColor(Color.BLACK)
        contour.setWidth(20f)
        contour.addDotParams(20f, 40f)
        contour.addDotParams(40f, 40f)
        contour.addDotParams(40f, 40f)
        contour.setIsDotRounded(true)
        list.add(PathShape.create()
                .setPath(pathProvider)
                .fillContour(contour)
                .setPointConverter(PercentagePointConverter()))
        //3rd
        pathProvider = PathProvider()
        pathProvider.putRect(PointF(0.5f, 0.5f), 0.9f, 0.9f, PathProvider.PathOperation.ADD)
        body = BodyFillProvider()
        body.setColor(Color.DKGRAY)
        body.setRoundedCorners(50f)
        contour = ContourFillProvider()
        contour.setColor(Color.BLACK)
        contour.setWidth(20f)
        contour.addDotParams(40f, 20f)
        contour.setRoundedCorners(50f)
        list.add(PathShape.create()
                .setPath(pathProvider)
                .fillBody(body)
                .fillContour(contour)
                .setPointConverter(PercentagePointConverter()))

        return list
    }

    private fun getGradientSamples(): List<PathShape> {
        var list = arrayListOf<PathShape>()

        //1st
        var pathProvider = PathProvider()
        pathProvider.putRect(PointF(0.5f, 0.5f), 0.9f, 0.9f, PathProvider.PathOperation.ADD)

        var body = BodyFillProvider()
        var gradient = GradientProvider()
        gradient.addColor(Color.BLACK)
                .addColor(Color.WHITE)
                .addColor(Color.BLACK)
                .setAngle(90f)
                .setType(GradientProvider.Type.LINEAR)
        body.setGradient(gradient)
        var contour = ContourFillProvider()
        contour.setWidth(20f)
        list.add(PathShape.create()
                .setPath(pathProvider)
                .fillBody(body)
                .setPointConverter(PercentagePointConverter()))
        //2nd
        pathProvider = PathProvider()
        pathProvider.putRect(PointF(0.5f, 0.5f), 0.9f, 0.9f, PathProvider.PathOperation.ADD)
        contour = ContourFillProvider()
        gradient = GradientProvider()
        gradient.addColor(Color.BLACK)
                .addColor(Color.WHITE)
                .addColor(Color.BLACK)
                .setType(GradientProvider.Type.RADIAL)
        contour.setColor(Color.BLACK)
        contour.setWidth(20f)
        body = BodyFillProvider()
        body.setGradient(gradient)
        list.add(PathShape.create()
                .setPath(pathProvider)
                .fillContour(contour)
                .fillBody(body)
                .setPointConverter(PercentagePointConverter()))
        //3rd
        pathProvider = PathProvider()
        pathProvider.putRect(PointF(0.5f, 0.5f), 0.9f, 0.9f, PathProvider.PathOperation.ADD)
        gradient = GradientProvider()
        gradient.addColor(Color.BLUE)
                .addColor(Color.WHITE)
                .addColor(Color.BLUE)
                .setType(GradientProvider.Type.SWEEP)
        body = BodyFillProvider()
        body.setGradient(gradient)
        body.setRoundedCorners(50f)
        gradient = GradientProvider()
        gradient.addColor(Color.BLACK, 0f)
                .addColor(Color.RED,0.1f)
                .addColor(Color.WHITE,0.5f)
                .addColor(Color.RED,0.9f)
                .addColor(Color.BLACK,1f)
                .setType(GradientProvider.Type.LINEAR)
        contour = ContourFillProvider()
        contour.setGradient(gradient)
        contour.setWidth(100f)
        contour.setRoundedCorners(50f)
        list.add(PathShape.create()
                .setPath(pathProvider)
                .fillBody(body)
                .fillContour(contour)
                .setPointConverter(PercentagePointConverter()))

        return list
    }

    private fun getPointConverterSamples(): List<PathShape> {
        var list = arrayListOf<PathShape>()

        //1st
        var pathProvider = PathProvider()
        pathProvider.putRect(PointF(200f, 200f), 100f, 100f, PathProvider.PathOperation.ADD)
        var body = BodyFillProvider()
        body.setColor(Color.LTGRAY)
        list.add(PathShape.create()
                .setPath(pathProvider)
                .fillBody(body))
        //2nd
        pathProvider = PathProvider()
        pathProvider.putRect(PointF(200f, 200f), 100f, 100f, PathProvider.PathOperation.ADD)
        body = BodyFillProvider()
        body.setColor(Color.LTGRAY)
        list.add(PathShape.create()
                .setPath(pathProvider)
                .fillBody(body)
                .setPointConverter(CoordinateConverter(400f, 500f)))
        //3rd
        pathProvider = PathProvider()
        pathProvider.putRect(PointF(0.5f, 0.5f), 0.9f, 0.9f, PathProvider.PathOperation.ADD)
        body = BodyFillProvider()
        body.setColor(Color.LTGRAY)
        list.add(PathShape.create()
                .setPath(pathProvider)
                .fillBody(body)
                .setPointConverter(PercentagePointConverter()))

        return list
    }
}