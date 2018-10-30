cmake_minimum_required(VERSION 3.12)
project(parallelsudoku)

set(CMAKE_CXX_STANDARD 17)

add_executable(parallelsudoku main.cpp DLXSolver.cpp DLXSolver.h)