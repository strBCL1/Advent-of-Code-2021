import java.io.BufferedReader;
import java.io.FileReader;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Queue;

/*
    Для текущего шага:
        Для всех элементов матрицы:
            Если текущий элемент посещен:
                Игнорировать его
            
            Добавить к нему 1
            Если текущий элемент больше 9:
                Добавить его в очередь
        
        
        обработайОчередь()



    обработайОчередь():
        Пока очередь не пустая:
            Взять первый элемент из очереди и удалить его из нее (далее "текущий элемент")
            Если текущий элемент посещен:
                Продолжить итерацию
            
            Пометить текущий элемент как посещенного
            Текущий элемент = 0
            Добавить к количеству морганий 1

            Для всех соседей текущего элемента:
                Если сосед посещен:
                    Продолжить итерацию

                Добавить к соседу 1
                Если он больше 9:
                    Добавить его в очередь
*/

class Day11 {
    private static final int LINES_AMOUNT = 10, COLUMNS_AMOUNT = 10;
    private static int[][] grid = new int[LINES_AMOUNT][COLUMNS_AMOUNT];
    private static int stepsAmount = 100;
    private static int flashesAmount = 0; // Part 1 answer


    public void readFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader("/home/mertens/VSCode Workspace/Java Language/Advent-of-Code-2021/Day 11/input.txt"))) {
            String currentLine = "";
            int rowCounter = 0;

            while ((currentLine = br.readLine()) != null) {
                for (int colCounter = 0; colCounter < currentLine.length(); ++colCounter) {
                    grid[rowCounter][colCounter] = currentLine.charAt(colCounter) - '0';
                }

                rowCounter++;
            }


            processGrid();
            // System.out.println(Arrays.deepToString(grid));
            System.out.println(flashesAmount);
        }

        catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void processGrid() {
        while (stepsAmount-- > 0) {
            Queue<Entry<Integer, Integer>> q = new LinkedList<>();
            boolean[][] isVisitedCell = new boolean[LINES_AMOUNT][COLUMNS_AMOUNT];

            for (int y = 0; y < LINES_AMOUNT; ++y) {
                for (int x = 0; x < COLUMNS_AMOUNT; ++x) {
                    if (isVisitedCell[y][x]) {
                        continue;
                    }

                    grid[y][x] += 1;

                    if (grid[y][x] > 9) { // Add all '9' and higher to queue
                        q.add(new AbstractMap.SimpleEntry<Integer, Integer> (y, x));
                    }
                }
            }

            processQueue(q, isVisitedCell);
        }
    }


    private void processQueue(Queue<Entry<Integer, Integer>> q, boolean[][] isVisitedCell) {
        while (!q.isEmpty()) {
            Entry<Integer, Integer> headElementCoords = q.poll();
            int curElementY = headElementCoords.getKey();
            int curElementX = headElementCoords.getValue();

            if (isVisitedCell[curElementY][curElementX]) {
                continue;
            }

            isVisitedCell[curElementY][curElementX] = true;
            grid[curElementY][curElementX] = 0;
            flashesAmount += 1; // Part 1 answer

            int[] dRow = {-1, 0, 1};
            int[] dCol = {-1, 0, 1};

            for (int i = 0; i < 3; ++i) {
                for (int j = 0; j < 3; ++j) {
                    int adjY = curElementY + dRow[i];
                    int adjX = curElementX + dCol[j];

                    // if (adjY == curElementY && adjX == curElementX) { // Ignore self-adding
                    //     continue;
                    // }
                    
                    if (!isInBounds(adjY, adjX)) {
                        continue;
                    }

                    if (isVisitedCell[adjY][adjX]) {
                        continue;
                    }
                    
                    grid[adjY][adjX] += 1;

                    if (grid[adjY][adjX] > 9) {
                        q.add(new AbstractMap.SimpleEntry<Integer, Integer> (adjY, adjX));
                    }
                }
            }
        }
    }


    private boolean isInBounds(int y, int x) {
        return y >= 0 && y < LINES_AMOUNT && x >= 0 && x < COLUMNS_AMOUNT;
    }
}

public class App {
    public static void main (String[] args) {
        Day11 object = new Day11();
        object.readFromFile();
    }
}