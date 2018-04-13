package demo.shape.path.view.fragment.custom

import android.content.Context
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import demo.shape.path.view.R
import shape.path.view.PathProvider
import shape.path.view.PathShape
import shape.path.view.fill.provider.BodyFillProvider
import shape.path.view.fill.provider.GradientProvider
import shape.path.view.graph.function.CustomLinesBuilder
import shape.path.view.graph.function.WaveFunction
import shape.path.view.point.converter.PercentagePointConverter

/**
 * Created by Gleb on 1/26/18.
 */
class CustomShapeAdapter(var context: Context): RecyclerView.Adapter<CustomShapeItemHolder>() {

    val items: ArrayList<PathShape> = arrayListOf()

    init {
        WaveFunction.WaveType.values().forEach {
            val pathProvider = PathProvider()
            val f = WaveFunction(0.2f, 0.1f, it)
            f.offset(0f, 0.85f)
            val shape = CustomLinesBuilder()
            shape.addGraphPoints( 0f, 1f, -1f, 1f, f)
            shape.addPoint(1f, 0f)
            shape.addPoint(0f, 0f)
            shape.setClosed(true)
            pathProvider.putCustomShape(shape, PathProvider.PathOperation.ADD)
            val body = BodyFillProvider()
            val gradient = GradientProvider()
            gradient.addColor(Color.LTGRAY)
            gradient.addColor(ContextCompat.getColor(context, R.color.colorAccent))
            gradient.setAngle(90f)
            body.setGradient(gradient)
            body.setRoundedCorners(30f)

            items.add(PathShape.create()
                    .setPath(pathProvider)
                    .fillBody(body)
                    .setPointConverter(PercentagePointConverter()))
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: CustomShapeItemHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomShapeItemHolder {
        return CustomShapeItemHolder.newInstance(parent)
    }
}