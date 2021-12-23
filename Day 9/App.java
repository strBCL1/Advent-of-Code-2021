import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

class Day9 {
    private final int GRID_HEIGHT = 100;
    private final int GRID_WIDTH = 100;
    private char[][] grid = new char[GRID_HEIGHT][GRID_WIDTH];
    private int totalSum = 0;

    public void readFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader("/home/mertens/VSCode Workspace/Java Language/Advent-of-Code-2021/Day 9/src/input.txt"))) {
            String currentLine = "";
            int lineCounter = 0;

            while ((currentLine = br.readLine()) != null) {
                for (int i = 0; i < currentLine.length(); ++i) {
                    grid[lineCounter][i] = currentLine.charAt(i);
                }

                lineCounter++;
            }

            sumMinCells();
        } 
        
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void sumMinCells() {
        for (int y = 0; y < GRID_HEIGHT; ++y) {
            for (int x = 0; x < GRID_WIDTH; ++x) {
                char currentCellValue = grid[y][x];

                if (currentCellValue == '9') {
                    continue;
                }

                int neighboursAmount = 0;
                List<Character> valuesToCompareTo = new ArrayList<>();

                if (isInBounds(y - 1, x) && grid[y - 1][x] > currentCellValue) { // Prev line
                    valuesToCompareTo.add(grid[y - 1][x]);
                }

                if (isInBounds(y + 1, x) && grid[y + 1][x] > currentCellValue) { // Next line
                    valuesToCompareTo.add(grid[y + 1][x]);
                }

                if (isInBounds(y, x - 1) && grid[y][x - 1] > currentCellValue) { // Prev col
                    valuesToCompareTo.add(grid[y][x - 1]);
                } 

                if (isInBounds(y, x + 1) && grid[y][x + 1] > currentCellValue) { // Next col
                    valuesToCompareTo.add(grid[y][x + 1]);
                }


                if (x > 0 && x < GRID_WIDTH - 1 && y > 0 && y < GRID_HEIGHT - 1) { // Value is in middle of grid - amount of its checked neighbours is 4
                    neighboursAmount = 4;
                }

                else if ((y > 0 && y < GRID_HEIGHT - 1 && (x == 0 || x == GRID_WIDTH - 1)) || 
                         (x > 0 && x < GRID_WIDTH - 1 && (y == 0 || y == GRID_HEIGHT - 1))) { // Value is on the top or on the bottom line - ... is 3
                    neighboursAmount = 3;
                }

                else { // Value is in the corner
                    neighboursAmount = 2;
                }

                if (valuesToCompareTo.size() != neighboursAmount) { // Ignore not appropriate neighbours amount
                    continue;
                }

                for (int i = 0; i < valuesToCompareTo.size(); ++i) {
                    if (i == valuesToCompareTo.size() - 1) {
                        totalSum += 1 + currentCellValue - '0';
                    }
                    else if (valuesToCompareTo.get(i) <= currentCellValue) {
                        break;
                    }
                }
            }
        }

        System.out.println(totalSum);
    }


    private boolean isInBounds(int y, int x) {
        return y >= 0 && y < GRID_HEIGHT && x >= 0 && x < GRID_WIDTH;
    }
}

public class App {
    public static void main(String[] args) throws Exception {
        Day9 object = new Day9();
        object.readFromFile();
    }
}
