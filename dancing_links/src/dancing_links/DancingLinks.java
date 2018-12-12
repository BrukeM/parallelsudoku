// Author: Rafal Szymanski <me@rafal.io>
// Implementation of the Dancing Links algorithm for exact cover.
package dancing_links;

import java.io.Serializable;
import java.util.*;

public class DancingLinks {

    static final boolean verbose = true;

    private Node hookDown(Node node, Node n1) {
        assert ((node.columnColumn == n1.columnColumn) && (node.rowColumn == n1.rowColumn));
        n1.setRowDown(node.rowDown);
        n1.setColumnDown(node.columnDown);

        Node n3 = linkTable[n1.rowDown][n1.columnDown];

        n3.setRowUp(n1.row);
        n3.setColumnUp(n1.column);

        linkTable[n3.row][n3.column] = n3;

        n1.setRowUp(node.row);
        n1.setColumnUp(node.column);

        node.setRowDown(n1.row);
        node.setColumnDown(n1.column);

        linkTable[n1.row][n1.column] = n1;
        linkTable[node.row][node.column] = node;

        return n1;

    }

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

        // hooke a node n1 to the right of `this` node
        DancingNode hookRight(DancingNode n1, boolean verbose) {
            n1.R = this.R;
            System.out.println("step 1");
            n1.R.L = n1;
            System.out.println("step 2");
            n1.L = this;
            System.out.println("step 3");
            this.R = n1;
            System.out.println("step 4");
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

        public DancingNode(ColumnNode c, DancingNode orig) {
            C = c;
            L = orig.L;
            R = orig.R;
            D = orig.D;
            U = orig.U;

        }
    }

    class ColumnNode extends DancingNode implements Cloneable, Serializable {

        int size; // number of ones in current column
        String name;

        public ColumnNode(ColumnNode orig, String n) {
            //super();
            this.C = this;
            this.L = orig.L;
            this.R = orig.R;
            this.D = this;
            this.U = this;
            this.name = n;
            this.size = orig.size;
        }

        public ColumnNode(String n) {
            super();
            size = 0;
            name = n;
            C = this;
        }

        void cover(ColumnNode header) {
            unlinkLR();
            int k = 0;
            for (DancingNode i = this.D; k < this.size; i = i.D) {
                for (DancingNode j = i.R; !j.C.name.equals(i.C.name); j = j.R) {
                    System.out.println("before -- j name: " + j.C.name + ", i name: " + i.C.name);
                    j.unlinkUD();
                    j.C.size--;
//                    k--;
                    System.out.println("after -- j name: " + j.C.name + ", i name: " + i.C.name);
                }
                System.out.println("exited");
//                k++;
            }
            header.size--; // not part of original
        }

        void uncover(ColumnNode header) {
            for (DancingNode i = this.U; i != this; i = i.U) {
                for (DancingNode j = i.L; j != i; j = j.L) {
                    j.C.size++;
                    j.relinkUD();
//                    System.out.println("yo");
                }
            }
            relinkLR();
            header.size++; // not part of original
        }
    }

    private Node[][] uncover(Node c, Node[][] iLinkTable) {
        for (Node i = iLinkTable[c.getRowUp()][c.getColumnUp()]; i != c; i = iLinkTable[i.getRowUp()][i.getColumnUp()]) {
            for (Node j = iLinkTable[i.getRowLeft()][i.getColumnLeft()]; j != i; j = iLinkTable[j.getRowLeft()][j.getColumnLeft()]) {
                iLinkTable[j.rowColumn][j.columnColumn].size++;
                iLinkTable = relinkUD(j, iLinkTable);
            }

        }
        iLinkTable = relinkLR(c, iLinkTable);
        iLinkTable[0][324].size++;

        return iLinkTable;
    }

    private Node[][] cover(Node c, Node[][] iLinkTable) {
        iLinkTable = unlinkLR(c, iLinkTable);
        int k = 0;
        for (Node i = iLinkTable[c.getRowDown()][c.getColumnDown()]; i != c; i = iLinkTable[i.getRowDown()][i.getColumnDown()]) {
            for (Node j = iLinkTable[i.getRowRight()][i.getColumnRight()]; j != i; j = iLinkTable[j.getRowRight()][j.getColumnRight()]) {
                iLinkTable = unlinkUD(j, iLinkTable);
                iLinkTable[j.rowColumn][j.columnColumn].size--;
            }
        }
        iLinkTable[0][324].size--;

        return iLinkTable;
    }

    Node[][] relinkLR(Node n, Node[][] iLinkTable) {
        Node n1 = iLinkTable[n.getRowLeft()][n.getColumnLeft()];
        n1.setRowRight(n.row);
        n1.setColumnRight(n.column);
        iLinkTable[n1.row][n1.column] = n1;

        Node n2 = iLinkTable[n.getRowRight()][n.getColumnRight()];
        n2.setRowLeft(n.row);
        n2.setColumnLeft(n.column);
        iLinkTable[n2.row][n2.column] = n2;

        updates++;

        return iLinkTable;
    }

    Node[][] unlinkLR(Node n, Node[][] iLinkTable) {
        Node n1 = iLinkTable[n.getRowLeft()][n.getColumnLeft()];
        n1.setRowRight(n.getRowRight());
        n1.setColumnRight(n.getColumnRight());
        iLinkTable[n1.row][n1.column] = n1;

        Node n2 = iLinkTable[n.getRowRight()][n.getColumnRight()];
        n2.setRowLeft(n.getRowLeft());
        n2.setColumnLeft(n.getColumnLeft());
        iLinkTable[n2.row][n2.column] = n2;

        updates++;

        return iLinkTable;
    }

    Node[][] unlinkUD(Node n, Node[][] iLinkTable) {
        Node n1 = iLinkTable[n.getRowUp()][n.getColumnUp()];
        n1.setRowDown(n.getRowDown());
        n1.setColumnDown(n.getColumnDown());
        iLinkTable[n1.row][n1.column] = n1;

        Node n2 = iLinkTable[n.getRowDown()][n.getColumnDown()];
        n2.setRowUp(n.getRowUp());
        n2.setColumnUp(n.getColumnUp());
        iLinkTable[n2.row][n2.column] = n2;

        updates++;

        return iLinkTable;
    }

    Node[][] relinkUD(Node n, Node[][] iLinkTable) {
        Node n1 = iLinkTable[n.getRowUp()][n.getColumnUp()];
        n1.setRowDown(n.row);
        n1.setColumnDown(n.column);
        iLinkTable[n1.row][n1.column] = n1;

        Node n2 = iLinkTable[n.getRowDown()][n.getColumnDown()];
        n2.setRowUp(n.row);
        n2.setColumnUp(n.column);
        iLinkTable[n2.row][n2.column] = n2;

        updates++;

        return iLinkTable;
    }

    private ColumnNode header;
    private ColumnNode newHeader;
    private int solutions = 0;
    private int updates = 0;
    private SolutionHandler handler;
    private List<DancingNode> answer;
    private List<Node> answerNew;

    private Node[][] linkTable = new Node[730][325];
    private final int HEADER_COL = 324;
    private final int HEADER_ROW = 0;
    private final Node HEADER;

    // hooke a node n1 to the right of `this` node
    Node hookRight(Node n1, Node n2) {
        n2.setColumnRight(n1.getColumnRight());
        n2.setRowRight(n1.getRowRight());
        Node n3 = linkTable[n2.rowRight][n2.columnRight];
        n3.setRowLeft(n2.row);
        n3.setColumnLeft(n2.column);
        linkTable[n2.row][n3.column] = n3;

        n2.setRowLeft(n1.row);
        n2.setColumnLeft(n1.column);
        n1.setColumnRight(n2.column);
        n1.setRowRight(n2.row);

        linkTable[n2.row][n2.column] = n2;
        linkTable[n1.row][n1.column] = n1;

        return n2;
    }

    private void searchNew(Node HEADER, Node[][] iLinkTable, int k) {
        if ((HEADER.getRowRight() == HEADER.row) && (HEADER.getColumnRight() == HEADER.column)) {
            if (verbose) {
                System.out.println("-----------------------------------------");
                System.out.println("Solution #" + solutions + "\n");
            }
            handler.handleSolution(answerNew, iLinkTable);
            if (verbose) {
                System.out.println("-----------------------------------------");
            }
            solutions++;
        } else {
            Node c = selectColumnNodeHeuristic(HEADER, iLinkTable);
            iLinkTable = cover(c, iLinkTable);

            for (Node r = iLinkTable[c.getRowDown()][c.getColumnDown()]; r != c; r = iLinkTable[r.getRowDown()][r.getColumnDown()]) {
                answerNew.add(r);
                for (Node j = iLinkTable[r.getRowRight()][r.getColumnRight()]; j != r; j = iLinkTable[j.getRowRight()][j.getColumnRight()]) {
                    iLinkTable = cover(iLinkTable[j.rowColumn][j.columnColumn], iLinkTable);
                }

//                System.out.println("in");
                searchNew(iLinkTable[0][324], iLinkTable, k + 1);
//                System.out.println("out");

//                if(solutions > 0) {
//                    return;
//                }
                r = answerNew.remove(answerNew.size() - 1);
                c = iLinkTable[r.getRowColumn()][r.getColumnColumn()];

                for (Node j = iLinkTable[r.getRowLeft()][r.getColumnLeft()]; j != r; j = iLinkTable[j.getRowLeft()][j.getColumnLeft()]) {
                    iLinkTable = uncover(iLinkTable[j.rowColumn][j.columnColumn], iLinkTable);
                }
            }
            iLinkTable = uncover(c, iLinkTable);
        }
    }

    // Heart of the algorithm
    private void search(ColumnNode header, int k) {
        if (header.R == header) { // all the columns removed
            if (verbose) {
                System.out.println("-----------------------------------------");
                System.out.println("Solution #" + solutions + "\n");
            }
//            handler.handleSolution(answer);
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
        for (ColumnNode c = (ColumnNode) header.R; !c.name.equals(header.name); c = (ColumnNode) c.R) {
            if (c.size < min) {
                min = c.size;
                ret = c;
            }
//            System.out.println(c.name + ", " + header.name);
        }
        return ret;
    }

    private Node selectColumnNodeHeuristic(Node header, Node[][] iLinkTable) {
        int min = Integer.MAX_VALUE;
        Node ret = null;
        int i = 0;
//        System.out.println(iLinkTable[header.row][header.column].rowRight + " " + iLinkTable[header.row][header.column].rowRight);
        for (Node c = iLinkTable[header.getRowRight()][header.getColumnRight()]; c != header; c = iLinkTable[c.getRowRight()][c.getColumnRight()]) {
//            System.out.println(i++);
//            System.out.println(c.row + " " + c.column + " " + c.size);
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

    public ColumnNode deep_copy(ColumnNode headerNode) {
        // HARD CODE
//        final int COLS = 324;
        final int ROWS = 729;

        HashMap<DancingNode, DancingNode> nodeMap = new HashMap<>();
        nodeMap.put(headerNode, new ColumnNode(headerNode, "newHeader"));
        int s = 0;
        for (ColumnNode c = (ColumnNode) headerNode.R; !c.name.equals(headerNode.name); c = (ColumnNode) c.R) {
            s++;
//            System.out.println("Deep copy: "+ c.name);
            nodeMap.put(c, new ColumnNode(c, c.name + "prime"));
            for (DancingNode d = c.D; d != c; d = d.D) {
                nodeMap.put(d, new DancingNode((ColumnNode) nodeMap.get(c), d));
            }
        }
//        System.out.println(((ColumnNode) nodeMap.get(headerNode.R)).name);
        ColumnNode newHeaderNode = (ColumnNode) nodeMap.get(headerNode);
        ColumnNode currentColumnNewNode = newHeaderNode;
        ColumnNode previousColumnNewNode = newHeaderNode;
        DancingNode currentDancingNewNode;
        for (ColumnNode c = (ColumnNode) headerNode; !c.name.equals(newHeaderNode.name); c = (ColumnNode) c.R) {
//            System.out.println("c name: " + c.name);
//            System.out.println("in loop: " + ((ColumnNode) c.R).name);
//            System.out.println("in loop: " + ((ColumnNode) nodeMap.get(c.R)).name);
            if ("newHeader".equals(((ColumnNode) nodeMap.get(c.R)).name)) {
//                System.out.println("finished");
                System.out.println("last: " + currentColumnNewNode.name);
                System.out.println("last2: " + currentColumnNewNode.R.C.name);
                currentColumnNewNode.R = newHeaderNode;
                currentColumnNewNode.R.L = currentColumnNewNode;

                currentDancingNewNode = currentColumnNewNode.R;
                DancingNode prevDancingNewNode = currentColumnNewNode.R;

//                System.out.println(((ColumnNode) c.R).name);
                int i = 0;
//                System.out.println("size is: " + c.size);
                for (DancingNode d = c.R; i < c.size; d = d.D) {
//                    System.out.println("test");
                    if (d.D == c.R) {
                        currentDancingNewNode.D = currentColumnNewNode.R;
                        break;
                    } else {
//                    System.out.println("\td col name: " + d.C.name);
//                    System.out.println("\td val: " + d.D);
//                    System.out.println("\tin subloop: " + nodeMap.get(d.D));
                        currentDancingNewNode.D = nodeMap.get(d.D);
                        currentDancingNewNode.U = prevDancingNewNode;//nodeMap.get(d);
                        currentDancingNewNode.L = nodeMap.get(d.L);
                        currentDancingNewNode.R = nodeMap.get(d.R);

                        currentDancingNewNode.C = (ColumnNode) currentColumnNewNode.R;
                        System.out.println(nodeMap.get(d.L).C.name + " " + currentDancingNewNode.L.C.name);
//                        currentDancingNewNode.L.R = currentDancingNewNode;
//                        currentDancingNewNode.R.L = currentDancingNewNode;
                        prevDancingNewNode = currentDancingNewNode;
                        currentDancingNewNode = currentDancingNewNode.D;
                        //                    break;
                    }
                    i++;
                }
                break;
            } else {
                currentColumnNewNode.R = nodeMap.get(c.R);
                currentColumnNewNode.R.L = currentColumnNewNode; //nodeMap.get(c.R.L);
                currentDancingNewNode = currentColumnNewNode.R;
                DancingNode prevDancingNewNode = currentColumnNewNode.R;

//                System.out.println(((ColumnNode) c.R).name);
                int i = 0;
//                System.out.println("size is: " + c.size);
                for (DancingNode d = c.R; i < c.size; d = d.D) {
//                    System.out.println("test");
                    if (d.D == c.R) {
                        currentDancingNewNode.D = currentColumnNewNode.R;
                        break;
                    } else {
//                    System.out.println("\td col name: " + d.C.name);
//                    System.out.println("\td val: " + d.D);
//                    System.out.println("\tin subloop: " + nodeMap.get(d.D));
                        currentDancingNewNode.D = nodeMap.get(d.D);
                        currentDancingNewNode.U = prevDancingNewNode;//nodeMap.get(d);
                        currentDancingNewNode.L = nodeMap.get(d.L);
                        currentDancingNewNode.R = nodeMap.get(d.R);

                        currentDancingNewNode.C = (ColumnNode) currentColumnNewNode.R;
                        System.out.println(nodeMap.get(d.L.L).C.name + " " + currentDancingNewNode.L.L.C.name);
//                        currentDancingNewNode.L.R = currentDancingNewNode;
//                        currentDancingNewNode.R.L = currentDancingNewNode;
                        prevDancingNewNode = currentDancingNewNode;
                        currentDancingNewNode = currentDancingNewNode.D;
                        //                    break;
                    }
                    i++;
                }
//                currentNewNode.L = nodeMap.get(c);
                currentColumnNewNode = (ColumnNode) currentColumnNewNode.R;
//                break;
            }
        }

        currentColumnNewNode = newHeaderNode;
        boolean broken = false;

//        ColumnNode newHeaderNode = new ColumnNode(headerNode, "newHeader");
//        ArrayList<ColumnNode> newColumnNodes = new ArrayList<ColumnNode>();
        System.out.println("---------------");
        System.out.println("new");
//        System.out.println(newHeaderNode.R.D.R.C.name);
//        System.out.println(newHeaderNode.L.L.C.name);
//        System.out.println(newHeaderNode.C.name);
        System.out.println(newHeaderNode.R.C.name);
        System.out.println(newHeaderNode.R.R.L.R.L.C.name);
//        System.out.println(newHeaderNode.L.L.C.name);
//        System.out.println(newHeaderNode.L.L.L.L.L.D.D.L.L.R.R.R.D.D.R.R.C.name);
//        System.out.println(newHeaderNode.L.L.L.L.L.D.D.U.D.L.R.L);
//        System.out.println(newHeaderNode.U.C.name);
//        System.out.println(newHeaderNode.D.C.name);
//        System.out.println(newHeaderNode.size);
//        System.out.println(newHeaderNode.name);
        System.out.println(newHeaderNode.R.R.D.D.D.R.C.name);
        System.out.println("old");
//        System.out.println(headerNode.R.D.L.L.C.name);
//        System.out.println(headerNode.C.name);
        System.out.println(headerNode.R.C.name);
        System.out.println(headerNode.R.R.L.R.L.C.name);
//        System.out.println(headerNode.L.L.C.name);
//        System.out.println(headerNode.L.L.L.L.L.D.D.L.L.R.R.R.D.D.R.R.C.name);
//        System.out.println(headerNode.L.L.L.L.L.L.L.L.D.D.D.U.D.L.R.L);
//        System.out.println(headerNode.U.C.name);
//        System.out.println(headerNode.D.C.name);
//        System.out.println(headerNode.size);
//        System.out.println(headerNode.name);
        System.out.println(headerNode.R.R.D.D.D.R.C.name);
//        System.out.println("");

        newHeaderNode.size = headerNode.size;
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
        }

        headerNode = headerNode.R.C;
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
                    prev = prev.hookRight(newNode);
                    col.size++;
                }
            }
        }

        headerNode.size = COLS;
        return headerNode;
    }

    private Node makeDLXBoardNew(int[][] grid) {
        final int COLS = grid[0].length;
        final int ROWS = grid.length;

        Node headerNode = new Node();
        linkTable[0][324] = headerNode;
        for (int j = 0; j < ROWS+1; j++) {
            for (int i = 0; i < COLS; i++) {
                linkTable[j][i] = new Node(i, j);
            }
        }

        for (int i = 0; i < COLS; i++) {
            headerNode = hookRight(headerNode, linkTable[0][i]);
//            System.out.println(headerNode.rowLeft + "," + headerNode.columnLeft + " <- " + headerNode.row+ "," + headerNode.column + " -> " + headerNode.rowRight + "," + headerNode.columnRight);
        }
        headerNode = linkTable[headerNode.getRowRight()][headerNode.getColumnRight()];

        for (int i = 0; i < ROWS; i++) {
            Node prev = null;
            for (int j = 0; j < COLS; j++) {
                if (grid[i][j] == 1) {
                    Node col = linkTable[0][j];
                    Node newNode = linkTable[i+1][j];
                    if (prev == null) {
                        prev = newNode;
                    }
//                    System.out.println(col.row + " " + col.column);
                    hookDown(linkTable[col.getRowUp()][col.getColumnUp()], newNode);

//                    col.U.hookDown(newNode);
////                    System.out.println(col.U.L.C.name + "<-" + col.U.C.name + "->" + col.U.R.C.name);
//                    prev = prev.hookRight(newNode);
                    prev = hookRight(prev, newNode);
//                    System.out.println(prev.row + " " + prev.column);
                    col.size++;
                }
            }
        }
//        for (Node j = linkTable[0][323]; j != headerNode; j = linkTable[j.getRowLeft()][j.getColumnLeft()]) {
//            System.out.println(j.row + " " + j.column);
//        }
//
        headerNode.size = COLS;
        return headerNode;
    }

    private void showInfo() {
        System.out.println("Number of updates: " + updates);
    }

    // Grid consists solely of 1s and 0s. Undefined behaviour otherwise
//    public DancingLinks(int[][] grid) {
////        this(grid, new DefaultHandler());
//    }
    public DancingLinks(int[][] grid, SolutionHandler h) {
//        header = makeDLXBoard(grid);
        HEADER = makeDLXBoardNew(grid);
//        newHeader = deep_copy(header);
        handler = h;
    }

    public ColumnNode duplicateGrid(ColumnNode oldHeader) {

        return oldHeader;
    }

    public void runSolver() {
        solutions = 0;
        updates = 0;
        answerNew = new LinkedList<Node>();
//        System.out.println(HEADER.rowLeft + " " + HEADER.columnLeft);
        searchNew(HEADER, linkTable, 0);
        if (verbose) {
            showInfo();
        }

        /**
         * ================================ Make testing statements below this
         * comment.
         */
    }

}
