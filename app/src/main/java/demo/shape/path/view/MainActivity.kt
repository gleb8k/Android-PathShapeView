package demo.shape.path.view

import android.app.Fragment
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import demo.shape.path.view.fragment.JsonParserFragment
import demo.shape.path.view.fragment.custom.CustomShapeListFragment
import demo.shape.path.view.fragment.ShapeFragment
import demo.shape.path.view.samples.Sample
import demo.shape.path.view.samples.SampleAdapter
import demo.shape.path.view.samples.SampleItemHolder
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), SampleItemHolder.OnItemClickListener {

    var fragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sample_list.layoutManager = LinearLayoutManager(this)
        sample_list.adapter = SampleAdapter(this)
    }

    override fun onItemClick(sample: Sample) {
        var tag: String
        when (sample) {
            Sample.CUSTOM_SHAPE_LIST_SAMPLE -> {
                fragment = CustomShapeListFragment.newInstance()
                tag = CustomShapeListFragment::javaClass.name
            }
            Sample.JSON_PARSER_SAMPLE -> {
                fragment = JsonParserFragment.newInstance()
                tag = JsonParserFragment::javaClass.name
            }
            else -> {
                fragment = ShapeFragment.newInstance(sample)
                tag = ShapeFragment::javaClass.name
            }
        }

        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.container, fragment)
        fragmentTransaction.addToBackStack(tag)
        fragmentTransaction.commit()
    }

    override fun onBackPressed() {
        if (fragmentManager.backStackEntryCount > 0) {
            fragmentManager.popBackStack()
        }
        else {
            super.onBackPressed()
        }
    }
}
