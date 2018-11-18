//
// Created by bruke on 11/7/2018.
//

#include "SudokuHandler.h"
#include "AbstractSudokuSolver.h"

#define N 9


int ** SudokuHandler::parseBoard(std::list<DancingNode> answer) {
    int ** result = new int*[N];
    for (int i = 0; i < N; i++) {
        result[i] = new int[N];
    }
    for (auto n : answer){
        DancingNode rcNode = n;
        int min = std::stoi(rcNode.C->getName());
        for(DancingNode * tmp = n.R; tmp != &n; tmp = tmp->R){
            int val = std::stoi(tmp->C->getName());
            if (val < min) {
                min = val;
                rcNode = *tmp;
            }
        }
        int ans1 = std::stoi(rcNode.C->getName());
        int ans2 = std::stoi(rcNode.R->C->getName());
        int r = ans1 / size;
        int c = ans1 % size;
        int num = (ans2 % size) + 1;
        result[r][c] = num;
    }
    return result;
}

void SudokuHandler::handleSolution(std::list<DancingNode> answer) {
    int ** result = parseBoard(answer);
    AbstractSudokuSolver::printSolution(result);
}
