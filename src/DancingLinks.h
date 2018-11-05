//
// Created by Praveer on 10/30/18.
//

#ifndef PARALLELSUDOKU_DANCINGLINKS_H
#define PARALLELSUDOKU_DANCINGLINKS_H

#include <iostream>
#include <jmorecfg.h>
#include <string>
#include <cmath>
#include <vector>
#include <cassert>

using namespace std;

class DancingLinks{

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

        explicit DancingNode (ColumnNode c){
            DancingNode();
            C = c;
        }

    };
// Class "ColumnNode" inherits from "DancingNode"
    class ColumnNode: public DancingNode{
    private:
        int size;       // number of ones in current column
        string name;
    public:
        explicit ColumnNode(const string &n){
            DancingNode ();
            size=0;
            name=n;
            C = this;
        }

        void cover(){
            unlinkLR();

        }
    };
public:
    DancingLinks();

};





#endif //PARALLELSUDOKU_DANCINGLINKS_H
