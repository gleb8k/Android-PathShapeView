package demo.shape.path.view

import android.content.Context

/**
 * Created by Gleb on 1/26/18.
 */
enum class Sample {
    SIMPLE(R.string.sample_simple);

    private var stringResId: Int = 0

    private constructor(stringResId: Int) {
        this.stringResId = stringResId
    }

    fun getName(context: Context): String {
        return context.getString(stringResId)
    }
}