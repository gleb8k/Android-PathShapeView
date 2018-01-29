package demo.shape.path.view

import android.content.Context
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.view_item_sample.view.*

/**
 * Created by Gleb on 1/26/18.
 */
class SampleItemHolder private constructor(val itemClickListener: OnItemClickListener, itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var title: AppCompatTextView? = null
    private var sample: Sample = Sample.SIMPLE

    companion object {
        fun newInstance(parent: ViewGroup, itemClickListener: OnItemClickListener): SampleItemHolder {
            return SampleItemHolder(itemClickListener, LayoutInflater.from(parent.context).inflate(R.layout.view_item_sample, parent, false))
        }
    }

    interface OnItemClickListener {
        fun onItemClick(sample: Sample)
    }

    init {
        title = itemView.sample_name
        itemView.setOnClickListener { itemClickListener.onItemClick(sample) }
    }

    fun bind(sample: Sample) {
        this.sample = sample
        title!!.text = sample.getName(itemView.context)
    }
}