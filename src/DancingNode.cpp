//
// Created by Praveer on 11/7/18.
//

#include <iostream>
#include <string>
#include <cmath>
#include <vector>
#include <cassert>
#include "DancingNode.h"

using namespace std;

DancingNode::DancingNode() {
    L = R = U = D = this;
}

DancingNode::DancingNode(ColumnNode *c) {
    L = R = U = D = this;
    C = c;
}

DancingNode* DancingNode::hookDown(DancingNode* n1){
    assert(this->C == n1->C);
    n1->D = this->D;
    n1->D->U = n1;
    n1->U = this;
    this->D = n1;
    return n1;
}

DancingNode* DancingNode::hookRight(DancingNode* n1){
    n1->R = this->R;
    n1->R->L = n1;
    n1->L = this;
    this->R = n1;
    return n1;
}

void DancingNode::unlinkLR(int * updates){
    this->L->R = this->R;
    this->R->L = this->L;
    updates++;
}

void DancingNode::relinkLR(int * updates){
    this->L->R = this->R->L = this;
    updates++;
}

void DancingNode::unlinkUD(int * updates){
    this->U->D = this->D;
    this->D->U = this->U;
    updates++;
}

void DancingNode::relinkUD(int * updates){
    this->U->D = this->D->U = this;
    updates++;
}
