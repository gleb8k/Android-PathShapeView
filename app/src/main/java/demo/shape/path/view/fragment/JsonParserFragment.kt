package demo.shape.path.view.fragment

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import demo.shape.path.view.R
import demo.shape.path.view.samples.Sample
import demo.shape.path.view.samples.ShapeManager
import kotlinx.android.synthetic.main.fragment_json_parser.*
import kotlinx.android.synthetic.main.fragment_shape.*
import shape.path.view.PathShape
import shape.path.view.PathShapeView
import shape.path.view.mark.MarkItem

/**
 * Created by Gleb on 1/26/18.
 */
class JsonParserFragment : Fragment(), PathShapeView.OnMarkClickListener, View.OnClickListener {

    companion object {

        fun newInstance(): JsonParserFragment {
            val f = JsonParserFragment()
            return f
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_json_parser, null)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        path_shape.setOnMarkClickListener(this)
        path_shape.setOnClickListener(this)
    }

    override fun onMarkClick(markId: Int, markItem: MarkItem) {
        Toast.makeText(activity, "mark Id: " + markId + ", item index: " + markItem.index +
                ", item label: " + markItem.label, Toast.LENGTH_SHORT).show()
    }

    override fun onClick(v: View?) {
        Toast.makeText(activity, "view Id: " + v!!.id, Toast.LENGTH_SHORT).show() //To change body of created functions use File | Settings | File Templates.
    }
}