cmake_minimum_required(VERSION 3.22.1)

project(perf_layer LANGUAGES CXX)

add_library(perf_layer SHARED
    ../../cpp/perf_layer.cpp
    ../../cpp/jni_bridge.cpp
)

find_library(log-lib log)

target_include_directories(perf_layer PRIVATE
    ../../cpp
)

target_link_libraries(perf_layer
    ${log-lib}
    GLESv3
)

target_compile_options(perf_layer PRIVATE
    -std=c++17
    -Wall
    -fvisibility=hidden
)