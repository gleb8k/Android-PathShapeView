package shape.path.view.graph.function

/**
 * Created by root on 2/15/18.
 */
class WaveFunction(var waveWidth: Float, var waveHeight: Float, var waveType: WaveType): GraphFunction() {

    enum class WaveType {
        SINE {
            override fun getFunction(waveWidth: Float, waveHeight: Float, xValue: Float, stepValue: Float): Float {
                val k = 2.0f * Math.PI / waveWidth
                return (waveHeight * Math.sin(k * xValue)).toFloat()
            }
        },
        SINE_ARC {
            override fun getFunction(waveWidth: Float, waveHeight: Float, xValue: Float, stepValue: Float): Float {
                val k = Math.PI / waveWidth
                return Math.abs(waveHeight * Math.sin(k * xValue)).toFloat()
            }
        },
        SINE_ARC_REVERSE {
            override fun getFunction(waveWidth: Float, waveHeight: Float, xValue: Float, stepValue: Float): Float {
                val k = Math.PI / waveWidth
                return -Math.abs(waveHeight * Math.cos(k * xValue)).toFloat()
            }
        },
        SQUARE {
            override fun getFunction(waveWidth: Float, waveHeight: Float, xValue: Float, stepValue: Float): Float {
                val k = 2.0f * Math.PI / waveWidth
                val f = Math.sin(k * xValue).toFloat()
                return waveHeight * Math.signum(f)
            }
        },
        TRIANGLE {
            override fun getFunction(waveWidth: Float, waveHeight: Float, xValue: Float, stepValue: Float): Float {
                val h = 2 * waveHeight
                val k = h / waveWidth
                        return Math.abs((xValue * k) % (h) - waveHeight)
            }
        },
        SAWTOOTH {
            override fun getFunction(waveWidth: Float, waveHeight: Float, xValue: Float, stepValue: Float): Float {
                val k =  waveHeight / waveWidth
                return (xValue * k) % waveHeight - waveHeight
            }
        };

        internal abstract fun getFunction(waveWidth: Float, waveHeight: Float, xValue: Float, stepValue: Float): Float

        companion object {

            fun fromString(type: String?): WaveType? {
                if (type.isNullOrEmpty()) return null
                WaveType.values().forEach {
                    if (it.toString() == type) {
                        return it
                    }
                }
                return null
            }
        }
    }

    init {

    }

    override fun onFunctionGetValue(xValue: Float, stepValue: Float, maxStepCount: Int): Float {
        return waveType.getFunction(waveWidth, waveHeight, xValue, stepValue)
    }

}