package demo.shape.path.view

import android.app.Fragment
import android.graphics.Color
import android.graphics.PointF
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.test.path.pathtest.ContourFillProvider
import kotlinx.android.synthetic.main.fragment_shape.*
import shape.path.view.BodyFillProvider
import shape.path.view.GradientProvider
import shape.path.view.PathProvider
import shape.path.view.PathShape
import shape.path.view.point.converter.PercentagePointConverter
import java.util.ArrayList

/**
 * Created by Gleb on 1/26/18.
 */
class ShapeFragment : Fragment() {

    private var sample: Sample = Sample.SIMPLE

    companion object {

        private val SAMPLE_TYPE_KEY: String = "sample_type_key"

        fun newInstance(sample: Sample): ShapeFragment {
            val f = ShapeFragment()
            val b = Bundle()
            b.putInt(SAMPLE_TYPE_KEY, sample.ordinal)
            f.arguments = b
            return f
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sample = Sample.values()[arguments!!.getInt(SAMPLE_TYPE_KEY, 0)]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_shape, null)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        gradient.addColor(Color.WHITE)
                .addColor(Color.BLACK)
                .setAngle(45f)
                .setType(GradientProvider.Type.LINEAR)
        val body = BodyFillProvider()
        body.setGradient(gradient)
        body.setRoundedCorners(180f)
        val contour = ContourFillProvider()
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