package demo.shape.path.view.samples

import android.content.Context
import demo.shape.path.view.R

/**
 * Created by Gleb on 1/26/18.
 */
enum class Sample private constructor(private var stringResId: Int) {
    SIMPLE_SHAPES(R.string.sample_simple_shapes),
    CONTOUR_SAMPLE(R.string.sample_shape_contour),
    GRADIENT_AND_TEXTURE_SAMPLE(R.string.sample_fill_shape),
    SHAPE_SET_SAMPLE(R.string.sample_shape_set),
    POINT_CONVERTER_SAMPLE(R.string.sample_point_converter),
    MARKS_SAMPLE(R.string.sample_marks),
    TEXT_SAMPLE(R.string.sample_text),
    FILL_WITH_EFFECTS_SAMPLE(R.string.sample_fill_with_effects),
    CUSTOM_SHAPE_LIST_SAMPLE(R.string.sample_custom_shape_list),
    JSON_PARSER_SAMPLE(R.string.sample_json_parser);

    fun getName(context: Context): String {
        return context.getString(stringResId)
    }
}