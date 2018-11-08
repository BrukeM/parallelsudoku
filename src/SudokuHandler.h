//
// Created by bruke on 11/7/2018.
//

#ifndef PARALLELSUDOKU_SOLUTIONHANDLER_H
#define PARALLELSUDOKU_SOLUTIONHANDLER_H

#define N 9

#include <list>
#include "DancingLinks.h"

class SudokuHandler {
private:

    int ** parseBoard(std::list<DancingLinks::DancingNode> answer);

public:
    int size = N;

    void handleSolution(std::list<DancingLinks::DancingNode> answer);

    SudokuHandler(int boardSize){
        size = boardSize;
    }
};


#endif //PARALLELSUDOKU_SOLUTIONHANDLER_H