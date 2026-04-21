#ifndef PERF_LAYER_H
#define PERF_LAYER_H

#include <atomic>
#include <chrono>
#include <cstdint>

namespace perf {

struct Settings {
    float resolutionScale = 1.0f;
    int fpsCap = 0;
    bool reduceEffects = false;
    bool cpuOptimization = false;
};

void init();
void setResolutionScale(float scale);
void setFpsCap(int cap);
void setReduceEffects(bool enabled);
void setCpuOptimization(bool enabled);
int getCurrentFps();
void onFrameEnd();
void applyResolutionScale();

} // namespace perf

#endif // PERF_LAYER_H