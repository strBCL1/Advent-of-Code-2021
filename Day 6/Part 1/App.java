import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Solution {
    final private int AMOUNT_OF_DAYS = 256;
    private ArrayList<Integer> numbers = new ArrayList<Integer>();


    public void readFromFile() throws IOException {
        BufferedReader bf = new BufferedReader(new FileReader("/home/mertens/VSCode Workspace/Java Language/Advent-of-Code-2021/Day 6/Part 1/input.txt"));
        String currentLine = "";
        
        while ((currentLine = bf.readLine()) != null) {
            Matcher matcher = Pattern.compile("\\d+").matcher(currentLine);

            while (matcher.find()) {
                int currentNumber = Integer.parseInt(matcher.group());
                numbers.add(currentNumber);
            }
        }

        bf.close();

        countLanternfishes();
    }


    private void countLanternfishes() {
        int currentDay = 0;
        int currentSize = numbers.size();
        int currentIndex = 0;

        while (currentDay <= AMOUNT_OF_DAYS) {
            if (currentIndex == currentSize) {
                currentSize = numbers.size();
                currentIndex = 0;
                currentDay += 1;

                if (currentDay == AMOUNT_OF_DAYS) {
                    System.out.println(numbers.size());
                    return ;
                }
            }

            numbers.set(currentIndex, numbers.get(currentIndex) - 1);

            if (numbers.get(currentIndex) < 0) {
                numbers.set(currentIndex, 6);
                numbers.add(8);
            }

            currentIndex++;
            
        }
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