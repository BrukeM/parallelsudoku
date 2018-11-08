//
// Created by Praveer on 11/7/18.
//

#ifndef PARALLELSUDOKU_DANCINGNODE_H
#define PARALLELSUDOKU_DANCINGNODE_H

#include <iostream>
#include <string>
#include <cmath>
#include <vector>
#include <cassert>

class ColumnNode;

class DancingNode{
public:
    DancingNode* L;
    DancingNode* R;
    DancingNode* U;
    DancingNode* D;
    ColumnNode* C;

    DancingNode* hookDown(DancingNode* n1){
        assert(this->C == n1->C);
        n1->D = this->D;
        n1->D->U = n1;
        n1->U = this;
        this->D = n1;
        return n1;
    }

    DancingNode* hookRight(DancingNode* n1){
        n1->R = this->R;
        n1->R->L = n1;
        n1->L = this;
        this->R = n1;
        return n1;
    }

    void unlinkLR(){
        this->L->R = this->R;
        this->R->L = this->L;
        updates++;
    }

    void relinkLR(){
        this->L->R = this->R->L = this;
        updates++;
    }

    void unlinkUD(){
        this->U->D = this->D;
        this->D->U = this->U;
        updates++;
    }

    void relinkUD(){
        this->U->D = this->D->U = this;
        updates++;
    }

    DancingNode (){
        L = R = U = D = this;
    }

    DancingNode (ColumnNode* c){
        this();
        C = c;
    }
};

#endif //PARALLELSUDOKU_DANCINGNODE_H
