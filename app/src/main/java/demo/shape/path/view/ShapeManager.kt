package demo.shape.path.view

import android.graphics.Color
import android.graphics.PointF
import co.test.path.pathtest.ContourFillProvider
import shape.path.view.BodyFillProvider
import shape.path.view.PathProvider
import shape.path.view.PathShape
import shape.path.view.point.converter.PercentagePointConverter
import java.util.*

/**
 * Created by Gleb on 1/29/18.
 */
class ShapeManager {

    fun getShapes(sample: Sample): List<PathShape> {
        when(sample) {
            Sample.SIMPLE -> return getSampleShapes()
        }
    }

    private fun getSampleShapes(): List<PathShape> {
        //1st
        var list = arrayListOf<PathShape>()
        var pathProvider = PathProvider()
        var points = ArrayList<PointF>()
        points.add(PointF(0f, 0f))
        points.add(PointF(1f, 0f))
        points.add(PointF(0.5f, 1f))
        pathProvider.putLines(points, true, PathProvider.PathOperation.ADD)
        var body = BodyFillProvider()
        body.setColor(Color.GRAY)
        var contour = ContourFillProvider()
        contour.setColor(Color.BLACK)
        contour.setWidth(5f)
        list.add(PathShape.create()
                .setPath(pathProvider)
                .fillBody(body)
                .fillContour(contour)
                .setPointConverter(PercentagePointConverter()))
         //2nd
        pathProvider = PathProvider()
        pathProvider.putRect(PointF(0.5f, 0.5f), 1f, 1f, PathProvider.PathOperation.ADD)
        contour = ContourFillProvider()
        contour.setColor(Color.BLACK)
        contour.setWidth(5f)
        list.add(PathShape.create()
                .setPath(pathProvider)
                .fillContour(contour)
                .setPointConverter(PercentagePointConverter()))
        //3rd
        pathProvider = PathProvider()
        pathProvider.putOval(PointF(0.5f, 0.5f), 1f, 1f, PathProvider.PathOperation.ADD)
        body = BodyFillProvider()
        body.setColor(Color.GRAY)
        list.add(PathShape.create()
                .setPath(pathProvider)
                .fillBody(body)
                .setPointConverter(PercentagePointConverter()))
        //4th
        pathProvider = PathProvider()
        pathProvider.putArc(PointF(0.5f, 0.5f), 1f, 0.7f, 45f, 140f, PathProvider.PathOperation.ADD)
        contour = ContourFillProvider()
        contour.setColor(Color.BLACK)
        contour.setWidth(5f)
        body = BodyFillProvider()
        body.setColor(Color.GRAY)
        list.add(PathShape.create()
                .setPath(pathProvider)
                .fillBody(body)
                .fillContour(contour)
                .setPointConverter(PercentagePointConverter()))

        return list
    }
}