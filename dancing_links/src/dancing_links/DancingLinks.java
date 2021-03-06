package dancing_links;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DancingLinks extends Thread {

    static final boolean verbose = true;
    private int solutions = 0;
    private int updates = 0;
    private final SolutionHandler handler;
    private List<Node> answerNew;

    private Node[][] linkTable = new Node[730][325];
    private final Node HEADER;
    private final int knum;

    @Override
    public void run() {
        searchNew(HEADER, linkTable, answerNew, knum);
    }

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

    private void searchNew(Node HEADER, Node[][] iLinkTable, List<Node> answer, int k) {
        if ((HEADER.getRowRight() == HEADER.row) && (HEADER.getColumnRight() == HEADER.column)) {
            if (verbose) {
                System.out.println("-----------------------------------------");
                System.out.println("Solution #" + solutions + "\n");
            }
            handler.handleSolution(answer, iLinkTable);

            if (verbose) {
                System.out.println("-----------------------------------------");
            }
            solutions++;
        } else {
            Node c = selectColumnNodeHeuristic(HEADER, iLinkTable);
            iLinkTable = cover(c, iLinkTable);

            Vector threads = new Vector();
            for (Node r = iLinkTable[c.getRowDown()][c.getColumnDown()]; r != c; r = iLinkTable[r.getRowDown()][r.getColumnDown()]) {
                answerNew.add(r);
                
                if (k < 5) { // for sequential, set condition to k < 0
                    Node[][] cloneLinkTable = cloneArray(iLinkTable);

                    ArrayList<Node> cloneAnswer = new ArrayList<>();
                    for (Node n : answerNew) {
                        cloneAnswer.add(cloneLinkTable[n.row][n.column]);
                        r = cloneLinkTable[n.row][n.column];
                    }

                    for (Node j = cloneLinkTable[r.getRowRight()][r.getColumnRight()]; j != r; j = cloneLinkTable[j.getRowRight()][j.getColumnRight()]) {
                        cloneLinkTable = cover(cloneLinkTable[j.rowColumn][j.columnColumn], cloneLinkTable);
                    }
                    Thread t = new Thread(new DancingLinks(cloneLinkTable[0][324], cloneLinkTable, cloneAnswer, handler, k + 1));
                    threads.add(t);
                    t.start();

                } else {
                    for (Node j = iLinkTable[r.getRowRight()][r.getColumnRight()]; j != r; j = iLinkTable[j.getRowRight()][j.getColumnRight()]) {
                        iLinkTable = cover(iLinkTable[j.rowColumn][j.columnColumn], iLinkTable);
                    }
                    searchNew(iLinkTable[0][324], iLinkTable, answerNew, k + 1);
                }

                r = answerNew.remove(answerNew.size() - 1);

                for (Node j = iLinkTable[r.getRowLeft()][r.getColumnLeft()]; j != r; j = iLinkTable[j.getRowLeft()][j.getColumnLeft()]) {
                    iLinkTable = uncover(iLinkTable[j.rowColumn][j.columnColumn], iLinkTable);
                }
                c = iLinkTable[r.getRowColumn()][r.getColumnColumn()];
            }
            for (int index = 0; index < threads.size(); index++) {
                Thread t = (Thread) threads.get(index);
                try {
                    t.join();
                } catch (InterruptedException ex) {
                    Logger.getLogger(DancingLinks.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public static Node[][] cloneArray(Node[][] list) {
        Node[][] clone = new Node[730][325];
        for (int i = 0; i < 730; i++) {
            for (int j = 0; j < 324; j++) {
                clone[i][j] = list[i][j].clone();
            }
        }
        clone[0][324] = list[0][324].clone();
        return clone;
    }

    private Node selectColumnNodeHeuristic(Node header, Node[][] iLinkTable) {
        int min = Integer.MAX_VALUE;
        Node ret = null;
        for (Node c = iLinkTable[header.getRowRight()][header.getColumnRight()]; c != header; c = iLinkTable[c.getRowRight()][c.getColumnRight()]) {
            if (c.size < min) {
                min = c.size;
                ret = c;
            }
        }
        return ret;
    }

    private Node makeDLXBoardNew(int[][] grid) {
        final int COLS = grid[0].length;
        final int ROWS = grid.length;

        Node headerNode = new Node();
        linkTable[0][324] = headerNode;
        for (int j = 0; j < ROWS + 1; j++) {
            for (int i = 0; i < COLS; i++) {
                linkTable[j][i] = new Node(i, j);
            }
        }

        for (int i = 0; i < COLS; i++) {
            headerNode = hookRight(headerNode, linkTable[0][i]);
        }
        headerNode = linkTable[headerNode.getRowRight()][headerNode.getColumnRight()];

        for (int i = 0; i < ROWS; i++) {
            Node prev = null;
            for (int j = 0; j < COLS; j++) {
                if (grid[i][j] == 1) {
                    Node col = linkTable[0][j];
                    Node newNode = linkTable[i + 1][j];
                    if (prev == null) {
                        prev = newNode;
                    }
                    hookDown(linkTable[col.getRowUp()][col.getColumnUp()], newNode);

                    prev = hookRight(prev, newNode);
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

    public DancingLinks(int[][] grid, SolutionHandler h) {
        HEADER = makeDLXBoardNew(grid);
        handler = h;
        knum = 0;
    }

    public DancingLinks(Node head, Node[][] jLinkTable, List<Node> answer, SolutionHandler h, int k) {
        HEADER = head;
        linkTable = jLinkTable;
        knum = k;
        answerNew = answer;
        handler = h;
    }

    public void runSolver() {
        solutions = 0;
        updates = 0;
        answerNew = new LinkedList<>();
        searchNew(HEADER, linkTable, answerNew, 0);
        if (verbose) {
            showInfo();
        }
    }
}
