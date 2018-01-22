package demo.shape.path.view

import android.graphics.Color
import android.graphics.PointF
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import co.test.path.pathtest.ContourFillProvider
import kotlinx.android.synthetic.main.activity_main.*
import shape.path.view.BodyFillProvider
import shape.path.view.GradientProvider
import shape.path.view.PathProvider
import shape.path.view.PathShape
import shape.path.view.point.convertor.PercentagePointConverter
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        path.setPath(getPathShape())
    }

    fun getPathShape(): PathShape {
        val pathProvider = PathProvider()
        val list = ArrayList<PointF>()
        list.add(PointF(0f, 0f))
        list.add(PointF(1f, 0f))
        list.add(PointF(1f, 1f))
        list.add(PointF(0f, 1f))
        pathProvider.putLines(list, true, PathProvider.PathOperation.ADD)
        val gradient = GradientProvider()
        gradient.addColor(Color.WHITE, 0.5f)
                .addColor(Color.BLACK)
                .setAngle(45f)
                .setType(GradientProvider.Type.SWEEP)
        val body = BodyFillProvider()
        body.setGradient(gradient)
        body.setRoundedCorners(180f)
        val contour = ContourFillProvider()
        //contour.setGradient(gradient)
        contour.setColor(Color.BLACK)
        contour.setRoundedCorners(180f)
        contour.setWidth(30f)
        return PathShape.create()
                .setPath(pathProvider)
                .fillBody(body)
                .fillContour(contour)
                .setPointConverter(PercentagePointConverter())
    }
}
