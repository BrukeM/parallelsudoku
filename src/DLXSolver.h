//
// Created by bruke on 10/29/2018.
//

#ifndef PARALLELSUDOKU_DLXSOLVER_H
#define PARALLELSUDOKU_DLXSOLVER_H


#include "AbstractSudokuSolver.h"

class DLXSolver : public AbstractSudokuSolver {
private:
    int getIdx(int row, int col, int num);
    int ** sudokuExactCover();
    int ** makeExactCoverGrid(int ** sudoku);
public:
    void generateAllSolutions();
    void runSolver(int ** sudoku) override;
};


#endif //PARALLELSUDOKU_DLXSOLVER_H
