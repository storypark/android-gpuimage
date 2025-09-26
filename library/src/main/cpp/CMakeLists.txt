# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.
cmake_minimum_required(VERSION 3.4.1)

# Project declaration
project(gpuimage-library)

# Enable 16KB page size support - comprehensive approach
set(CMAKE_ANDROID_ARM64_PAGE_SIZE 16384)

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
add_library( # Sets the name of the library.
        gpuimage-library

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        yuv-decoder.c)

# Add comprehensive 16KB page size linker options for proper alignment
target_link_options(gpuimage-library PRIVATE
    "-Wl,-z,max-page-size=16384"
    "-Wl,-z,common-page-size=16384"
)

# Ensure proper section alignment for 16KB pages
target_compile_options(gpuimage-library PRIVATE
    "-ffunction-sections"
    "-fdata-sections"
)

# Searches for a specified prebuilt library and stores the path as a variable.
find_library( # Sets the name of the path variable.
        log-lib
        # Specifies the name of the NDK library that you want CMake to locate.
        log)

# Specifies libraries CMake should link to your target library.
target_link_libraries( # Specifies the target library.
        gpuimage-library
        # Links the target library to the log library included in the NDK.
        ${log-lib}
        GLESv2
        jnigraphics)