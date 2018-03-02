package shape.path.view

import android.graphics.*
import android.text.TextPaint

/**
 * Created by root on 1/25/18.
 */
class TextConfigurator {
    internal val paint: TextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
    internal var textOffset: PointF = PointF(0f, 0f)

    enum class Style {
        BOLD,
        UNDERLINE,
        STRIKE,
        SUB_PIXEL,
        ITALIC
    }

    init {
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.textAlign = Paint.Align.CENTER
        paint.typeface = Typeface.DEFAULT
        //paint.strokeWidth = 30f
    }

    fun setTextSize(size: Float) {
        paint.textSize = size
    }

    fun setTextColor(color: Int) {
        paint.color = color
    }

    fun setTypeface(typeface: Typeface) {
        paint.typeface = typeface
    }

    fun setTextOffset(offset: PointF) {
        textOffset = offset
    }

    fun setStyle(vararg style: Style) {
        paint.isFakeBoldText = false
        paint.isUnderlineText = false
        paint.isStrikeThruText = false
        paint.isSubpixelText = false
        paint.textSkewX = 0f
        style.forEach {
            when(it) {
                Style.BOLD -> paint.isFakeBoldText = true
                Style.UNDERLINE -> paint.isUnderlineText = true
                Style.STRIKE -> paint.isStrikeThruText = true
                Style.SUB_PIXEL -> paint.isSubpixelText = true
                Style.ITALIC -> paint.textSkewX = -0.2f
            }
        }
    }

    internal fun getPath(text: String, position: PointF, textWidth: Float, textHeight: Float): Path {
        val path = Path()
        if (text.isNotEmpty()) {
            val x = position.x
            val y = position.y
            paint.getTextPath(text, 0, text.length, 0f, 0f, path)
            val bounds = Rect()
            paint.getTextBounds(text, 0, text.length, bounds)
            bounds.offsetTo(0, 0)
            val currentBounds = RectF(bounds)
            val matrix = Matrix()
            val newBounds = RectF(x, y, textWidth + x, textHeight + y)
            matrix.setRectToRect(currentBounds, newBounds, Matrix.ScaleToFit.CENTER)
            path.transform(matrix)
        }
        return path
    }

}