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

        // hooks node n1 `below` current node
        DancingNode hookDown(DancingNode n1) {
            assert (this.C == n1.C);
            n1.D = this.D;
            n1.D.U = n1;
            n1.U = this;
            this.D = n1;
            return n1;
        }

        // hooke a node n1 to the right of `this` node
        DancingNode hookRight(DancingNode n1) {
            n1.R = this.R;
            n1.R.L = n1;
            n1.L = this;
            this.R = n1;
            return n1;
        }

        void unlinkLR() {
            this.L.R = this.R;
            this.R.L = this.L;
            updates++;
        }

        void relinkLR() {
            this.L.R = this.R.L = this;
            updates++;
        }

        void unlinkUD() {
            this.U.D = this.D;
            this.D.U = this.U;
            updates++;
        }

        void relinkUD() {
            this.U.D = this.D.U = this;
            updates++;
        }

        public DancingNode() {
            L = R = U = D = this;
        }

        public DancingNode(ColumnNode c) {
            this();
            C = c;
        }
        
        public DancingNode(ColumnNode orig, boolean copy_flag){
            L = orig.L;
            R = orig.R;
            D = orig.D;
            U = orig.U;
        }
    }

    class ColumnNode extends DancingNode implements Cloneable, Serializable {

        int size; // number of ones in current column
        String name;
        
        public ColumnNode(ColumnNode orig, String n){
            super();
            C = orig.C;
            L = orig.L;
            R = orig.R;
            D = orig.D;
            U = orig.U;
            name = n;
            size = orig.size;
        }
        
        public ColumnNode(String n) {
            super();
            size = 0;
            name = n;
            C = this;
        }

        void cover(ColumnNode header) {
            unlinkLR();
            for (DancingNode i = this.D; i != this; i = i.D) {
                for (DancingNode j = i.R; j != i; j = j.R) {
                    j.unlinkUD();
                    j.C.size--;
                }
            }
            header.size--; // not part of original
        }

        void uncover(ColumnNode header) {
            for (DancingNode i = this.U; i != this; i = i.U) {
                for (DancingNode j = i.L; j != i; j = j.L) {
                    j.C.size++;
                    j.relinkUD();
                }
            }
            relinkLR();
            header.size++; // not part of original
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
        if (header.R == header) { // all the columns removed
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

            for (DancingNode r = c.D; r != c; r = r.D) {
                answer.add(r);

                for (DancingNode j = r.R; j != r; j = j.R) {
                    j.C.cover(header);
                }

                search(header, k + 1);

                r = answer.remove(answer.size() - 1);
                c = r.C;

                for (DancingNode j = r.L; j != r; j = j.L) {
                    j.C.uncover(header);
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
        for (ColumnNode c = (ColumnNode) header.R; c != header; c = (ColumnNode) c.R) {
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
        for (ColumnNode tmp = (ColumnNode) header.R; tmp != header; tmp = (ColumnNode) tmp.R) {

            for (DancingNode d = tmp.D; d != tmp; d = d.D) {
                String ret = "";
                ret += d.C.name + " --> ";
                for (DancingNode i = d.R; i != d; i = i.R) {
                    ret += i.C.name + " --> ";
                }
                System.out.println(ret);
            }
        }
    }
    
    public ColumnNode deep_copy(ColumnNode headerNode, int[][] grid){
        // HARD CODE
        final int COLS = 324;
        final int ROWS = 729;
        
        ColumnNode newHeaderNode = new ColumnNode(headerNode, "newHeader");
        ArrayList<ColumnNode> newColumnNodes = new ArrayList<ColumnNode>();
        
//        while(headerNode.L.C != headerNode){
            for(int i = 0; i < COLS; i++){
                ColumnNode k = new ColumnNode(headerNode.L.C, Integer.toString(400+i));
                newColumnNodes.add(k);
                newHeaderNode = (ColumnNode) newHeader.hookRight(k);
                System.out.println(newHeaderNode.L.C.name +"<-" + newHeaderNode.name + "->" + newHeaderNode.R.C.name);
            }
            newHeaderNode = newHeaderNode.R.C;
            
//            for(int i = 0; i < ROWS; i++){
//                DancingNode old = null;
//                for(int j = 0; j < COLS; j++){
//                    if(grid[i][j] == 1){
//                        ColumnNode newCol = newColumnNodes.get(j);
//                        DancingNode newCopy = new DancingNode(newCol, true);
//                        if (old == null)
//                            old = newCopy;
//                        newCol.U.hookDown(newCopy);
//                        old = old.hookRight(newCopy);
//                        newCol.size++;
//                    }
//                }
//            }
//        }
        newHeaderNode.size = COLS;        
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
//            System.out.println(headerNode.L.C.name +"<-" + headerNode.name + "->" + headerNode.R.C.name);
        }

        headerNode = headerNode.R.C;
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
                    col.U.hookDown(newNode);
//                    System.out.println(col.U.L.C.name + "<-" + col.U.C.name + "->" + col.U.R.C.name);
                    prev = prev.hookRight(newNode);
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
        newHeader = deep_copy(header, grid);
        handler = h;
    }

    public ColumnNode duplicateGrid(ColumnNode oldHeader) {
        
        return oldHeader;
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
