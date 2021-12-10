import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Performs brute-force solution of lanternfishes problem
 * It won't work in Part 2 due to stack overflow
 */
class Solution {
    final private int AMOUNT_OF_DAYS = 80;
    private ArrayList<Integer> fishes = new ArrayList<Integer>();


    /**
     * Reads initial fishes and puts them into arraylist
     * @throws IOException if input file is not found
     */
    public void readFromFile() throws IOException {
        BufferedReader bf = new BufferedReader(new FileReader("path/to/input/file"));
        String currentLine = "";
        
        //Extract all fishes from row and place them in arraylist
        while ((currentLine = bf.readLine()) != null) {
            Matcher matcher = Pattern.compile("\\d+").matcher(currentLine);

            while (matcher.find()) {
                fishes.add(Integer.parseInt(matcher.group()));
            }
        }

        bf.close();

        //Calculate the amount of fishes
        long answer = countLanternfishes();
        System.out.println(answer);
    }


    /**
     * Counts amounts of lanternfishes in a brute-force way
     * It won't work in Part 2 due to stack overflow
     */
    private long countLanternfishes() {
        int currentDay = 0;
        int currentSize = fishes.size(); //Fix size to avoid infinite loop
        int currentFishesIndex = 0;

        while (currentDay <= AMOUNT_OF_DAYS) {
            //If current pos in arraylist if last one comparing to fixed size variable:
            //Update current fixed size, set index to zero to start again and update current day
            if (currentFishesIndex == currentSize) {
                currentSize = fishes.size();  
                currentFishesIndex = 0;
                currentDay += 1;

                if (currentDay == AMOUNT_OF_DAYS) {
                    return fishes.size();
                }
            }

            fishes.set(currentFishesIndex, fishes.get(currentFishesIndex) - 1);

            if (fishes.get(currentFishesIndex) < 0) {
                fishes.set(currentFishesIndex, 6);
                fishes.add(8);
            }

            currentFishesIndex++;
        }

        return fishes.size();
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