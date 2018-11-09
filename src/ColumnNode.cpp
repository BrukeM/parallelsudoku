//
// Created by taha on 11/7/18.
//

#include "ColumnNode.h"

ColumnNode::ColumnNode(std::string n) {
    DancingNode();
    size = 0;
    name = n;
    C = this;
}
void ColumnNode::cover() {
    unlinkLR();
    for(DancingNode* i = this->D; i != this; i = i->D ) {
        for(DancingNode* j = this->R; j != i; j = j->R ) {
           j->unlinkUD();
           j->C->size--;
        }
    }
    header.size-- // from the dancing links file
}
void ColumnNode::uncover() {
    for(DancingNode* i = this->U; i != this; i = i->U ) {
        for(DancingNode* j = this->L; j != i; j = j->L ) {
            j->C->size++;
            j->relinkUD();
        }
    }
    relinkLR();
    header.size++;
}