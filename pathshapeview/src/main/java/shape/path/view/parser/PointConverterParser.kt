package shape.path.view.parser

import org.json.JSONObject
import shape.path.view.point.converter.CoordinateConverter
import shape.path.view.point.converter.DefaultPointConverter
import shape.path.view.point.converter.PercentagePointConverter
import shape.path.view.point.converter.PointConverter

/**
 * Created by root on 3/27/18.
 */
class PointConverterParser {

    enum class ConverterType {
        PERCENTAGE,
        COORDINATE;

        companion object {
            fun fromString(type: String?): ConverterType? {
                if (type.isNullOrEmpty()) return null
                ConverterType.values().forEach {
                    if (it.toString() == type) {
                        return it
                    }
                }
                return null
            }
        }
    }

    companion object {

        internal fun fromJson(json: JSONObject?): PointConverter {
            json?.let {
                val sType = json.optString("type")
                if (!sType.isNullOrEmpty()) {
                    when(ConverterType.fromString(sType)) {
                        ConverterType.PERCENTAGE -> return PercentagePointConverter()
                        ConverterType.COORDINATE -> {
                            val width = json.optDouble("originWidth", 0.0).toFloat()
                            val height = json.optDouble("originHeight", 0.0).toFloat()
                            return CoordinateConverter(width, height)
                        }
                    }
                }
            }
            return DefaultPointConverter()
        }
    }

}
