//
// Created by taha on 10/29/18.
//

#ifndef PARALLELSUDOKU_ABSTRACTSUDOKUSOLVER_H
#define PARALLELSUDOKU_ABSTRACTSUDOKUSOLVER_H


#include <jmorecfg.h>
#include <iostream>
#include <string>
#include <math.h>

class AbstractSudokuSolver {
protected:
    int S = 9;
    int side = 3;
    virtual void runSolver(int** sudoku) = 0;
    static boolean validateSudoku(int** grid);

public:
    boolean solve(int ** sudoku);


};


#endif //PARALLELSUDOKU_ABSTRACTSUDOKUSOLVER_H
