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
private:
    typedef DancingNode super;
public:
    ColumnNode(std::string N);
    void cover();
    void uncover();

};

#endif //PARALLELSUDOKU_COLUMNNODE_H
