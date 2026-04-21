#include "perf_layer.h"
#include <GLES3/gl3.h>
#include <thread>
#include <cmath>

namespace perf {

static Settings gSettings;
static std::atomic<int> gFrameCount{0};
static std::chrono::steady_clock::time_point gLastFpsTime;
static int gCurrentFps = 0;
static bool gInitialized = false;

void init() {
    if (gInitialized) return;
    gLastFpsTime = std::chrono::steady_clock::now();
    gFrameCount = 0;
    gInitialized = true;
}

void setResolutionScale(float scale) {
    if (scale < 0.5f) scale = 0.5f;
    if (scale > 1.0f) scale = 1.0f;
    gSettings.resolutionScale = scale;
    applyResolutionScale();
}

void setFpsCap(int cap) {
    gSettings.fpsCap = cap < 0 ? 0 : cap;
}

void setReduceEffects(bool enabled) {
    gSettings.reduceEffects = enabled;
}

void setCpuOptimization(bool enabled) {
    gSettings.cpuOptimization = enabled;
}

int getCurrentFps() {
    return gCurrentFps;
}

void onFrameEnd() {
    if (!gInitialized) {
        init();
    }

    ++gFrameCount;
    auto now = std::chrono::steady_clock::now();
    auto elapsed = std::chrono::duration_cast<std::chrono::milliseconds>(now - gLastFpsTime).count();

    if (elapsed >= 1000) {
        gCurrentFps = static_cast<int>(gFrameCount * 1000.0 / elapsed);
        gFrameCount = 0;
        gLastFpsTime = now;
    }

    if (gSettings.fpsCap > 0) {
        static auto lastSwap = std::chrono::steady_clock::now();
        double frameTime = std::chrono::duration<double>(now - lastSwap).count();
        double target = 1.0 / gSettings.fpsCap;

        if (frameTime < target) {
            auto sleepTime = static_cast<int>((target - frameTime) * 1000);
            if (sleepTime > 0) {
                std::this_thread::sleep_for(std::chrono::milliseconds(sleepTime));
            }
        }
        lastSwap = std::chrono::steady_clock::now();
    }
}

void applyResolutionScale() {
    GLint viewport[4];
    glGetIntegerv(GL_VIEWPORT, viewport);

    int width = static_cast<int>(viewport[2] * gSettings.resolutionScale);
    int height = static_cast<int>(viewport[3] * gSettings.resolutionScale);

    glViewport(viewport[0], viewport[1], width, height);
}

} // namespace perf