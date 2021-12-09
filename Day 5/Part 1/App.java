import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Solution {
    private LinkedList<Entry<Integer, Integer>> startCoords = new LinkedList<>();
    private LinkedList<Entry<Integer, Integer>> finishCoords = new LinkedList<>();
    int maxX = 0, maxY = 0;

    public void readFromFile() throws IOException {
        BufferedReader bf = new BufferedReader(new FileReader("/home/mertens/Рабочий стол/VSCode Workspace/Java Language/AoC/Day 5/Part 1/input.txt"));
        String currentLine = "";

        while ((currentLine = bf.readLine()) != null) {
            Matcher matcher = Pattern.compile("\\d+").matcher(currentLine);
            int[] currentRowCoords = new int[4]; int curRowsCoordIndex = 0;

            while (matcher.find()) {
                currentRowCoords[curRowsCoordIndex++] = Integer.parseInt(matcher.group());
            }

            startCoords.add(Map.entry(currentRowCoords[0], currentRowCoords[1]));
            finishCoords.add(Map.entry(currentRowCoords[2], currentRowCoords[3]));

            maxX = Math.max(Math.max(maxX, currentRowCoords[0]), currentRowCoords[2]);
            maxY = Math.max(Math.max(maxY, currentRowCoords[1]), currentRowCoords[3]);
        }

        bf.close();

        processGrid();
    }


    
    private void processGrid() {
        char[][] grid = new char[maxY + 1][maxX + 1];
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


            if (currentX == finishX) {  //startX == finishX - same column
                int temp = finishY;
                finishY = Math.max(currentY, finishY);
                currentY = Math.min(currentY, temp);
                for (; currentY <= finishY;  currentY += (currentY > finishY) ? -1 : 1) {
                    grid[currentY][currentX] = grid[currentY][currentX] == '.' ? '1' : (char)(grid[currentY][currentX] + 1);
                }
            }

            else if (currentY == finishY) {//same row
                int temp = finishX;
                finishX =  Math.max(currentX, finishX);
                currentX = Math.min(currentX, temp);
                for (; currentX <= finishX; currentX += (currentX > finishX) ? -1 : 1) {
                    grid[currentY][currentX] = grid[currentY][currentX] == '.' ? '1' : (char)(grid[currentY][currentX] + 1);
                }
            }

            else { //different rows and different columns
                int temp = finishY;
                finishY = Math.max(currentY, finishY);
                currentY = Math.min(currentY, temp);

                temp = finishX;
                finishX =  Math.max(currentX, finishX);
                currentX = Math.min(currentX, temp);

                for (; currentY <= finishY; currentX += (currentX > finishX) ? -1 : 1, currentY += (currentY > finishY) ? -1 : 1 ) {
                    grid[currentY][currentX] = grid[currentY][currentX] == '.' ? '1' : (char)(grid[currentY][currentX] + 1);
                }
            }
        }

        printGrid(grid);
        countCells(grid);
    }


    // 1 . 1 . . . . 1 1 . 
    // . 1 1 1 . . . 2 . . 
    // . . 2 . 1 . 1 1 1 . 
    // . . . 1 . 2 . 2 . . 
    // . 1 1 2 2 1 3 2 1 1 
    // . . . 1 . 2 . . . . 
    // . . 1 . . . 1 . . . 
    // . 1 . . . . . 1 . . 
    // 1 . . . . . . . 1 . 
    // 2 2 2 1 1 1 . . . .

    private void printGrid(char[][] grid) {
        for (int r = 0; r < grid.length; ++r) {
            for (int c = 0; c < grid[0].length; ++c) {
                System.out.print(grid[r][c] + " ");
            }
            System.out.println();
        }
    }


    private void countCells(char[][] grid) {
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
