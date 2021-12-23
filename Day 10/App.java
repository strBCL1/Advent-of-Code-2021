import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Stack;

class Day10 {
    private int totalChunksSum = 0;


    public void readFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader("./input.txt"))) {
            String currentLine = "";

            while ((currentLine = br.readLine()) != null) {
                processLine(currentLine);
            }

            System.out.println(totalChunksSum);
        }

        catch (FileNotFoundException FNFE) {
            System.err.println("File not found!");
        }

        catch (IOException IOE) {
            System.err.println("I/O error occurred!");
        }
    }


    private void processLine(String currentLine) {
        Stack<Character> stack = new Stack<>();

        for (char currentLineChunk : currentLine.toCharArray()) {
            if (currentLineChunk == '(' || currentLineChunk == '[' || 
                currentLineChunk == '{' || currentLineChunk == '<') {
                    stack.add(currentLineChunk);
            }

            else {
                char currentStackChunk = stack.pop();

                if ((currentStackChunk == '(' && currentLineChunk != ')') ||
                    (currentStackChunk == '[' && currentLineChunk != ']') ||
                    (currentStackChunk == '{' && currentLineChunk != '}') ||
                    (currentStackChunk == '<' && currentLineChunk != '>')) {
                        totalChunksSum += getChunksScore(currentLineChunk);
                        return ;
                }
            }
            
        }
    }


    private int getChunksScore(char currentLineChunk) {
        switch (currentLineChunk) {
            case ')': 
                return 3;

            case ']':
                return 57;
            
            case '}':
                return 1197;

            case '>':
                return 25137;

            default: 
                return 0;
        }
    }
}


public class App {
    public static void main(String[] args) throws Exception {
        Day10 object = new Day10();
        object.readFromFile();
    }
}
