package demo.shape.path.view.fragment.custom

import android.app.Fragment
import android.graphics.Color
import android.graphics.PointF
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import demo.shape.path.view.MainActivity
import demo.shape.path.view.R
import kotlinx.android.synthetic.main.fragment_custom_shape_list.*
import shape.path.view.PathProvider
import shape.path.view.fill.provider.*
import shape.path.view.graph.function.CustomLinesBuilder
import shape.path.view.graph.function.WaveFunction
import shape.path.view.point.converter.PercentagePointConverter
import shape.path.view.PathShape
import shape.path.view.fill.provider.ContourFillProvider

/**
 * Created by Gleb on 1/26/18.
 */
class CustomShapeListFragment : Fragment() {


    companion object {

        fun newInstance(): CustomShapeListFragment {
            val f = CustomShapeListFragment()
            return f
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_custom_shape_list, null)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).setSupportActionBar(toolBar)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = CustomShapeAdapter(activity)
        initHeader()
        initAvatarBehavior()
    }

    private fun initHeader() {
        val pathProvider = PathProvider()
        val f = WaveFunction(0.05f, 0.05f, WaveFunction.WaveType.SINE_ARC)
        f.offset(0f, 0.94f)
        val shape = CustomLinesBuilder()
        shape.addGraphPoints( 0f, 1f, -1f, 1f, f)
        shape.addPoint(1f, 0.4f)
        shape.addPoint(0.4f, 0f)
        shape.addPoint(0f, 0f)
        shape.setClosed(true)
        pathProvider.putCustomShape(shape, PathProvider.PathOperation.ADD)
        val body = BodyFillProvider()
        body.setColor(ContextCompat.getColor(activity, R.color.colorPrimary))
        header.setPath(PathShape.create()
                .setPath(pathProvider)
                .fillBody(body)
                .setPointConverter(PercentagePointConverter()))
    }

    private fun initAvatarBehavior() {
        val pathProvider = PathProvider()
        pathProvider.putCircle(PointF(0.5f, 0.5f), 0.5f, PathProvider.PathOperation.ADD)
        val body = BodyFillProvider()
        body.setTexture(R.drawable.bridge)
        body.fitTextureToSize(1f, 1f, true)
        val contour = ContourFillProvider()
        contour.setColor(Color.WHITE)
        contour.setWidth(8f)
        ava.setPath(PathShape.create()
                .setPath(pathProvider)
                .fillBody(body)
                .fillContour(contour)
                .setPointConverter(PercentagePointConverter()))
        appbar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            val kx = 1f + (verticalOffset.toFloat() / appbar.totalScrollRange.toFloat())
            Log.d("scale factor","kx = " + kx)
            ava.scaleX = kx
            ava.scaleY = kx
        }
    }

}