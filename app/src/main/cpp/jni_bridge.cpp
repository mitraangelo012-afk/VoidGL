#include <jni.h>
#include "perf_layer.h"

extern "C" {

JNIEXPORT void JNICALL
Java_com_example_mcperf_NativeBridge_initLayer(JNIEnv* env, jobject thiz) {
    perf::init();
}

JNIEXPORT void JNICALL
Java_com_example_mcperf_NativeBridge_setResolutionScale(JNIEnv* env, jobject thiz, jfloat scale) {
    perf::setResolutionScale(static_cast<float>(scale));
}

JNIEXPORT void JNICALL
Java_com_example_mcperf_NativeBridge_setFpsCap(JNIEnv* env, jobject thiz, jint cap) {
    perf::setFpsCap(static_cast<int>(cap));
}

JNIEXPORT void JNICALL
Java_com_example_mcperf_NativeBridge_setReduceEffects(JNIEnv* env, jobject thiz, jboolean enabled) {
    perf::setReduceEffects(enabled == JNI_TRUE);
}

JNIEXPORT void JNICALL
Java_com_example_mcperf_NativeBridge_setCpuOptimization(JNIEnv* env, jobject thiz, jboolean enabled) {
    perf::setCpuOptimization(enabled == JNI_TRUE);
}

JNIEXPORT jint JNICALL
Java_com_example_mcperf_NativeBridge_getCurrentFps(JNIEnv* env, jobject thiz) {
    return static_cast<jint>(perf::getCurrentFps());
}

} // extern "C"