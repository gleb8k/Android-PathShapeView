package shape.path.view

import android.graphics.PointF
import android.support.test.runner.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*
import shape.path.view.utils.Utils

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
@RunWith(AndroidJUnit4::class)
class MathTest {

    @Test
    @Throws(Exception::class)
    fun useAppContext() {
        // Context of the app under test.
        // test distance between points
        var p = Utils.getLinesIntersection(PointF(-1f, 0f), PointF(1f, 0f), PointF(0f, -1f), PointF(0f, 1f))
        assertEquals(PointF(0f,0f), p)
    }
}
