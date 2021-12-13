import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Day8 {
    private final Map<Integer, Integer> M = Stream.of(new Object[][] {
        {1, 2},
        {7, 3},
        {4, 4},
        {2, 5},
        {3, 5},
        {5, 5},
        {0, 6},
        {6, 6},
        {9, 6},
        {8, 7},
        }).collect(Collectors.toMap(data -> (Integer) data[0], data -> (Integer) data[1]));

        private ArrayList<String> digits = new ArrayList<>();
        private List<String> strings5 = new ArrayList<>();
        private List<String> strings6 = new ArrayList<>();
        private LinkedHashMap<Integer, String> positionsLetters = new LinkedHashMap<>();

        private StringBuilder currentPattern = new StringBuilder();
        
    public int readFromFile() {
            try (BufferedReader bf = new BufferedReader(new FileReader("/home/mertens/VSCode Workspace/Java Language/Advent-of-Code-2021/Day 8/input.txt"))) {
                String currentLine = "";
                boolean flag = false;

                //while ((currentLine = bf.readLine()) != null) {
                
                currentLine = bf.readLine();
                for (String s : currentLine.split(" ")) {
                    if (s.equals("|")) {
                        flag = true;
                        continue;
                    }

                    if (flag == true) {
                        digits.add(String.copyValueOf(s.toCharArray()));
                        continue;
                    }

                    switch (s.length()) {
                        case 2: // current number is 1
                            positionsLetters.put(1, s); 
                            break;

                        case 3: // current number is 7
                            positionsLetters.put(7, s);
                            break;

                        case 4: // current number is 4
                            positionsLetters.put(4, s);
                            break;

                        case 7: // current number is 8
                            positionsLetters.put(8, s);
                            break;

                        case 5: { // current number is 2 or 3 or 5
                            strings5.add(s);
                            break;
                        }

                        case 6: { // current number is 0 or 6 or 9
                            strings6.add(s);
                            break;
                        }

                    }
                }
                
                //}
            
                createPattern();
        }

        catch (Exception e) { }

        return 0;
    }

    private void createPattern() {
        currentPattern.append( getLetter(positionsLetters.get(7), positionsLetters.get(1), "subtract") ); //{7} - {1}
        // currentPattern.append( getLetter(positionsLetters.get(7), positionsLetters.get(1), "add").toString() )

        // System.out.println(currentLetter);
    }

    private char getLetter(String s1, String s2, String operation) {
        HashMap<Character, Integer> occurences = new HashMap<>();
        int i = 0, j = 0, maxValue = 0;
        char curChar1, curChar2;

        for ( ; i < s1.length() && j < s2.length(); ++i, ++j) {
            curChar1 = s1.charAt(i); 
            curChar2 = s2.charAt(j);

            occurences.put(curChar1, occurences.getOrDefault(curChar1, 0) + 1);
            occurences.put(curChar2, occurences.getOrDefault(curChar2, 0) + 1);

            maxValue = Math.max(maxValue, Math.max(occurences.get(s1.charAt(i)), occurences.get(s2.charAt(j))));
        }

        for (; i < s1.length(); ++i) {
            curChar1 = s1.charAt(i);
            occurences.put(curChar1, occurences.getOrDefault(curChar1, 0) + 1);
        }

        for (; j < s2.length(); ++j) {
            curChar2 = s2.charAt(j);
            occurences.put(curChar2, occurences.getOrDefault(curChar2, 0) + 1);
        }

        for (var pair : occurences.entrySet()) {
            if ((operation == "subtract" && pair.getValue() != maxValue) || (operation == "add" && pair.getValue() == maxValue)) {
                return pair.getKey();
            }
        }

        return '0';
    }
}

public class App {
    public static void main(String[] args) throws Exception {
        Day8 object = new Day8();
        System.out.println(object.readFromFile());

    }
}