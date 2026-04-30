cmake_minimum_required(VERSION 4.1.2)
project("MyApplication")
set(CMAKE_CXX_STANDARD 23)
set(CMAKE_CXX_STANDARD_REQUIRED ON)
add_subdirectory(includes)
add_library(MainActivity SHARED
    MainActivity.cpp
)


target_link_libraries(MainActivity PRIVATE oak log)