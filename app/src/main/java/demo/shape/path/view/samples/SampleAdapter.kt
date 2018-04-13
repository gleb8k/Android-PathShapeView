package demo.shape.path.view.samples

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

/**
 * Created by Gleb on 1/26/18.
 */
class SampleAdapter(val itemClickListener: SampleItemHolder.OnItemClickListener): RecyclerView.Adapter<SampleItemHolder>() {

    val items: Array<Sample> = Sample.values()

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: SampleItemHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SampleItemHolder {
        return SampleItemHolder.newInstance(parent, itemClickListener)
    }
}