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
public class Node implements Cloneable {
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
        this.columnLeft = column;
        this.rowLeft = row;
        this.columnRight = column;
        this.rowRight = row;
        this.columnUp = column;
        this.rowUp = row;
        this.columnDown = column;
        this.rowDown = row;
    }
    
    public Node(int i) {
        this(i, 0);
        size = 0;
        
    }
    
//    Header initializer only
    public Node() {
        this.size = 0;
        this.columnColumn = 324;
        this.rowColumn = 0;
        this.column = 324;
        this.row = 0;
        this.columnUp = 324;
        this.rowUp = 0;
        this.columnDown = 324;
        this.rowDown = 0;
        this.columnLeft = 324;
        this.rowLeft = 0;
        this.columnRight = 324;
        this.rowRight = 0;
    }
    
    @Override
    public Node clone() {
        Node cloneObj = new Node(this.column, this.row);
        cloneObj.setColumnColumn(columnColumn);
        cloneObj.setRowColumn(rowColumn);
        cloneObj.setColumnDown(columnDown);
        cloneObj.setRowDown(rowDown);
        cloneObj.setColumnLeft(columnLeft);
        cloneObj.setRowLeft(rowLeft);
        cloneObj.setColumnRight(columnRight);
        cloneObj.setRowRight(rowRight);
        cloneObj.setColumnUp(columnUp);
        cloneObj.setRowUp(rowUp);
        cloneObj.size = size;
        return cloneObj;
    }
    
}
