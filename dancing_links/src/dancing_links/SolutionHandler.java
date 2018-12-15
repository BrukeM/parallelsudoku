package dancing_links;

import java.util.*;

public interface SolutionHandler {
    void handleSolution(List<Node> solution, Node[][] iLinkTable);
}

class SudokuHandler implements SolutionHandler {

    int size = 9;
    
    @Override
    public void handleSolution(List<Node> answer, Node[][] iLinkTable) {
        int[][] result = parseBoard(answer, iLinkTable);
        AbstractSudokuSolver.printSolution(result);
    }

    public SudokuHandler(int boardSize) {
        size = boardSize;
    }

    private int[][] parseBoard(List<Node> answer, Node[][] iLinkTable) {
        int[][] result = new int[size][size];
        for (Node n : answer) {
            Node rcNode = n;
            int min = rcNode.column;
            for (Node tmp = iLinkTable[n.getRowRight()][n.getColumnRight()]; tmp != n; tmp = iLinkTable[tmp.getRowRight()][tmp.getColumnRight()]) {
                int val = tmp.column;
                if (val < min) {
                    min = val;
                    rcNode = tmp;
                }
            }
            int ans1 = rcNode.column;
            int ans2 = iLinkTable[rcNode.getRowRight()][rcNode.getColumnRight()].column;
            int r = ans1 / size;
            int c = ans1 % size;
            int num = (ans2 % size) + 1;
            result[r][c] = num;
        }
        return result;
    }
}