import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;


/**
 * Solution based on Reddit posted Java solutions with explanation
 * Calculates amount of fishes based on pairs: fishes' ages <-> amount of these fishes
 * It works both in part 1 and 2
 */
class Solution {
    final private int AMOUNT_OF_DAYS = 18;


    /**
     * Calculates amount of fishes using the following method:
     * There are 10 ages: 0, 1, 2, 3, 4, 5, 6, 7, 8, 9. The 9th one is used for decreasing.
     * @throws IOException
     */
    public void readFromFile() throws IOException {
        BufferedReader bf = new BufferedReader(new FileReader("/home/mertens/VSCode Workspace/Java Language/Advent-of-Code-2021/Day 6/Part 2/input.txt")); //path/to/input/file
        long[] fishesAges = new long[10];

        for (String s : bf.readLine().split(",")) {
            fishesAges[Integer.parseInt(s)]++;
        }

        for (int currentDay = 0; currentDay < AMOUNT_OF_DAYS; ++currentDay) {
            fishesAges[7] += fishesAges[0];
            fishesAges[9] = fishesAges[0];

            for (int i = 0; i < 9; ++i) {
                fishesAges[i] = fishesAges[i + 1];
            }

            fishesAges[9] = 0;
        }

        System.out.println(Arrays.stream(fishesAges).sum());
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
