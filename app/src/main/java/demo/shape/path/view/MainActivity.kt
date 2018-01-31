package demo.shape.path.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), SampleItemHolder.OnItemClickListener {

    var fragment: ShapeFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sample_list.layoutManager = LinearLayoutManager(this)
        sample_list.adapter = SampleAdapter(this)
    }

    override fun onItemClick(sample: Sample) {
        fragment = ShapeFragment.newInstance(sample)
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.container, fragment)
        fragmentTransaction.addToBackStack(ShapeFragment.javaClass.simpleName)
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
