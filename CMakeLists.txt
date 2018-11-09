cmake_minimum_required(VERSION 3.12)
project(parallelsudoku)

set(CMAKE_CXX_STANDARD 17)

add_executable(parallelsudoku src/main.cpp src/DLXSolver.cpp src/DLXSolver.h src/AbstractSudokuSolver.h src/SudokuHandler.cpp src/SudokuHandler.h src/DancingNode.h src/DancingNode.cpp)