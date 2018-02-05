package shape.path.view

import android.graphics.*
import shape.path.view.point.converter.PointConverter

/**
 * Created by root on 1/25/18.
 */
class TextConfigurator {
    internal val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    internal var textOffset: PointF = PointF(0f, 0f)

    enum class Style {
        BOLD,
        UNDERLINE,
        STRIKE,
        SUB_PIXEL,
        ITALIC
    }

    init {
        paint.textAlign = Paint.Align.CENTER
        paint.typeface = Typeface.DEFAULT
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

    internal fun build(pointConverter: PointConverter) {
        //textOffset = pointConverter.convertPoint(textOffset)
    }

    internal fun getPath(text: String, position: PointF): Path {
        val p = Path()
        val x = position.x + textOffset.x
        val y = position.y + textOffset.y
        paint.getTextPath(text, 0, text.length - 1, x, y, p)
        return p
    }

}