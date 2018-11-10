//
// Created by Praveer on 10/30/18.
//

#ifndef PARALLELSUDOKU_DANCINGLINKS_H
#define PARALLELSUDOKU_DANCINGLINKS_H

#include <iostream>
#include <string>
#include <cmath>
#include <vector>
#include <cassert>

#include "DancingNode.h"
#include "ColumnNode.h"
#include "SudokuHandler.h"

using namespace std;

class DancingLinks{

private:
    ColumnNode * header;
    int * solutions = 0;
    int * updates = 0;
    SudokuHandler handler;
    std::list<DancingNode> answer = {};

    void search(int k);
    ColumnNode selectColumnNodeNaive();
    ColumnNode selectColumnNodeHeuristic()
    ColumnNode selectColumnNodeRandom();
    ColumnNode selectColumnNodeNth(int n);
    void printBoard();
    ColumnNode makeDLXBoard(int ** grid);
    void showInfo();


protected:
    bool verbose = true;

public:
    DancingLinks(int ** grid);
    DancingLinks(int ** grid, SudokuHandler h);
    void runSolver();

};





#endif //PARALLELSUDOKU_DANCINGLINKS_H
