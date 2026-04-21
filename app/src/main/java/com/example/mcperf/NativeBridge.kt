package com.example.mcperf

object NativeBridge {
    init {
        System.loadLibrary("perf_layer")
    }

    external fun initLayer()

    external fun setResolutionScale(scale: Float)
    external fun setFpsCap(cap: Int)
    external fun setReduceEffects(enabled: Boolean)
    external fun setCpuOptimization(enabled: Boolean)
    external fun getCurrentFps(): Int
}