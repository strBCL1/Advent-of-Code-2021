import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

class Solution {
    final private int AMOUNT_OF_DAYS = 18;

    public void readFromFile() throws IOException {
        BufferedReader bf = new BufferedReader(new FileReader("path/yo/input/file"));
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