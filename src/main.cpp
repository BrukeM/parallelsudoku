//
// Created by bruke on 10/29/2018.
//

#include <iostream>
#include <vector>
#include <string>

#include "DLXSolver.h"


using namespace std;

int ** fromString(string s);

void printBoard(int ** board);

void runExample();

int main() {
    runExample();
    return 0;
}

int ** fromString(string s){
    //std::cout << s << std::endl;
    int ** board = new int*[N];
    for (int r = 0; r < N; r++){
        board[r] = new int[N];
        //std::cout << "row: " << r << std::endl;
        for (int c = 0; c < N; c++){
            //std::cout << "col: " << r << std::endl;
            int index = c + (N * r);
            //std::cout << "index: " << index << std::endl;
            char ch = s[index];
            //std::cout << "char: " << ch << std::endl;
            if(ch != '.'){
                board[r][c] = ch - '0';
            } else {
                board[r][c] = 0;
            }
        }
    }
    return board;
}


void runExample(){
    vector<long> timings;

    int ** board = fromString(const_cast<char *>("..5....3....9...8....57.....9.7....3.7.13..5.3.2......2...8......1..94259....78.."));
    //printBoard(board);

    DLXSolver* solver = new DLXSolver();
    solver->printSolution(board);
//    solver->solve(board);



//    for (auto i : timings){
//        cout << i << endl;
//    }
}