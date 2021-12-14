import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Day8 {
    private StringBuilder currentPattern = new StringBuilder();

    private final Map<Integer, Integer> NUMS_AND_STICKS_AMOUNT = Stream.of(new Integer[][] {
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
    }).collect(Collectors.toMap(data -> data[0], data -> data[1]));

    private Map<Integer, String> positionToString = new HashMap<>(); // {1} -> "ab", {7} -> "gab", ...
    private ArrayList<String> fragmentsOfLenFive = new ArrayList<>(); // Fragments of length 5 for numbers: 2, 3 and 5.
    private ArrayList<String> fragmentsOfLenSix = new ArrayList<>();  // Fragments of length 6 for numbers: 0, 6 and 9.
    private ArrayList<String> digitsAsStrings = new ArrayList<>(); // Digits coming after pipe, e.g. | cdfeb fcadb cdfeb cdbaf.

    public void processInput() {
        try (BufferedReader bf = new BufferedReader(new FileReader("/home/mertens/VSCode Workspace/Java Language/Advent-of-Code-2021/Day 8/input.txt"))) {
            // String currentLine = "";
            String currentLine = bf.readLine();
            boolean pipeIsReached = false;

            // while ((currentLine = bf.readLine()) != null) {
                for (String currentSequence : currentLine.split(" ")) {
                    if (currentSequence.equals("|")) {
                        pipeIsReached = true; continue;
                    }

                    if (pipeIsReached) {
                        digitsAsStrings.add(currentSequence); continue;
                    }

                    switch (currentSequence.length()) {
                        case 2: 
                            positionToString.put(1, currentSequence); break;

                        case 3:
                            positionToString.put(7, currentSequence); break;

                        case 4:
                            positionToString.put(4, currentSequence); break;

                        case 7:
                            positionToString.put(8, currentSequence); break;

                        case 5: 
                            fragmentsOfLenFive.add(currentSequence); break;

                        case 6: 
                            fragmentsOfLenSix.add(currentSequence); break;
                    }
                }
            // }
        }
        catch (Exception e) { }

        discoverPattern();
    }

    /**
     *      1
          _____
         |     |
       2 |     | 3
         |--4--|
       5 |     | 6
         |_____|
            7
     */
    private void discoverPattern() {

        // 1 = {7} - {1}
        String result = calculateSequence(positionToString.get(7), positionToString.get(1), Operation.SUBTRACTION);        
        currentPattern.append(result);


        result = calculateSequence(fragmentsOfLenFive.get(0), fragmentsOfLenFive.get(1), Operation.ADDITION); // 147 = {2 / 3 / 5} + {2 / 3 / 5} 
        currentPattern.append(calculateSequence(result, positionToString.get(4), Operation.ADDITION)); // 4 = {147} + {4}

        
        for (int i = 0; i < fragmentsOfLenSix.size(); ++i) { // 7 = {147} + {0} - {W}
            result = calculateSequence(result, fragmentsOfLenSix.get(i), Operation.ADDITION);
            if (result != null && result.length() == 2) { 
                currentPattern.append(calculateSequence(result, currentPattern.toString(), Operation.SUBTRACTION));
            }    
        }


        for (int i = 0; i < fragmentsOfLenSix.size(); ++i) { // 5 = {9} - {8}
            if (currentPattern.toString().length() == 3) {
                result = calculateSequence(fragmentsOfLenSix.get(i), positionToString.get(8), Operation.ADDITION); 
                if (result.length() == 6 && calculateSequence(result, currentPattern.toString(), Operation.ADDITION).length() == 3) { //Check if current 6-length fragment is 9:
                    currentPattern.append(calculateSequence(positionToString.get(8), fragmentsOfLenSix.get(i), Operation.SUBTRACTION));
                }
            }
        }


        for (int i = 0; i < fragmentsOfLenSix.size(); ++i) { // 6 = {6} + {1}
            result = calculateSequence(fragmentsOfLenSix.get(i), positionToString.get(1), Operation.ADDITION);
            if (result.length() == 1) {
                currentPattern.append(result);
            }
        }


        currentPattern.append(calculateSequence(positionToString.get(1), currentPattern.toString(), Operation.SUBTRACTION)); // 3 = {1} - {W}


        currentPattern.append(calculateSequence(positionToString.get(8), currentPattern.toString(), Operation.SUBTRACTION)); // 2 = {8} - {W}

        
        System.out.println(currentPattern.toString());
    }

    private String calculateSequence(String stringOne, String stringTwo, Operation operation) {
        HashMap<Character, Integer> charsOccurences = new HashMap<>();
        StringBuilder answer = new StringBuilder();
        int maxValue = 0;

        for (int i = 0; i < stringOne.length(); ++i) {
            Character currentCharacter = stringOne.charAt(i);
            charsOccurences.put(currentCharacter, charsOccurences.getOrDefault(currentCharacter, 0) + 1);
            maxValue = Math.max(maxValue, charsOccurences.get(currentCharacter));
        }

        for (int j = 0; j < stringTwo.length(); ++j) {
            Character currentCharacter = stringTwo.charAt(j);
            charsOccurences.put(stringTwo.charAt(j), charsOccurences.getOrDefault(stringTwo.charAt(j), 0) + 1);
            maxValue = Math.max(maxValue, charsOccurences.get(currentCharacter));
        }

        for (var pair : charsOccurences.entrySet()) {
            if (operation == Operation.SUBTRACTION && pair.getValue() < maxValue) {
                return Character.toString(pair.getKey());
            }
            else if (operation == Operation.ADDITION && pair.getValue() == maxValue) {
                answer.append(pair.getKey());
            }
                
        }

        return answer.toString();
    }

    private enum Operation {
        ADDITION, SUBTRACTION
    }
}

public class App {
    public static void main(String[] args) throws Exception {
        Day8 object = new Day8();
        object.processInput();
    }
}