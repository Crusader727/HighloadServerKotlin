
cmake_minimum_required(VERSION 3.9)
project(server)

set(CMAKE_CXX_STANDARD 14)

find_package(Boost 1.60.0 REQUIRED system)
include_directories(include)

add_executable(server main.cpp utils/server.cpp)
