//
// Created by taha on 10/29/18.
//

#include "AbstractSudokuSolver.h"
boolean AbstractSudokuSolver::validateSudoku(int** grid) {
    return true;
}

boolean AbstractSudokuSolver::solve(int ** sudoku) {
    if(!validateSudoku(sudoku)) {
        std::cout<<"Error: Invalid sudoku"<<std::endl;
        return  false;
    }
    S = sudoku.length;
    side = (int)sqrt(S);
    return true;
}
