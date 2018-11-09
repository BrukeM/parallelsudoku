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

    DancingNode* hookDown(DancingNode* n1);

    DancingNode* hookRight(DancingNode* n1);

    void unlinkLR();
    void relinkLR();
    void unlinkUD();
    void relinkUD();

    DancingNode();
    DancingNode(ColumnNode* c);
};

#endif //PARALLELSUDOKU_DANCINGNODE_H
