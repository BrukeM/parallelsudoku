//
// Created by bruke on 10/29/2018.
//

#include "DLXSolver.h"
//#include "DancingLinks.h"
//#include "SudokuHandler.h"

int DLXSolver::getIdx(int row, int col, int num) {
    return ((row - 1) * N * N + (col - 1) * N + (num - 1));
}

int ** DLXSolver::sudokuExactCover() {
    int ** R = new int*[9*9*9];
    for (int i = 0; i < (9*9*9); ++i){
        R[i] = new int[(9*9*4)];
    }
    int hBase = 0;

    // row-column constraints
    for(int r = 1; r <= N; r++){
        for(int c = 1; c <= N; c++, hBase++){
            for(int n = 1; n <= N; n++){
                R[getIdx(r, c, n)][hBase] = 1;
            }
        }
    }

    // row-number constraints
    for(int r = 1; r <= N; r++){
        for(int n = 1; n <= N; n++, hBase++){
            for(int c1 = 1; c1 <= N; c1++){
                R[getIdx(r, c1, n)][hBase] = 1;
            }
        }
    }

    // column-number constraints
    for(int c = 1; c <= N; c++){
        for(int n =1; n <= N; n++, hBase++){
            for(int r1 = 1; r1 <= N; r1++){
                R[getIdx(r1, c, n)][hBase] = 1;
            }
        }
    }

    //box-number constraints
    for(int br = 1; br <= N; br += 3){
        for(int bc = 1; bc <= N; bc += 3){
            for(int n = 1; n <= N; n++, hBase++){
                for(int rDelta = 0; rDelta < 3; rDelta++){
                    for(int cDelta = 0; cDelta < 3; cDelta++){
                        R[getIdx((br + rDelta), (bc + cDelta), n)][hBase] = 1;
                    }
                }
            }
        }
    }

    return R;
}

int ** DLXSolver::makeExactCoverGrid(int ** sudoku) {
    int ** R = sudokuExactCover();
    for(int i = 1; i <= N; i++){
        for(int j = 1; j <= N; j++){
            int n = sudoku[i - 1][j - 1];
            if (n != 0){
                for(int num = 1; num <= N; num++){
                    if (num != n){
                        int index = getIdx(i, j, num);
                        std::fill(R[index], R[index] + sizeof(R[index])/sizeof(int), 0);
                    }
                }
            }
        }
    }
    return R;
}


void DLXSolver::generateAllSolutions() {
    int ** cover = sudokuExactCover();
    //DancingLinks dlx = new DancingLinks(cover, new SudokuHandler(N));
    //dlx.runSolver();
}

void DLXSolver::runSolver(int** sudoku) {
    int ** cover = makeExactCoverGrid(sudoku);
    //DancingLinks dlx = new DancingLinks(cover, new SudokuHandler(N));
    //dlx.runSolver();
}
