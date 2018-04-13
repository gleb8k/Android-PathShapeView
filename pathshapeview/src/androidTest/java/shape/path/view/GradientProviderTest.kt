package shape.path.view

import android.graphics.PointF
import android.support.test.runner.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*
import shape.path.view.fill.provider.GradientProvider
import shape.path.view.utils.MathUtils

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
@RunWith(AndroidJUnit4::class)
class GradientProviderTest {

    private var gradient: GradientProvider = GradientProvider()

    @Test
    @Throws(Exception::class)
    fun useAppContext() {
        // Context of the app under test.
        // test distance between points
        assertEquals(5f, MathUtils.getLength(0f, 3f, 4f, 0f))
        assertEquals(5f, MathUtils.getLength(0f, -3f, -4f, 0f))
        //
        var expectedP = PointF(500f, 500f)
        var actualP = MathUtils.getVectorEndPoint(45f, 500f, 500f)
        assertEquals(expectedP, actualP)
        expectedP = PointF(500f, 0f)
        actualP = MathUtils.getVectorEndPoint(0f, 500f, 500f)
        assertEquals(expectedP, actualP)
        expectedP = PointF(0f, 500f)
        actualP = MathUtils.getVectorEndPoint(90f, 500f, 500f)
        assertEquals(expectedP, actualP)
        expectedP = PointF(500f, 0f)
        actualP = MathUtils.getVectorEndPoint(180f, 500f, 500f)
        assertEquals(expectedP, actualP)
        expectedP = PointF(0f, 500f)
        actualP = MathUtils.getVectorEndPoint(-90f, 500f, 500f)
        assertEquals(expectedP, actualP)
        //3,4,5 (53.13, 36.87, 90)
        expectedP = PointF(3f, 4f)
        actualP = MathUtils.getVectorEndPoint(53.13f, 5f)
        assertEquals(expectedP, actualP)
        expectedP = PointF(4f, 3f)
        actualP = MathUtils.getVectorEndPoint(36.87f, 5f)
        assertEquals(expectedP, actualP)
    }
}
