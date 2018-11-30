//
// Created by taha on 11/7/18.
//

#include "ColumnNode.h"

ColumnNode::ColumnNode(std::string n) {
    L = R = U = D = this;
    size = 0;
    name = n;
    C = this;
}
void ColumnNode::cover(ColumnNode * header, int * updates) {
    unlinkLR(updates);
    for(DancingNode* i = this->D; i != this; i = i->D ) {
        for(DancingNode* j = this->R; j != i; j = j->R ) {
           j->unlinkUD(updates);
           j->C->size--;
        }
    }
    header->size--; // from the dancing links file
}
void ColumnNode::uncover(ColumnNode * header, int * updates) {
    for(DancingNode* i = this->U; i != this; i = i->U ) {
        for(DancingNode* j = this->L; j != i; j = j->L ) {
            j->C->size++;
            j->relinkUD(updates);
        }
    }
    relinkLR(updates);
    header->size++;
}

std::string ColumnNode::getName() {
    return name;
}

bool ColumnNode::operator==(const ColumnNode &rhs) {
    return (name == rhs.name) && (size == rhs.size);
}

bool ColumnNode::operator==(const DancingNode &rhs) {
    return (name == rhs.C->name) && (size == rhs.C->size) && (L == rhs.L) && (R == rhs.R) && (U == rhs.U) && (D == rhs.D);
}

bool ColumnNode::operator!=(const ColumnNode &rhs) {
    return (name != rhs.name) || (size != rhs.size);
}

bool ColumnNode::operator!=(const DancingNode &rhs) {
    return (name != rhs.C->name) || (size != rhs.C->size) || (L != rhs.L) || (R != rhs.R) || (U != rhs.U) || (D != rhs.D);
}
