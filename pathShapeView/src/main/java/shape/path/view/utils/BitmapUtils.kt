package shape.path.view.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.support.v4.content.res.ResourcesCompat


/**
 * Created by root on 2/13/18.
 */
class BitmapUtils {

    companion object {

        internal fun loadBitmap(context: Context, bitmap: Bitmap?, resId: Int, reqWidth: Float, reqHeight: Float): Bitmap? {
            if (bitmap == null && resId == -1) {
                return null
            }
            var result = bitmap
            try {
                if (result == null) {
                    val drawable = ResourcesCompat.getDrawable(context.resources, resId, null)
                    if (drawable != null) {
                        result = drawableToBitmap(drawable, reqWidth, reqHeight)
                    }
                }
                else {
                    val w = reqWidth.toInt()
                    val h = reqHeight.toInt()
                    if (reqWidth > 0 && reqHeight > 0 && (result.width != w || result.height != h)) {
                        result = scaleBitmap(result, w, h, false)
                    }
                }
            }
            catch (e: OutOfMemoryError) {
                e.printStackTrace()
            }
            catch (e: Error) {
                e.printStackTrace()
            }
            return result
        }

        private fun drawableToBitmap(drawable: Drawable, width: Float, height: Float): Bitmap? {
            val w = if (width > 0) width.toInt() else drawable.intrinsicWidth
            val h = if (height > 0) height.toInt() else drawable.intrinsicHeight
            if (w <= 0 || h <= 0) {
                return null
            }
            if (drawable is BitmapDrawable) {
                if (w == drawable.intrinsicWidth && h == drawable.intrinsicHeight) {
                    return drawable.bitmap
                }
                return scaleBitmap(drawable.bitmap, w, h, true)
            }
            val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            return bitmap
        }

        private fun scaleBitmap(bm: Bitmap, newWidth: Int, newHeight: Int, keepOrigin: Boolean): Bitmap {
            val width = bm.width
            val height = bm.height
            val scaleWidth = newWidth.toFloat() / width.toFloat()
            val scaleHeight = newHeight.toFloat() / height.toFloat()

            val matrix = Matrix()
            matrix.postScale(scaleWidth, scaleHeight)
            val resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false)
            if (!keepOrigin) {
                bm.recycle()
            }
            return resizedBitmap
        }
    }
}