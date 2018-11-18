//
// Created by bruke on 11/7/2018.
//

#include "DancingLinks.h"

#ifndef PARALLELSUDOKU_SOLUTIONHANDLER_H
#define PARALLELSUDOKU_SOLUTIONHANDLER_H

#define N 9
#include <list>

class SudokuHandler {
private:

    int ** parseBoard(std::list<DancingNode> answer);

public:
    int size = N;

    void handleSolution(std::list<DancingNode> answer);

    SudokuHandler(){

    }

    SudokuHandler(int boardSize){
        size = boardSize;
    }
};


#endif //PARALLELSUDOKU_SOLUTIONHANDLER_H
