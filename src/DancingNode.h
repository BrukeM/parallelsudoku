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

//#include "ColumnNode.h"
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

    void unlinkLR(int * updates);
    void relinkLR(int * updates);
    void unlinkUD(int * updates);
    void relinkUD(int * updates);

    DancingNode();
    DancingNode(ColumnNode* c);

    operator ColumnNode() const;
};

#endif //PARALLELSUDOKU_DANCINGNODE_H
