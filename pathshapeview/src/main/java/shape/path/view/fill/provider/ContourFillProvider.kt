package shape.path.view.fill.provider

import android.graphics.*

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
        dotParams.add(dotLength)
        dotParams.add(dotDistance)
    }

    override fun buildEffect(): PathEffect? {
        if (dotParams.isEmpty()) {
            return super.buildEffect()
        }
        else {
            val dashPathEffect = DashPathEffect(getDots(), 0f)
            val effect = super.buildEffect()
            if (effect == null) {
                return dashPathEffect
            }
            else {
                return ComposePathEffect(dashPathEffect, effect)
            }
        }
    }

    private fun getDots(): FloatArray {
        if (paint.strokeCap == Paint.Cap.ROUND) {
            val array = FloatArray(dotParams.size)
            for (i in 0 until dotParams.size) {
                if ((i + 1) % 2 == 1) {
                    array[i] = getDotLength(dotParams[i])
                }
                else {
                    array[i] = dotParams[i]
                }
            }
            return array
        }
        return dotParams.toFloatArray()
    }

    private fun getDotLength(length: Float): Float {
        var l = length - paint.strokeWidth
        if (l < 0f) {
            l = 0f
        }
        return l
    }
}