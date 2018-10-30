//
// Created by bruke on 10/29/2018.
//

#ifndef PARALLELSUDOKU_DLXSOLVER_H
#define PARALLELSUDOKU_DLXSOLVER_H


#include "AbstractSudokuSolver.h"

class DLXSolver : public AbstractSudokuSolver {
public:
    void runSolver(int** sudoku);

};


#endif //PARALLELSUDOKU_DLXSOLVER_H
