import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

class Solution {
    public void readFromFile() throws IOException {
        BufferedReader bf = new BufferedReader(new FileReader("/home/mertens/VSCode Workspace/Java Language/Advent-of-Code-2021/Day 6/Part 1/input.txt"));
        String currentLine = "";
        
        while ((currentLine = bf.readLine()) != null) {
            
        }
    }
}


public class App {
    public static void main(String[] args) throws Exception {
        Solution object = new Solution();
        try {
            object.readFromFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}