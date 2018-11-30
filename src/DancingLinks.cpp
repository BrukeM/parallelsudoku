//
// Created by bruke on 11/9/2018.
//

#include "DancingLinks.h"

void DancingLinks::search(int k) {
    if(header->R == header){
        if(verbose){
            cout << "-----------------------------------------" << endl;
            cout << "Solution #" << solutions << "\n" << endl;
        }
        handler.handleSolution(answer);
        if(verbose){
            cout << "-----------------------------------------" << endl;
        }
        solutions++;
    } else {
        ColumnNode c = selectColumnNodeHeuristic();
        c.cover(header, updates);
        for (DancingNode * r = c.D; c != *r; r = r->D){
            answer.push_back(*r);
            for(DancingNode * j = r->R; j != r; j = j->R){
                j->C->cover(header, updates);
            }
            search(k + 1);
            r = &(answer.back());
            answer.pop_back();
            c = *(r->C);

            for(DancingNode * j = r->L; j != r; j = j->L){
                j->C->uncover(header, updates);
            }
        }
        c.uncover(header, updates);
    }
}

ColumnNode DancingLinks::selectColumnNodeNaive() {
    return (ColumnNode) *(header->R);
//    return ColumnNode("");
}

ColumnNode DancingLinks::selectColumnNodeHeuristic() {
    int min = INT8_MAX;
    ColumnNode * ret = nullptr;
    
    return ColumnNode("");
}


