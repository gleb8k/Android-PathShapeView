package demo.shape.path.view.fragment

import android.annotation.SuppressLint
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import demo.shape.path.view.R
import kotlinx.android.synthetic.main.view_item_custom_shape.view.*
import shape.path.view.PathShape
import shape.path.view.PathShapeView

/**
 * Created by Gleb on 1/26/18.
 */
class CustomShapeItemHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var title: AppCompatTextView? = null
    private var shapeView: PathShapeView? = null

    companion object {
        fun newInstance(parent: ViewGroup): CustomShapeItemHolder {
            return CustomShapeItemHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_item_custom_shape, parent, false))
        }
    }

    init {
        title = itemView.title
        shapeView = itemView.custom_shape_view
    }

    fun bind(shape: PathShape) {
        this.shapeView!!.setPath(shape)
        title!!.text = itemView.context.getString(R.string.custom_shape_item_title, adapterPosition)
    }
}