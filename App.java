import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Class to solve part 1 task of Day 4 of 'Advent of Code 2021'.
 * The idea is to create and initialize: 
 * queue of numbers to be checked, ArrayList of custom type to store the whole grid on indices base,
 * ArrayList of rows numbers' sums, ArrayList of columns numbers' sums, 
 * auxiliary 2D array of custom type to store current grid while input reading.
 * 
 * Each time the number from queue is checked, its flag is switched,
 * and it is subtracted from row's sum and column's sum.
 * 
 * If either row's sum or column's sum have '0' value, it means
 * all the number from this row or column are checked. 
 * If so, return number from queue multiplied by sum of unchecked numbers in winning board.
 */

class Solution {
    Queue<Integer> q = new LinkedList<>(); //Stores first line values to be checked

    ArrayList<GridType[][]> grids = new ArrayList<>(); //Stores all grids from input

    ArrayList<Integer> rowsNumberSum = new ArrayList<>(); //Stores each row numbers' sums

    ArrayList<int[]> columnsNumberSum = new ArrayList<>(); //1D int arrays stores each column numbers' sums

    GridType[][] currentGrid = new GridType[5][5]; //Stores the current grid from the input stream
    
    int lineCounter = 0; //Index of current grid row in input
    int columnCounter = 0; //Index of current grid column in input

    { //Initialize currentGrid using default constructor
        for (int rowIndex = 0; rowIndex < 5; ++rowIndex) {
            for (int colIndex = 0; colIndex < 5; ++colIndex) {
                currentGrid[rowIndex][colIndex] = new GridType();
            }
        }
    }; 

    
    /**
     * Read input from text file
     * @throws IOException if the file wasn't found
     */
    public void readFromFile() throws IOException {
        BufferedReader bf = new BufferedReader(new FileReader("input.txt")); //Object to read data from input stream (here - from text file)
        String currentLine = ""; //Empty auxiliary string to iterate over input file

        while ((currentLine = bf.readLine()) != null) {
            if (currentLine.isEmpty()) { //Ignore blank rows
                continue;
            }

            processInputOf(currentLine);
        }
        
        //Alternative - use try-with-resources to close the input stream and release used system resources
        bf.close();

        //Calculate sum of numbers of each column in each grid
        calculateColsSums();

        int winningScore = calculateWinningScore();
        System.out.println("Winning score: " + winningScore);
    }
    

    /**
     * Function extracts numbers from string, initializes queue,
     * calculates sum of numbers in each row of each grid and initializes 'grid'
     * @param currentLine one line from input stream to be processed
     */
    private void processInputOf(String currentLine) { 
        Matcher matcher = Pattern.compile("\\d+").matcher(currentLine);
        int sumOfRowNumbers = 0;
    

        while (matcher.find()) {
            //Convert found subsequence to integer
            int currentNumber = Integer.parseInt(matcher.group());

            if (lineCounter == 0) {
                q.add(currentNumber);
            }
            else {
                //Call copy constructor to perform deep copy of integer object
                currentGrid[(lineCounter - 1) % 5][columnCounter % 5] = new GridType(new GridType(currentNumber));
                sumOfRowNumbers += currentNumber;
                columnCounter++;
            }
        }
        

        //Stop string processing if it's the very first line of input; increase line counter
        if (0 == lineCounter++) {
            return ;
        }


        rowsNumberSum.add(sumOfRowNumbers);


        //If the current line is last for current grid:
        if ((lineCounter - 1) % 5 == 0) {
            
            //Create a deep copy of current 2D grid and add it to grids array 
            grids.add(Arrays.stream(currentGrid).map(el -> el.clone()).toArray($ -> currentGrid.clone()));

            //Alternative approach:
            // final GridType[][] GRID_DEEP_COPY = new GridType[5][];

            // for (int i = 0; i < 5; i++) {
            //     GRID_DEEP_COPY[i] = Arrays.copyOf(currentGrid[i], 5);
            // }

            // grids.add(GRID_DEEP_COPY);
        }
    }


    /**
     * Function to count sum of numbers in each column of each grid
     */
    private void calculateColsSums() {
        for (int gridIndex = 0; gridIndex < grids.size(); ++gridIndex) {
            GridType[][] currentGrid = grids.get(gridIndex);


            //Array to store sums of numbers in each column of current grid
            int[] columnsSums = new int[5];
            int columnsSumsIndex = 0;


            for (int row = 0; row < 5; ++row) {
                int currentColumnSum = 0;

                //Iterate over current column
                for (int col = 0; col < 5; ++col) {
                    currentColumnSum += currentGrid[col][row].value;
                }

                columnsSums[columnsSumsIndex++] = currentColumnSum;
            }


            //Create deep copy of columns sums array to avoid accident value rewriting
            columnsNumberSum.add(Arrays.copyOf(columnsSums, 5));
        }
    }


    /**
     * Determines the winning board and the winning score
     * @return sum of unchecked numbers multiplied by the current number called from queue; '0' if no winning board found.
     */
    private int calculateWinningScore() {
        //Iterate over queue to mark all its numbers as checked in grids
        for (int queueNumber : q) {

            for (int gridIndex = 0; gridIndex < grids.size(); ++gridIndex) {
                GridType[][] currentGrid = grids.get(gridIndex);

                for (int gridRowIndex = 0; gridRowIndex < 5; ++gridRowIndex) {
                    for (int gridColumnIndex = 0; gridColumnIndex < 5; ++gridColumnIndex) {

                        //Ignore numbers that don't match current queue number
                        if (currentGrid[gridRowIndex][gridColumnIndex].value != queueNumber) {
                            continue;
                        }


                        GridType currentGridNumber = currentGrid[gridRowIndex][gridColumnIndex];
                        currentGridNumber.isChecked = true; //Mark current number in current grid as checked
                        

                        int currentRowIndex = (gridIndex * 5) + gridRowIndex;

                        //Subtract current number from current row numbers' sum
                        rowsNumberSum.set(currentRowIndex, rowsNumberSum.get(currentRowIndex) - currentGridNumber.value); 


                        int[] colsArray = columnsNumberSum.get(gridIndex);
                        colsArray[gridColumnIndex] -= currentGridNumber.value; //Subtract current number from current column numbers' sum


                        //If any row or any column has all numbers checked
                        if (rowsNumberSum.get(currentRowIndex) == 0 || colsArray[gridColumnIndex] == 0) {
                            return queueNumber * sumOfUncheckedNums(gridIndex);                           
                        }
                    }
                }
            }
        }

        return 0;
    }


    /**
     * Calculate the sum of all unchecked numbers in current grid
     * @param gridIndex index of current grid in 'grids' ArrayList
     * @return sum of unchecked numbers
     */
    private int sumOfUncheckedNums(int gridIndex) {
        GridType[][] currentGrid = grids.get(gridIndex);
        int uncheckedNumbersSum = 0;


        for (int rowIndex = 0; rowIndex < 5; ++rowIndex) {
            for (int colindex = 0; colindex < 5; ++colindex) {
                if (!currentGrid[rowIndex][colindex].isChecked) {
                    uncheckedNumbersSum += currentGrid[rowIndex][colindex].value;
                }
            }
        }


        return uncheckedNumbersSum;
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