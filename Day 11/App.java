import java.io.BufferedReader;
import java.io.FileReader;
import java.util.AbstractMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Queue;

/*
    Algorithm in english:
    For each step:
        For each grid element (octopus; octopus can only flash at most once per step):
            If current element is visited (if this octopus has flashed):
                Discard him

            Add 1 to octopus
            If octopus > 9:
                Add his coords to queue

        processQueue()

    
    processQueue():
        While queue isn't empty:
            Get head octopus and remove him from queue

            If octopus is visited:
                Discard him 

            Mark octopus as visited
            Octopus = 0
            Add 1 to amount of flashes

            For all octopus's neighbours:
                If neighbour is visited:
                    Discard the neighbour

                Add 1 to neighbour
                If neighbour > 9:
                    Add his coords to queue

    ==========================================================================

    Алгоритм на русском:
    Для текущего шага:
        Для всех элементов матрицы:
            Если текущий элемент посещен:
                Продолжить проверку матрицы
            
            Добавить к нему 1
            Если текущий элемент больше 9:
                Добавить его в очередь
        
        
        обработайОчередь()



    обработайОчередь():
        Пока очередь не пустая:
            Взять первый элемент из очереди и удалить его из нее (далее "текущий элемент")
            Если текущий элемент посещен:
                Продолжить итерацию очереди
            
            Пометить текущий элемент как посещенного
            Текущий элемент = 0
            Добавить к количеству морганий 1

            Для всех соседей текущего элемента:
                Если сосед посещен:
                    Продолжить итерацию соседей

                Добавить к соседу 1
                Если он больше 9:
                    Добавить его в очередь
*/


/**
 * Solves day 11 puzzle of Advent of Code 2021.
 * Link to the puzzle: https://adventofcode.com/2021/day/11
 * Solution contains explanations. Check line 8 for algorithm in english or russian.
 */
class Day11 {
    private static final int LINES_AMOUNT = 10, COLUMNS_AMOUNT = 10;
    private static int[][] grid = new int[LINES_AMOUNT][COLUMNS_AMOUNT];
    private static int currentStep = 0;
    private static int amountOfFlashes = 0;


    /**
     * Reads lines from input text file and initializes grid of octopuses.
     */
    public void readFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader("./input.txt"))) {
            String currentLine = "";
            int rowCounter = 0;

            while ((currentLine = br.readLine()) != null) {
                for (int colCounter = 0; colCounter < currentLine.length(); ++colCounter) {
                    grid[rowCounter][colCounter] = currentLine.charAt(colCounter) - '0';
                }

                rowCounter++;
            }


            processGrid();
        }

        catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * Processes grid of octopuses until they all shine simultaneously.
     */
    private void processGrid() {
        while (!allAreFlashing()) {
            Queue<Entry<Integer, Integer>> q = new LinkedList<>();
            boolean[][] isVisitedCell = new boolean[LINES_AMOUNT][COLUMNS_AMOUNT];

            for (int y = 0; y < LINES_AMOUNT; ++y) {
                for (int x = 0; x < COLUMNS_AMOUNT; ++x) {
                    if (isVisitedCell[y][x]) { // Ignore visited octopuses
                        continue;
                    }

                    grid[y][x] += 1;

                    // Add all '9' and higher elements' coords to queue
                    if (grid[y][x] > 9) {
                        // https://stackoverflow.com/questions/6121246/list-of-entries-how-to-add-a-new-entry?lq=1
                        q.add(new AbstractMap.SimpleEntry<Integer, Integer> (y, x));
                    }
                }
            }

            // Process current queue containing octupuses > 9
            processQueue(q, isVisitedCell); 

            currentStep++;

            if (currentStep == 100) { // Print part 1 answer
                System.out.println("Amount of flashes after step 100: " + amountOfFlashes);
            }
        }

        System.out.println("The step all octopuses flash: " + currentStep); // Print part 2 answer
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
            amountOfFlashes += 1; // Part 1 answer

            int[] dRow = {-1, 0, 1};
            int[] dCol = {-1, 0, 1};

            for (int i = 0; i < 3; ++i) {
                for (int j = 0; j < 3; ++j) {
                    int adjY = curElementY + dRow[i];
                    int adjX = curElementX + dCol[j];
                    
                    if (!isInBounds(adjY, adjX)) { // Ignore out of bounds coords
                        continue;
                    }

                    if (isVisitedCell[adjY][adjX]) { // Ignore visited octopuses
                        continue;
                    }
                    
                    grid[adjY][adjX] += 1;

                    if (grid[adjY][adjX] > 9) {
                        // https://stackoverflow.com/questions/6121246/list-of-entries-how-to-add-a-new-entry?lq=1
                        q.add(new AbstractMap.SimpleEntry<Integer, Integer> (adjY, adjX));
                    }
                }
            }
        }
    }


    /**
     * Checks if all octopuses in grid shine simultaneously
     * @return true if all octopuses in grid shine simultaneously. Else false. 
     */
    private boolean allAreFlashing() {
        for (int y = 0; y < LINES_AMOUNT; ++y) {
            for (int x = 0; x < COLUMNS_AMOUNT; ++x) {
                if (grid[y][x] != 0) {
                    return false; 
                }
            }
        }

        return true;
    }


    /**
     * Checks if coords of octopus being checked isn't out of bounds.
     * @param y row of current element
     * @param x column of current element
     * @return true if coords of current element are within the grid's bounds. Else false. 
     */
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