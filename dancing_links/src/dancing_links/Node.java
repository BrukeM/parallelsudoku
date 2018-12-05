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
    int columnUp, rowUp, columnDown, rowDown, columnLeft, rowLeft, columnRight, rowRight, columnColumn, rowColumn, size, row, column;

    public int getColumnUp() {
        return columnUp;
    }

    public void setColumnUp(int columnUp) {
        this.columnUp = columnUp;
    }

    public int getRowUp() {
        return rowUp;
    }

    public void setRowUp(int rowUp) {
        this.rowUp = rowUp;
    }

    public int getColumnDown() {
        return columnDown;
    }

    public void setColumnDown(int columnDown) {
        this.columnDown = columnDown;
    }

    public int getRowDown() {
        return rowDown;
    }

    public void setRowDown(int rowDown) {
        this.rowDown = rowDown;
    }

    public int getColumnLeft() {
        return columnLeft;
    }

    public void setColumnLeft(int columnLeft) {
        this.columnLeft = columnLeft;
    }

    public int getRowLeft() {
        return rowLeft;
    }

    public void setRowLeft(int rowLeft) {
        this.rowLeft = rowLeft;
    }

    public int getColumnRight() {
        return columnRight;
    }

    public void setColumnRight(int columnRight) {
        this.columnRight = columnRight;
    }

    public int getRowRight() {
        return rowRight;
    }

    public void setRowRight(int rowRight) {
        this.rowRight = rowRight;
    }

    public int getColumnColumn() {
        return columnColumn;
    }

    public void setColumnColumn(int columnColumn) {
        this.columnColumn = columnColumn;
    }

    public int getRowColumn() {
        return rowColumn;
    }

    public void setRowColumn(int rowColumn) {
        this.rowColumn = rowColumn;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public Node(int column, int row){
        this.columnColumn = column;
        this.rowColumn = 0;
        this.column = column;
        this.row = row;
    }
    
    public Node(int i) {
        size = 0;
        this.columnColumn = i;
        this.rowColumn = 0;
        this.column = i;
        this.row = 0;
    }
    public Node() {
        size = 0;
    }    
    
}
