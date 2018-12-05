package dancing_links;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author taha
 */
public class Node {
    int columnUp, rowUp, columnDown, rowDown, columnLeft, rowLeft, columnRight, rowRight, columnColumn, rowColumn, size ;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getcolumnUp() {
        return columnUp;
    }

    public void setcolumnUp(int columnUp) {
        this.columnUp = columnUp;
    }

    public int getrowUp() {
        return rowUp;
    }

    public void setrowUp(int rowUp) {
        this.rowUp = rowUp;
    }

    public int getcolumnDown() {
        return columnDown;
    }

    public void setcolumnDown(int columnDown) {
        this.columnDown = columnDown;
    }

    public int getrowDown() {
        return rowDown;
    }

    public void setrowDown(int rowDown) {
        this.rowDown = rowDown;
    }

    public int getcolumnLeft() {
        return columnLeft;
    }

    public void setcolumnLeft(int columnLeft) {
        this.columnLeft = columnLeft;
    }

    public int getrowLeft() {
        return rowLeft;
    }

    public void setrowLeft(int rowLeft) {
        this.rowLeft = rowLeft;
    }

    public int getcolumnRight() {
        return columnRight;
    }

    public void setcolumnRight(int columnRight) {
        this.columnRight = columnRight;
    }

    public int getrowRight() {
        return rowRight;
    }

    public void setrowRight(int rowRight) {
        this.rowRight = rowRight;
    }

    public int getcolumnColumn() {
        return columnColumn;
    }

    public void setcolumnColumn(int columnColumn) {
        this.columnColumn = columnColumn;
    }

    public int getrowColumn() {
        return rowColumn;
    }

    public void setrowColumn(int rowColumn) {
        this.rowColumn = rowColumn;
    }
    Node hookDown(Node n1) {
        return new Node();
    }
    Node hookRight(Node n1) {
        return new Node();
    }
    void unlinkLR() {
        
    }
    void relinkLR() {
        
    }
    void unlinkUD() {
        
    }
    void relinkUD() {
    }
    public Node() {
        
    }
    void cover() {
        
    }
    void uncover() {
    }
    
    
}
