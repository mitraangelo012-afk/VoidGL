# MC Perf Optimizer

*Updated*

Android performance optimization layer for Minecraft Java Edition via mobile launchers (Zalith, PojavLauncher).

## Requirements

- **JDK 17** - [Download](https://adoptium.net/temurin/releases/?version=17)
- **Android Studio Arctic Fox or newer** - [Download](https://developer.android.com/studio)
- **NDK r26** - Installed via Android Studio SDK Manager
- **CMake 3.22+** - Installed via Android Studio SDK Manager

## Project Structure

```
MCPerfOptimizer/
├── app/
│   ├── src/main/
│   │   ├── java/com/example/mcperf/
│   │   │   ├── MainActivity.kt      # WebView host + JNI bridge
│   │   │   ├── SettingsManager.kt  # DataStore preferences
│   │   │   └── NativeBridge.kt   # JNI wrapper
│   │   ├── cpp/
│   │   │   ├── perf_layer.h     # Header
│   │   │   ├── perf_layer.cpp  # Core optimization logic
│   │   │   └── jni_bridge.cpp  # JNI entry points
│   │   ├── assets/ui/
│   │   │   ├── index.html      # Dashboard UI
│   │   │   ├── style.css      # Glassmorphic dark theme
│   │   │   └── app.js        # UI ↔ Kotlin bridge
│   │   ├── res/
│   │   └── cmake/
│   │       └── CMakeLists.txt
│   └── build.gradle
├── build.gradle
├── settings.gradle
└── gradle.properties
```

## Build Instructions

1. **Open Android Studio** → *Open an existing project* → Select `MCPerfOptimizer/` folder

2. **Wait for Gradle sync** - It will download dependencies and configure NDK/CMake

3. **Fix SDK if needed** - If prompted, install:
   - Android SDK Platform 33
   - NDK r26
   - CMake 3.22.1

4. **Build Debug APK** - *Build → Build Bundle(s) / APK(s) → Build APK(s)*

5. **Install on device** - Transfer `app/build/outputs/apk/debug/app-debug.apk` to your Android 12+ device

## Features

- **Resolution Scale** - 0.5x to 1.0x render scaling
- **FPS Cap** - 0 (unlimited) to 120 FPS with frame pacing
- **Reduce Effects** - Toggle for particle/visual reduction hints
- **CPU Optimization** - Thread priority hints
- **Presets** - Low / Balanced / Ultra modes
- **Live FPS Display** - Real-time frame counter

## Integration with Launchers

To use the native library with Zalith/PojavLauncher:

1. Install the MCPerfOptimizer app on device
2. The launcher's data directory will have access to `libperf_layer.so`
3. Launchers can load it via `System.loadLibrary("perf_layer")`

For full integration, modify the launcher's JVM args to include:
```
-Djava.library.path=/data/data/com.example.mcperf/lib/arm64
```

## Architecture

- **Kotlin Layer** - Settings persistence, UI hosting, JNI calls
- **Native Layer (C++)** - Frame timing, FPS capping, resolution scaling
- **WebView UI** - HTML/CSS/JS dashboard with glassmorphic dark theme
- **DataStore** - Persistent preferences

## Tech Stack

- Kotlin 1.9.0
- C++17 with NDK
- OpenGL ES 3.0
- Jetpack DataStore
- WebView with JavaScriptInterface