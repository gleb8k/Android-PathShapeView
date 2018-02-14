package shape.path.view.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v4.content.res.ResourcesCompat
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable



/**
 * Created by root on 2/12/18.
 */
class DrawableUtils {

    companion object {
        internal fun getScaledDrawable(context: Context, drawable: Drawable?, drawableResId: Int, width: Float, height: Float): Drawable? {
            var result: Drawable? = drawable
            if (result == null && drawableResId != -1) {
                result = ResourcesCompat.getDrawable(context.resources, drawableResId, null)
            }
            if (result != null) {
                scaleDrawable(result, width, height)
            }
            return result
        }

        private fun scaleDrawable(drawable: Drawable, width: Float, height: Float) {
            val w = drawable.bounds.width()
            val h = drawable.bounds.height()
            if (width > 0 && width.toInt() != w && height > 0 && height.toInt() != h) {
                drawable.setBounds(0, 0, width.toInt(), height.toInt())
            }
        }
    }


}