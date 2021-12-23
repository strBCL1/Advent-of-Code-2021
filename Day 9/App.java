import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Solves day 9 part 1 and part 2 puzzle.
 * Reads input from text files, creates 2D grid, finds lowest points' sum and basins' sizes.
 * Grid's height and width are provided manually.
 */
class Day9 {
    private final int GRID_HEIGHT = 100;
    private final int GRID_WIDTH = 100;
    private GridType[][] grid = new GridType[GRID_HEIGHT][GRID_WIDTH];
    private int lowestPointsSum = 0;
    private List<Integer> basinsSizes = new ArrayList<>();


    /**
     * Reads all lines of grid and places it in 2D array of custom type.
     */
    public void readFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader("./input.txt"))) {
            String currentLine = "";
            int lineCounter = 0;

            while ((currentLine = br.readLine()) != null) {
                for (int i = 0; i < currentLine.length(); ++i) { // Initialize each cell of grid
                    grid[lineCounter][i] = new GridType(currentLine.charAt(i), lineCounter, i);
                }

                lineCounter++;
            }

            findLowestPoints();
        } 
        
        catch (FileNotFoundException FNFE) {
            System.err.println("No input file found!");
        }

        catch (IOException IOE) {
            System.err.println("I/O error occurred!");
        }
    }


    /**
     * Calculates sum of lowest points.
     * Traverses over adjacent cells, get their values and finds lowest points.
     */
    private void findLowestPoints() {
        for (int y = 0; y < GRID_HEIGHT; ++y) {
            for (int x = 0; x < GRID_WIDTH; ++x) {
                char currentCellValue = grid[y][x].value;

                if (currentCellValue == '9') { // Ignore highest points (of height '9')
                    continue;
                }
                
                //https://www.geeksforgeeks.org/depth-first-traversal-dfs-on-a-2d-array/ - used to iterate over adjacent cells of each cell
                // Get all values of adjacent cells
                List<Character> adjacentCellsValues = new ArrayList<>();
                int dRow[] = { 0, 1, 0, -1 };
                int dCol[] = { -1, 0, 1, 0 };

                for (int i = 0; i < 4; ++i) {
                    int adjY = y + dRow[i];
                    int adjX = x + dCol[i];
                    
                    //Avoid IndexOutOfBoundsException
                    if (isInBounds(adjY, adjX) && grid[adjY][adjX].value > currentCellValue) {
                        adjacentCellsValues.add(grid[adjY][adjX].value);
                    }
                }


                int neighboursAmount = 0;

                // Current cell is in middle of grid - it has 4 adjacent cells
                if (x > 0 && x < GRID_WIDTH - 1 && y > 0 && y < GRID_HEIGHT - 1) { 
                    neighboursAmount = 4;
                }

                // Current cell isn't a corner cell and
                // it is in first column or last column or
                // it is in the first row or in last row - it has 3 adjacent cells
                else if ((y > 0 && y < GRID_HEIGHT - 1 && (x == 0 || x == GRID_WIDTH - 1)) || 
                         (x > 0 && x < GRID_WIDTH - 1 && (y == 0 || y == GRID_HEIGHT - 1))) { 
                    neighboursAmount = 3;
                }
                
                // Current cell is the corner cell - it has only 2 adjacent cells
                else { 
                    neighboursAmount = 2;
                }

                // If amount of neighbours of current cell isn't appropriate - ignore this case
                if (adjacentCellsValues.size() != neighboursAmount) {
                    continue;
                }

                for (int i = 0; i < adjacentCellsValues.size(); ++i) {
                    // Current cell's value must be smaller than its adjacent cells' values
                    if (adjacentCellsValues.get(i) <= currentCellValue) {
                        break;
                    }

                    else if (i == adjacentCellsValues.size() - 1) {
                        lowestPointsSum += (currentCellValue - '0') + 1; // Part 1
                        calculateCurrentBasinsSize(y, x); // Part 2
                    }
                }
            }
        }

        
        Collections.sort(basinsSizes);
        int sumOfBasinsSizes = 1;
        
        // Multiplication of 3 largest basins' sizes
        for (int i = basinsSizes.size() - 3; i < basinsSizes.size(); ++i) {
            sumOfBasinsSizes *= basinsSizes.get(i);
        }
        

        System.out.println("Part 1 - sum of all lowest points: " + lowestPointsSum);
        System.out.println("Part 2 - multiplication of 3 largest basins: " + sumOfBasinsSizes);
    }


    /**
     * Checks if given 'x' and 'y' are not out of bounds of grid.
     * @param currentY 'y' coordinate of cell being checked
     * @param currentX 'x' coordinate of cell being checked
     * @return true if indices aren't out of bounds, false otherwise
     */
    private boolean isInBounds(int currentY, int currentX) {
        return currentY >= 0 && currentY < GRID_HEIGHT && 
               currentX >= 0 && currentX < GRID_WIDTH;
    }


    /**
     * Calculates size of each basin using BFS traversal.
     * Check https://www.geeksforgeeks.org/breadth-first-traversal-bfs-on-a-2d-array/ for more information
     * @param currentY starting 'y' coordinate of current lowest point
     * @param currentX starting 'x' coordinate of current lowest point
     */
    private void calculateCurrentBasinsSize(int currentY, int currentX) {
        int currentBasinSize = 0;

        Queue<GridType> q = new LinkedList<>();
        q.add(grid[currentY][currentX]);

        while (!q.isEmpty()) {
            GridType currentCell = q.peek();
            q.remove();

            currentBasinSize++;

            currentX = currentCell.x;
            currentY = currentCell.y;

            int dRow[] = { 0, 1, 0, -1 };
            int dCol[] = { -1, 0, 1, 0 };

            for (int i = 0; i < 4; ++i) {
                int adjX = currentX + dRow[i];
                int adjY = currentY + dCol[i];

                if (!isInBounds(adjY, adjX)) {
                    continue;
                }

                if (!grid[adjY][adjX].isChecked && grid[adjY][adjX].value != '9' && grid[adjY][adjX].value > currentCell.value) {
                    q.add(grid[adjY][adjX]);
                    grid[adjY][adjX].isChecked = true;
                }
            }
        }
        
        basinsSizes.add(currentBasinSize);
    }
}

public class App {
    public static void main(String[] args) throws Exception {
        Day9 object = new Day9();
        object.readFromFile();
    }
}
