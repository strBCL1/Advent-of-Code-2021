import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;


/**
 * Reads input lines form text file, calculates score of corrupted lines and score of incomplete lines.
 * Solution explained and based on Reddit answers discussion.
 */
class Day10 {
    private static long corruptedLinesScore = 0; // Part 1 result
    private static List<Long> incompleteLinesScore = new LinkedList<>(); // Its middle element is part 2 result


    /**
     * Reads all input lines from file and processes them.
     * Calculates score of illegal characters for each line. 
     * If line isn't corrupted, calculate its remaining chars score.
     */
    public void readFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader("./input.txt"))) {
            String currentLine = "";

            linesInputLoop:
            while ((currentLine = br.readLine()) != null) {
                final Map<Character, Integer> BRACKET_TO_SCORE = Map.ofEntries(
                    Map.entry(')', 3),
                    Map.entry(']', 57),
                    Map.entry('}', 1197),
                    Map.entry('>', 25137)
                );

                // Converts ArrayDeque (FIFO queue) to stack (LIFO queue).
                Queue<Character> stack = Collections.asLifoQueue(new ArrayDeque<>());

                for (char currentChar : currentLine.toCharArray()) {
                    switch (currentChar) {
                        case '(' -> stack.add(')');
                        case '[' -> stack.add(']');
                        case '{' -> stack.add('}');
                        case '<' -> stack.add('>');

                        default -> { // Current character is closing bracket ')', ']', '}' or '>'
                            Character topChar = stack.poll();

                            if (topChar == null) { // If stack is empty:
                                continue linesInputLoop; // Finish reading current line and move to next if exists
                            }

                            if (topChar != currentChar) { // If closing bracket doesn't match bracket in stack:
                                Day10.corruptedLinesScore += BRACKET_TO_SCORE.get(currentChar);
                                continue linesInputLoop; // Finish reading current line and move to next if exists
                            }
                        }
                    }
                }

                // If line isn't corrupted, calculate its remaining brackets score:
                long currentRemainingCharsScore = 0;

                for (char remainingChar : stack) {
                    currentRemainingCharsScore *= 5;
                    currentRemainingCharsScore += switch (remainingChar) {
                        case ')' -> 1;
                        case ']' -> 2;
                        case '}' -> 3;
                        default -> 4;
                    };
                }

                incompleteLinesScore.add(currentRemainingCharsScore);
            }

            
            Collections.sort(incompleteLinesScore);
            System.out.println("Part 1: total syntax error score: " + corruptedLinesScore);
            System.out.println("Part 2: middle score of incomplete lines: " + incompleteLinesScore.get(incompleteLinesScore.size() / 2));
        }
        
        catch (FileNotFoundException FNFE) {
            System.err.println("Exception: input file not found!");
        }

        catch (IOException IOE) {
            System.err.println("Exception: I/O error occurred!");
        }
    }
}


public class App {
    public static void main(String[] args) throws Exception {
        Day10 object = new Day10();
        object.readFromFile();
    }
}
