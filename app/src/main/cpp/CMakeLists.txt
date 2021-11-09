
# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.18.0)

# Declares and names the project.

project(YUV)

include_directories(${CMAKE_CURRENT_SOURCE_DIR}/libyuv/include)

SET(LIBYUV_PATH ${CMAKE_CURRENT_SOURCE_DIR}/libyuv/source)

SET(LIBYUV_SOURCE
        ${LIBYUV_PATH}/compare.cc
        ${LIBYUV_PATH}/compare_common.cc
        ${LIBYUV_PATH}/compare_neon64.cc
        ${LIBYUV_PATH}/convert.cc
        ${LIBYUV_PATH}/convert_argb.cc
        ${LIBYUV_PATH}/convert_from.cc
        ${LIBYUV_PATH}/convert_from_argb.cc
        ${LIBYUV_PATH}/convert_jpeg.cc
        ${LIBYUV_PATH}/convert_to_argb.cc
        ${LIBYUV_PATH}/convert_to_i420.cc
        ${LIBYUV_PATH}/cpu_id.cc
        ${LIBYUV_PATH}/mjpeg_decoder.cc
        ${LIBYUV_PATH}/mjpeg_validate.cc
        ${LIBYUV_PATH}/planar_functions.cc
        ${LIBYUV_PATH}/rotate.cc
        ${LIBYUV_PATH}/rotate_any.cc
        ${LIBYUV_PATH}/rotate_argb.cc
        ${LIBYUV_PATH}/rotate_common.cc
        ${LIBYUV_PATH}/rotate_neon64.cc
        ${LIBYUV_PATH}/row_any.cc
        ${LIBYUV_PATH}/row_common.cc
        ${LIBYUV_PATH}/row_neon64.cc
        ${LIBYUV_PATH}/scale.cc
        ${LIBYUV_PATH}/scale_any.cc
        ${LIBYUV_PATH}/scale_argb.cc
        ${LIBYUV_PATH}/scale_common.cc
        ${LIBYUV_PATH}/scale_neon64.cc
        ${LIBYUV_PATH}/scale_uv.cc
        ${LIBYUV_PATH}/video_common.cc)

add_library(YUV SHARED YUV.cpp ${LIBYUV_SOURCE})
target_link_libraries(YUV
        android
        m)
