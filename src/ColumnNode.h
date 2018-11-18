//
// Created by taha on 11/7/18.
//

#ifndef PARALLELSUDOKU_COLUMNNODE_H
#define PARALLELSUDOKU_COLUMNNODE_H

#include <string>
#include "DancingNode.h"

class ColumnNode : public DancingNode {

    int size; // number of ones in current column
    std::string name;
public:
    std::string getName();
    ColumnNode(std::string N);
    void cover(ColumnNode * header, int * updates);
    void uncover(ColumnNode * header, int * updates);
    bool operator==(const ColumnNode& rhs);
    bool operator==(const DancingNode& rhs);
    bool operator!=(const ColumnNode& rhs);
    bool operator!=(const DancingNode& rhs);

};

#endif //PARALLELSUDOKU_COLUMNNODE_H
