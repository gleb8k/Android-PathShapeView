package shape.path.view

/**
 * Created by root on 2/15/18.
 */
interface GraphFunction {
    fun onFunctionGetValue(xValue: Float, maxStepCount: Int): Float
}