import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

class Day9 {
    private final int GRID_HEIGHT = 100;
    private final int GRID_WIDTH = 100;
    private GridType[][] grid = new GridType[GRID_HEIGHT][GRID_WIDTH];
    private int totalSum = 0;
    private List<Integer> list = new ArrayList<>();
    // private TreeMap<Integer, Integer> treemap = new TreeMap<>();


    public void readFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader("/home/mertens/VSCode Workspace/Java Language/Advent-of-Code-2021/Day 9/src/input.txt"))) {
            String currentLine = "";
            int lineCounter = 0;

            while ((currentLine = br.readLine()) != null) {
                for (int i = 0; i < currentLine.length(); ++i) {
                    grid[lineCounter][i] = new GridType(currentLine.charAt(i), lineCounter, i);
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
                char currentCellValue = grid[y][x].value;

                if (currentCellValue == '9') {
                    continue;
                }

                int neighboursAmount = 0;
                List<Character> valuesToCompareTo = new ArrayList<>();

                if (isInBounds(y - 1, x) && grid[y - 1][x].value > currentCellValue) { // Prev line
                    valuesToCompareTo.add(grid[y - 1][x].value);
                }

                if (isInBounds(y + 1, x) && grid[y + 1][x].value > currentCellValue) { // Next line
                    valuesToCompareTo.add(grid[y + 1][x].value);
                }

                if (isInBounds(y, x - 1) && grid[y][x - 1].value > currentCellValue) { // Prev col
                    valuesToCompareTo.add(grid[y][x - 1].value);
                } 

                if (isInBounds(y, x + 1) && grid[y][x + 1].value > currentCellValue) { // Next col
                    valuesToCompareTo.add(grid[y][x + 1].value);
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
                        totalSum += 1 + currentCellValue - '0'; // Part 1
                        getBasinsSize(y, x);
                    }
                    else if (valuesToCompareTo.get(i) <= currentCellValue) {
                        break;
                    }
                }
            }
        }

        System.out.println(totalSum);

        Collections.sort(list);
        // list.forEach(e -> System.out.print(e + " "));

        int sum = 1;

        for (int i = list.size() - 1; i > list.size() - 4; --i) {
            sum *= list.get(i);
        }

        // treemap.entrySet().forEach(entry -> System.out.println(entry.getKey() + " " + entry.getValue()));
        System.out.println(sum);
    }


    private boolean isInBounds(int y, int x) {
        return y >= 0 && y < GRID_HEIGHT && x >= 0 && x < GRID_WIDTH;
    }


    private void getBasinsSize(int y, int x) {
        int currentSize = 0;

        Queue<GridType> q = new LinkedList<>();
        q.add(grid[y][x]);

        while (!q.isEmpty()) {
            GridType currentCell = q.peek();
            q.remove();

            x = currentCell.x;
            y = currentCell.y;
            currentSize++;

            currentCell.isChecked = true;

            int dRow[] = { 0, 1, 0, -1 };
            int dCol[] = { -1, 0, 1, 0 };

            for (int i = 0; i < 4; ++i) {
                int adjx = x + dRow[i];
                int adjy = y + dCol[i];

                if (!isInBounds(adjy, adjx)) {
                    continue;
                }

                if (!grid[adjy][adjx].isChecked && grid[adjy][adjx].value != '9' && grid[adjy][adjx].value > currentCell.value) {
                    q.add(grid[adjy][adjx]);
                    grid[adjy][adjx].isChecked = true;
                }
            }
        }
        
        // treemap.put(currentSize, treemap.getOrDefault(currentSize, 0) + 1);
        list.add(currentSize);
    }
}

public class App {
    public static void main(String[] args) throws Exception {
        Day9 object = new Day9();
        object.readFromFile();
    }
}
