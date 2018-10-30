//
// Created by taha on 10/29/18.
//

#ifndef PARALLELSUDOKU_ABSTRACTSUDOKUSOLVER_H
#define PARALLELSUDOKU_ABSTRACTSUDOKUSOLVER_H


#include <jmorecfg.h>
#include <iostream>
#include <string>
#include <math.h>

#define N 9
#define side 3;

class AbstractSudokuSolver {
protected:
    virtual void runSolver(int** sudoku) = 0;
    boolean validateSudoku(int** grid) {
        return true;
    }

public:
    boolean solve(int ** sudoku) {
        if(!validateSudoku(sudoku)) {
            std::cout<<"Error: Invalid sudoku"<<std::endl;
            return  false;
        }
        runSolver(sudoku);
        return true;
    }
    void static printSolution(int ** board) {
        for (int r = 0; r < N; r++){
            for (int c = 0; c < N; c++)
                printf("%2d", board[r][c]);
            printf("\n");
        }
    }


};


#endif //PARALLELSUDOKU_ABSTRACTSUDOKUSOLVER_H
