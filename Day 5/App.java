import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Contains methods to read input from text file, calculate rows and columns overlappings,
 * count calculated cells and print the whole grid
 */
class Solution {
    //Store pairs of starting coordinates
    private LinkedList<Entry<Integer, Integer>> startCoords = new LinkedList<>();

    //Store pairs of finishing coordinates
    private LinkedList<Entry<Integer, Integer>> finishCoords = new LinkedList<>();

    //Values of grid's borders
    int gridsBorderX = 0, gridsBorderY = 0;


    /**
     * Proceeds input from text file as coordinates, pushes pairs to linked lists and updates grid's borders
     * @throws IOException may be caused by BufferedReader.readLine() method if an I/O error occurs
     */
    public void readFromFile() throws IOException {
        BufferedReader bf = new BufferedReader(new FileReader("/path/to/input file")); //Specify input file
        String currentLine = "";

        while ((currentLine = bf.readLine()) != null) {
            Matcher matcher = Pattern.compile("\\d+").matcher(currentLine);

            int[] currentRowCoords = new int[4]; //Array to store current line's coordinates (x1, y1) and (x2, y2)
            int curRowsCoordIndex = 0;

            while (matcher.find()) {
                currentRowCoords[curRowsCoordIndex++] = Integer.parseInt(matcher.group());
            }

            //Create pairs of coordinates
            startCoords.add(Map.entry(currentRowCoords[0], currentRowCoords[1]));
            finishCoords.add(Map.entry(currentRowCoords[2], currentRowCoords[3]));

            //Update grid's borders
            gridsBorderX = Math.max(Math.max(gridsBorderX, currentRowCoords[0]), currentRowCoords[2]);
            gridsBorderY = Math.max(Math.max(gridsBorderY, currentRowCoords[1]), currentRowCoords[3]);
        }

        bf.close();

        processGrid();
    }

    
    /**
     * Initializes grid and modifies it by adding numbers to it based on coordinates from linked lists
     */
    private void processGrid() {
        //Declare and initialize grid
        char[][] grid = new char[gridsBorderY + 1][gridsBorderX + 1];
        for (int i = 0; i < grid.length; ++i) {
            for (int j = 0; j < grid[0].length; ++j) {
                grid[i][j] = '.';
            }
        }


        for (int currentCoordsIndex = 0; currentCoordsIndex < startCoords.size(); ++currentCoordsIndex) {
            int currentX = startCoords.get(currentCoordsIndex).getKey();
            int currentY = startCoords.get(currentCoordsIndex).getValue();

            int finishX = finishCoords.get(currentCoordsIndex).getKey();
            int finishY = finishCoords.get(currentCoordsIndex).getValue();


            if (currentX == finishX || currentY == finishY) {  //Same column || same row

                //Begin iterating from min value to max value, thus use temp variable to determine them
                int temp = finishY;
                finishY = Math.max(currentY, finishY);
                currentY = Math.min(currentY, temp);
                
                temp = finishX;
                finishX = Math.max(currentX, finishX);
                currentX = Math.min(currentX, temp);

                //Var to store amount of moves to be performed to avoid infinite loop
                int amountOfMoves = Math.max(Math.abs(finishX - currentX), Math.abs(finishY - currentY));

                //Iteration from min value to max value
                for (; amountOfMoves >= 0 && 
                    (currentX == finishX ? (currentY <= finishY) : (currentX <= finishX));
                    currentY += ((currentY > finishY) ? -1 : (currentY == finishY) ? 0 : 1), 
                    currentX += ((currentX > finishX) ? -1 : (currentX == finishX) ? 0 : 1), 
                    --amountOfMoves) {
                        grid[currentY][currentX] = grid[currentY][currentX] == '.' ? '1' : (char)(grid[currentY][currentX] + 1);
                }
            }

            else {  //Different rows and different columns
                int amountOfMoves = Math.max(Math.abs(finishX - currentX), Math.abs(finishY - currentY));

                //Iteration from min value to max value
                for ( ; amountOfMoves >= 0 && 
                    ((currentX > finishX) ? (currentX >= finishX) : (currentX <= finishX)) && 
                    ((currentY > finishY) ? (currentY >= finishY) : (currentY <= finishY)); 
                    currentX += (currentX > finishX) ? -1 : 1, 
                    currentY += (currentY > finishY) ? -1 : 1, 
                    --amountOfMoves) {
                        grid[currentY][currentX] = grid[currentY][currentX] == '.' ? '1' : (char)(grid[currentY][currentX] + 1);
                }
            }
        }

        countOverlappingCells(grid);
    }

    
    /**
     * Prints grid's content
     * @param grid 2D char array storing dots and numbers of hydrothermal vents
     */
    private void printGrid(char[][] grid) {
        for (int row = 0; row < grid.length; ++row) {
            for (int col = 0; col < grid[0].length; ++col) {
                System.out.print(grid[row][col] + " ");
            }
            System.out.println();
        }
    }


    /**
     * Counts cells which have values greater than or equal to 2 and prints the answer to the console
     * @param grid 2D char array storing dots and numbers of hydrothermal vents
     */
    private void countOverlappingCells(char[][] grid) {
        int counter = 0;

        for (int i = 0; i < grid.length; ++i) {
            for (int j = 0; j < grid[0].length; ++j) {
                if ((char)grid[i][j] >= '2') {
                    counter++;
                }
            }
        }

        System.out.println(counter);
    }
}


public class App {
    public static void main(String[] args) throws Exception {
        Solution object = new Solution();
        try {
            object.readFromFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
