package dancing_links;

import java.io.BufferedReader;
import java.io.FileReader;

import java.util.*;

public class Examples{
    
    private static int[][] fromString(String s){
        int[][] board = new int[9][9];
        for(int i = 0; i < 81; i++){
            char c = s.charAt(i);
            int row = i / 9;
            int col = i % 9;
            if(c != '.'){
                board[row][col] = c-'0';
            }
        }
        return board;
    }
    
    
    private static void runExample(){
        
        String[] diffs = {"simple.txt","easy.txt","intermediate.txt","expert.txt"};
        
        
        BufferedReader reader;
        String text;
        for(String diff : diffs){
            
            String filename = "boards/" + diff;
            
            List<Long> timings = new ArrayList<>();
            
            try{
                reader = new BufferedReader(new FileReader(filename));
                
                while ((text = reader.readLine()) != null) {
                    
                    int[][] sudoku = fromString(text);
                    
                    SudokuDLX solver = new SudokuDLX();
                    
                    long milis = System.nanoTime();
                    
                    solver.solve(sudoku);
                    
                    long elapsed = System.nanoTime() - milis;
                    
                    timings.add(elapsed);                   
                }
                
            } catch (Exception e){
                e.printStackTrace();
            }
            
            System.out.println("STATS: " + diff + "\n");
            printStats(timings);
            break;
        }   
    }
    
    private static void printStats(List<Long> timings){
        long min = timings.get(0);
        long max = timings.get(0);
        
        long sum = 0;
        long sqsum = 0;
        
        for(long ll : timings){
            min = Math.min(min,ll);
            max = Math.max(max,ll);
            sum += ll; 
        }
        
        double avg = sum / timings.size();
        
        for(long ll : timings){
            sqsum += (ll-avg)*(ll-avg);
        }
        
        double std = Math.sqrt(sqsum/timings.size());
        
        
        System.out.println("min: " + min*1e-6);
        System.out.println("max: " + max*1e-6);
        System.out.println("avg: " + avg*1e-6);
        System.out.println("std: " + std*1e-6);
    }

    public static void main(String[] args){
        runExample();    
    }

}
