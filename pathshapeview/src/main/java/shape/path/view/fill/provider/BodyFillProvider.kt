package shape.path.view.fill.provider

import android.content.Context
import android.graphics.Paint
import org.json.JSONObject

/**
 * Created by root on 1/10/18.
 */
class BodyFillProvider : FillProvider() {

    init {
        paint.style = Paint.Style.FILL
    }

    companion object {

        internal fun fromJson(context: Context, json: JSONObject?): BodyFillProvider? {
            if (json == null) return null
            val provider = BodyFillProvider()
            provider.fromJson(context, json)
            return provider
        }
    }

}