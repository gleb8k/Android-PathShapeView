package co.test.path.pathtest

import android.graphics.*
import shape.path.view.FillProvider

/**
 * Created by root on 1/10/18.
 */
class ContourFillProvider: FillProvider() {

    private var dotParams: ArrayList<Float> = arrayListOf()

    init {
        paint.style = Paint.Style.STROKE
    }

    fun setWidth(width: Float) {
        paint.strokeWidth = width
    }

    fun setIsDotRounded(isDotRounded: Boolean) {
        if (isDotRounded) {
            paint.strokeCap = Paint.Cap.ROUND
        }
        else {
            paint.strokeCap = Paint.Cap.BUTT
        }
    }

    fun addDotParams(dotLength: Float, dotDistance: Float) {
        var l = dotLength - paint.strokeWidth
        if (l < 0f) {
            l = 0f
        }
        dotParams.add(l)
        dotParams.add(dotDistance)
    }

    override fun buildEffect(): PathEffect? {
        if (dotParams.isEmpty()) {
            return super.buildEffect()
        }
        else {
            val dashPathEffect = DashPathEffect(dotParams.toFloatArray(), 0f)
            val effect = super.buildEffect()
            if (effect == null) {
                return dashPathEffect
            }
            else {
                return ComposePathEffect(effect, dashPathEffect)
            }
        }
    }
}