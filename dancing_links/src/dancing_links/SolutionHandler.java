package dancing_links;

import dancing_links.DancingLinks.*;
import java.util.*;
import dancing_links.SudokuDLX.*;

public interface SolutionHandler {
//    void handleSolution(List<DancingNode> solution);

    void handleSolution(List<Node> solution, Node[][] iLinkTable);
}

class SudokuHandler implements SolutionHandler {

    int size = 9;

//    public void handleSolution(List<DancingNode> answer){
//        int[][] result = parseBoard(answer);
//        AbstractSudokuSolver.printSolution(result);
//    }
    public void handleSolution(List<Node> answer, Node[][] iLinkTable) {
        int[][] result = parseBoard(answer, iLinkTable);
        AbstractSudokuSolver.printSolution(result);
    }

    private int[][] parseBoard(List<DancingNode> answer) {
        int[][] result = new int[size][size];
        for (DancingNode n : answer) {
            DancingNode rcNode = n;
            int min = Integer.parseInt(rcNode.C.name);
            for (DancingNode tmp = n.R; tmp != n; tmp = tmp.R) {
                int val = Integer.parseInt(tmp.C.name);
                if (val < min) {
                    min = val;
                    rcNode = tmp;
                }
            }
            int ans1 = Integer.parseInt(rcNode.C.name);
            int ans2 = Integer.parseInt(rcNode.R.C.name);
            int r = ans1 / size;
            int c = ans1 % size;
            int num = (ans2 % size) + 1;
            result[r][c] = num;
        }
        return result;
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
//                System.out.println(tmp.row + " " + tmp.column);
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

//class DefaultHandler implements SolutionHandler{
//    public void handleSolution(List<DancingNode> answer){
//        for(DancingNode n : answer){
//            String ret = "";
//            ret += n.C.name + " ";
//            DancingNode tmp = n.R;
//            while (tmp != n){
//                ret += tmp.C.name + " ";
//                tmp = tmp.R;
//            }
//            System.out.println(ret);
//        }
//    }
//}
