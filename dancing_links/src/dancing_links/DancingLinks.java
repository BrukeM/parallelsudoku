// Author: Rafal Szymanski <me@rafal.io>
// Implementation of the Dancing Links algorithm for exact cover.
package dancing_links;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;

public class DancingLinks {

    static final boolean verbose = true;

    class DancingNode {
        DancingNode L, R, U, D;
        ColumnNode C;
        
        public DancingNode(){
            L = R = D = U = this;
        }
        
        public DancingNode(ColumnNode c){
            this();
            this.C = c;
        }
        
        public DancingNode(DancingNode orig){
            this.setLeft(orig.getLeft());
            this.setRight(orig.getRight());
            this.setDown(orig.getDown());
            this.setUp(orig.getUp());
            this.C = orig.getColumnNode();
        }
        
        // Set the links
        public void setLeft(DancingNode L){
            this.L = L;
        }
        
        public void setRight(DancingNode R){
            this.R = R;
        }
        
        public void setDown(DancingNode D){
            this.D = D;
        }
        
        public void setUp(DancingNode U){
            this.U = U;
        }
        
        // Get the links
        public DancingNode getLeft(){
            return this.L;
        }
        
        public DancingNode getRight(){
            return this.R;
        }
        
        public DancingNode getDown(){
            return this.D;
        }
        
        public DancingNode getUp(){
            return this.U;
        }
        
        public ColumnNode getColumnNode(){
            return this.C;
        }
        
        // Hooks node n1 `below` current node
        DancingNode hookDown(DancingNode n1){
            assert(this.getColumnNode() == n1.getColumnNode());
            n1.setDown(this.D);
            n1.getDown().setUp(n1);
            n1.setUp(this);
            this.setDown(n1);            
            return n1;
        }
        
        // Hooks a node n1 to the right of `this` node
        DancingNode hookRight(DancingNode n1){
            n1.setRight(this.R);
            n1.getRight().setLeft(n1);
            n1.setLeft(this);
            this.setRight(n1);
            return n1;
        }
        
        void unlinkLR(){
            this.getLeft().setRight(this.R);
            this.getRight().setLeft(this.L);
            updates++;
        }
        
        void relinkLR(){
            this.getLeft().setRight(this);
            this.getRight().setLeft(this);
            updates++;
        }
        
        void unlinkUD(){
            this.getUp().setDown(this.D);
            this.getDown().setUp(this.U);
            updates++;
        }
        
        void relinkUD(){
            this.getUp().setDown(this);
            this.getDown().setUp(this);
            updates++;
        }
    }
    
    class ColumnNode extends DancingNode {
        
        int size; // number of ones in current column
        String name;
        
        public ColumnNode(String n){
            super();
            size = 0;
            name = n;
            C = this;
        }
        
        // Copy constructor
        public ColumnNode(ColumnNode orig){
            super((DancingNode) orig);
            size = orig.size;
            name = orig.name + "_a";
            C = this;
        }
        
        void cover(ColumnNode header){
            unlinkLR();
            for (DancingNode i = this.getDown(); i != this; i = i.getDown()){
                for (DancingNode j = i.getRight(); j != i; j = j.getRight()){
                    j.unlinkUD();
                    j.getColumnNode().size--;
                }
            }
            header.size--;
        }
        
        void uncover(ColumnNode header){
            for (DancingNode i = this.getUp(); i != this; i = i.getUp()){
                for (DancingNode j = i.getLeft(); j != i; j = j.getLeft()){
                    j.getColumnNode().size++;
                    j.relinkUD();
                }
            }
            relinkLR();
            header.size++;
        }
    }
    
    private ColumnNode header;
    private ColumnNode newHeader;
    private int solutions = 0;
    private int updates = 0;
    private SolutionHandler handler;
    private List<DancingNode> answer;

    // Heart of the algorithm
    private void search(ColumnNode header, int k) {
        if (header.getRight() == header) { // all the columns removed
            if (verbose) {
                System.out.println("-----------------------------------------");
                System.out.println("Solution #" + solutions + "\n");
            }
            handler.handleSolution(answer);
            if (verbose) {
                System.out.println("-----------------------------------------");
            }
            solutions++;
        } else {
            ColumnNode c = selectColumnNodeHeuristic(header);
            c.cover(header);

            for (DancingNode r = c.getDown(); r != c; r = r.getDown()) {
                answer.add(r);

                for (DancingNode j = r.getRight(); j != r; j = j.getRight()) {
                    j.getColumnNode().cover(header);
                }

                search(header, k + 1);

                r = answer.remove(answer.size() - 1);
                c = r.getColumnNode();

                for (DancingNode j = r.getLeft(); j != r; j = j.getLeft()) {
                    j.getColumnNode().uncover(header);
                }
            }
            c.uncover(header);
        }
    }

    private ColumnNode selectColumnNodeNaive() {
        return (ColumnNode) header.R;
    }

    private ColumnNode selectColumnNodeHeuristic(ColumnNode header) {
        int min = Integer.MAX_VALUE;
        ColumnNode ret = null;
        for (ColumnNode c = (ColumnNode) header.getRight(); c != header; c = (ColumnNode) c.getRight()) {
            if (c.size < min) {
                min = c.size;
                ret = c;
            }
        }
        return ret;
    }

    // Ha, another Knuth algorithm
    private ColumnNode selectColumnNodeRandom() { // select a column randomly
        ColumnNode ptr = (ColumnNode) header.R;
        ColumnNode ret = null;
        int c = 1;
        while (ptr != header) {
            if (Math.random() <= 1 / (double) c) {
                ret = ptr;
            }
            c++;
            ptr = (ColumnNode) ptr.R;
        }
        return ret;
    }

    private ColumnNode selectColumnNodeNth(int n) {
        int go = n % header.size;
        ColumnNode ret = (ColumnNode) header.R;
        for (int i = 0; i < go; i++) {
            ret = (ColumnNode) ret.R;
        }
        return ret;
    }

    private void printBoard() { // diagnostics to have a look at the board state
        System.out.println("Board Config: ");
        for (ColumnNode tmp = (ColumnNode) header.getRight(); tmp != header; tmp = (ColumnNode) tmp.getRight()) {

            for (DancingNode d = tmp.getDown(); d != tmp; d = d.getDown()) {
                String ret = "";
                ret += d.getColumnNode().name + " --> ";
                for (DancingNode i = d.getRight(); i != d; i = i.getRight()) {
                    ret += i.getColumnNode().name + " --> ";
                }
                System.out.println(ret);
            }
        }
    }
    
    private ColumnNode deep_copy(ColumnNode oldHeaderNode){
        // HARD CODE:
        final int ROWS = 729;
        final int COLS = 324;
        
//        System.out.println(oldHeaderNode.name + "\n");
        // Check if there is anything at all in the old header node
        if (oldHeaderNode.getLeft().getColumnNode() == oldHeaderNode.getColumnNode())
            return null;
        
        ColumnNode newHeaderNode = new ColumnNode(oldHeaderNode);
        System.out.println(newHeaderNode.name);
        
        ArrayList<ColumnNode> copy_of_ColumnNodes = new ArrayList<ColumnNode>();
        
        oldHeaderNode = oldHeaderNode.getRight().getColumnNode();
        
        while((ColumnNode) oldHeaderNode.getRight() != oldHeaderNode){
            ColumnNode new_col = new ColumnNode(oldHeaderNode);
            copy_of_ColumnNodes.add(new_col);
            newHeaderNode = (ColumnNode) newHeaderNode.hookRight(new_col);
            oldHeaderNode = (ColumnNode) oldHeaderNode.getRight();
            System.out.println(newHeaderNode.getLeft().getColumnNode().name + "<-" + newHeaderNode.name + "->" + newHeaderNode.getRight().getColumnNode().name);
        }
        
        
        return newHeaderNode;
    }

    // grid is a grid of 0s and 1s to solve the exact cover for
    // returns the root column header node
    private ColumnNode makeDLXBoard(int[][] grid) {
        final int COLS = grid[0].length;
        final int ROWS = grid.length;

        ColumnNode headerNode = new ColumnNode("header");
        ArrayList<ColumnNode> columnNodes = new ArrayList<ColumnNode>();

        for (int i = 0; i < COLS; i++) {
            ColumnNode n = new ColumnNode(Integer.toString(i));
            columnNodes.add(n);
            headerNode = (ColumnNode) headerNode.hookRight(n);
            System.out.println(headerNode.getLeft().getColumnNode().name +"<-" + headerNode.name + "->" + headerNode.getRight().getColumnNode().name);
        }
        System.out.println("\n==============================================================================================\n");

        headerNode = headerNode.getRight().getColumnNode();
//        System.out.println(headerNode.D.C.name);

        for (int i = 0; i < ROWS; i++) {
            DancingNode prev = null;
            for (int j = 0; j < COLS; j++) {
                if (grid[i][j] == 1) {
                    ColumnNode col = columnNodes.get(j);
                    DancingNode newNode = new DancingNode(col);
                    if (prev == null) {
                        prev = newNode;
                    }
                    col.getUp().hookDown(newNode);
//                    System.out.println(col.getUp().getLeft().getColumnNode().name + "<-" + col.getUp().getColumnNode().name + "->" + col.getUp().getRight().getColumnNode().name);
                    prev = prev.hookRight(newNode);
//                    System.out.println(prev.L.C.name +"<-" + prev.C.name + "->" + prev.R.C.name);
//                    System.out.println(prev.L.C.name);
                    col.size++;
                }
            }
        }
        
        headerNode.size = COLS;

        return headerNode;
    }
    
    

    private void showInfo() {
        System.out.println("Number of updates: " + updates);
    }

    // Grid consists solely of 1s and 0s. Undefined behaviour otherwise
    public DancingLinks(int[][] grid) {
        this(grid, new DefaultHandler());
    }

    public DancingLinks(int[][] grid, SolutionHandler h) {
        header = makeDLXBoard(grid);
        newHeader = deep_copy(header);
        handler = h;
    }

    public void runSolver() {
        solutions = 0;
        updates = 0;
        answer = new LinkedList<DancingNode>();
//        search(header, 0);        
//        if(verbose) showInfo();

/**
 * ================================ Make testing statements below this comment.
 */


    }

}
