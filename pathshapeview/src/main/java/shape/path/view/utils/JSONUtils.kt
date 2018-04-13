package shape.path.view.utils

import android.content.Context
import android.graphics.Color
import android.graphics.PointF
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import org.json.JSONObject
import java.io.IOException
import java.nio.charset.Charset

/**
 * Created by root on 3/20/18.
 */
class JSONUtils {
    companion object {

        internal fun jsonToPoint(json: JSONObject?): PointF? {
            return jsonToPoint(json, null)
        }

        internal fun jsonToPoint(json: JSONObject?, default: PointF?): PointF? {
            if (json == null) return default
            val x = json.optDouble("x")
            val y = json.optDouble("y")
            if (x == Double.NaN || y == Double.NaN) return default
            return PointF(x.toFloat(), y.toFloat())
        }

        internal fun jsonToTypeface(context: Context, json: JSONObject?): Typeface {
            if (json == null) return Typeface.DEFAULT
            val asset = json.optString("typefaceAsset")
            if (asset.isNullOrEmpty()) return Typeface.DEFAULT
            return Typeface.createFromAsset(context.assets, asset)
        }

        internal fun loadJsonFromAsset(context: Context, filename: String): JSONObject? {
            var json: JSONObject? = null
            try {
                val stream = context.assets.open(filename)
                val size = stream.available()
                val buffer = ByteArray(size)
                stream.read(buffer)
                stream.close()
                val string = String(buffer, Charset.forName("UTF-8"))
                json = JSONObject(string)
            } catch (ex: IOException) {
                ex.printStackTrace()
            }
            return json
        }

        internal fun stringToDrawableResId(context: Context, resName: String?): Int {
            if (resName.isNullOrEmpty()) return -1
            val resID = context.resources.getIdentifier(resName,
                    "drawable", context.packageName)
            return if (resID == 0) -1 else resID
        }
    }
}
