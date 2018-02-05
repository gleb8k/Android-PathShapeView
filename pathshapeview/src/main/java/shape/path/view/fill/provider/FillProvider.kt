package shape.path.view

import android.graphics.*
import shape.path.view.point.converter.PointConverter

/**
 * Created by root on 1/10/18.
 */
abstract class FillProvider {
    internal val paint: Paint = Paint()
    private var gradient: GradientProvider? = null
    private var roundedCornerRadius: Float = 0f

    fun setColor(color: Int) {
        paint.color = color
    }

    fun setGradient(gradient: GradientProvider) {
        this.gradient = gradient
    }

    fun setRoundedCorners(radius: Float) {
        roundedCornerRadius = radius
    }

    protected open fun buildEffect(): PathEffect? {
        if (roundedCornerRadius != 0f) {
            return CornerPathEffect(roundedCornerRadius)
        }
        return null
    }

    internal fun build(converter: PointConverter) {
        paint.pathEffect = buildEffect()
        paint.shader = gradient?.build(converter)
    }
}