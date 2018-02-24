package demo.shape.path.view.fragment

import android.app.Fragment
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import demo.shape.path.view.MainActivity
import demo.shape.path.view.R
import kotlinx.android.synthetic.main.fragment_custom_shape_list.*

/**
 * Created by Gleb on 1/26/18.
 */
class CustomShapeListFragment : Fragment() {


    companion object {

        private val SAMPLE_TYPE_KEY: String = "sample_type_key"

        fun newInstance(): CustomShapeListFragment {
            val f = CustomShapeListFragment()
            return f
        }
    }

    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //sample = Sample.values()[arguments!!.getInt(SAMPLE_TYPE_KEY, 0)]
    }*/

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_custom_shape_list, null)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).setSupportActionBar(toolBar)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = CustomShapeAdapter(activity)
        //collapsing_toolbar
    }
}