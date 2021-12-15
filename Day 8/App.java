import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Day8 {
    // private final Map<Integer, Integer> NUMS_AND_STICKS_AMOUNT = Stream.of(new Integer[][] {
    //     {1, 2},
    //     {7, 3},
    //     {4, 4},
    //     {2, 5},
    //     {3, 5},
    //     {5, 5},
    //     {0, 6},
    //     {6, 6},
    //     {9, 6},
    //     {8, 7},
    // }).collect(Collectors.toMap(data -> data[0], data -> data[1]));



    public void processInput() {
        StringBuilder currentPattern = new StringBuilder();


        HashMap<Integer, String> numberOfStickToItsLetter = new HashMap<>() {
            {
                put(1, "");
                put(2, "");
                put(3, "");
                put(4, "");
                put(5, "");
                put(6, "");
                put(7, "");
            }
        };


        Map<String, Integer> numberToItsSticks = Stream.of(new Object[][] {
            {"123567", 0},
            {"36", 1},
            {"13457", 2},
            {"13467", 3},
            {"2346", 4},
            {"12467", 5},
            {"124567", 6},
            {"136", 7},
            {"1234567", 8},
            {"123467", 9}
        }).collect(Collectors.toMap(data -> (String) data[0], data -> (Integer) data[1]));


        Map<Integer, String> positionToString = new HashMap<>(); // {1} -> "ab", {7} -> "gab", ...
        ArrayList<String> fragmentsOfLenFive = new ArrayList<>(); // Fragments of length 5 for numbers: 2, 3 and 5.
        ArrayList<String> fragmentsOfLenSix = new ArrayList<>();  // Fragments of length 6 for numbers: 0, 6 and 9.
        ArrayList<String> digitsAfterPipe = new ArrayList<>(); // Digits coming after pipe, e.g. "| cdfeb fcadb cdfeb cdbaf".

        try (BufferedReader bf = new BufferedReader(new FileReader("/home/mertens/VSCode Workspace/Java Language/Advent-of-Code-2021/Day 8/input.txt"))) {
            String currentLine = "";
            // String currentLine = bf.readLine();
            boolean pipeIsReached = false;

            while ((currentLine = bf.readLine()) != null) {
                for (String currentSequence : currentLine.split(" ")) {
                    if (currentSequence.equals("|")) {
                        pipeIsReached = true; continue;
                    }

                    // Sort current fragment to properly compare digits coming after pipe
                    currentSequence = currentSequence.chars()
                                    .sorted()
                                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                                    .toString();

                    if (pipeIsReached) {
                        digitsAfterPipe.add(currentSequence); continue;
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
            }
        }
        catch (Exception e) { }

        discoverPattern(positionToString, fragmentsOfLenFive, fragmentsOfLenSix, numberOfStickToItsLetter, currentPattern);
        processDigitsComingAfterPipe(numberToItsSticks, numberOfStickToItsLetter, digitsAfterPipe);
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
    private void discoverPattern(Map<Integer, String> positionToString, ArrayList<String> fragmentsOfLenFive, ArrayList<String> fragmentsOfLenSix, 
                                HashMap<Integer, String> numberOfStickToItsLetter, StringBuilder currentPattern) {

        // 1 = {7} - {1}
        String result = calculateSequence(positionToString.get(7), positionToString.get(1), Operation.SUBTRACTION); 
        numberOfStickToItsLetter.put(1, result);
        currentPattern.append(result);


        result = calculateSequence(fragmentsOfLenFive.get(0), fragmentsOfLenFive.get(1), Operation.ADDITION); // 147 = {2 / 3 / 5} + {2 / 3 / 5} 
        currentPattern.append(calculateSequence(result, positionToString.get(4), Operation.ADDITION)); // 4 = {147} + {4}
        numberOfStickToItsLetter.put(4, Character.toString(currentPattern.toString().charAt(currentPattern.length() - 1))); 

        
        for (int i = 0; i < fragmentsOfLenSix.size(); ++i) { // 7 = {147} + {0} - {W}
            result = calculateSequence(result, fragmentsOfLenSix.get(i), Operation.ADDITION);
            if (result != null && result.length() == 2) { 
                currentPattern.append(calculateSequence(result, currentPattern.toString(), Operation.SUBTRACTION));
                numberOfStickToItsLetter.put(7, Character.toString(currentPattern.toString().charAt(currentPattern.length() - 1)));
            }    
        }


        for (int i = 0; i < fragmentsOfLenSix.size(); ++i) { // 5 = {9} - {8}
            if (currentPattern.toString().length() == 3) {
                result = calculateSequence(fragmentsOfLenSix.get(i), positionToString.get(8), Operation.ADDITION); 
                if (result.length() == 6 && calculateSequence(result, currentPattern.toString(), Operation.ADDITION).length() == 3) { //Check if current 6-length fragment is 9:
                    currentPattern.append(calculateSequence(positionToString.get(8), fragmentsOfLenSix.get(i), Operation.SUBTRACTION));
                    numberOfStickToItsLetter.put(5, Character.toString(currentPattern.toString().charAt(currentPattern.length() - 1)));
                }
            }
        }


        for (int i = 0; i < fragmentsOfLenSix.size(); ++i) { // 6 = {6} + {1}
            result = calculateSequence(fragmentsOfLenSix.get(i), positionToString.get(1), Operation.ADDITION);
            if (result.length() == 1) {
                currentPattern.append(result);
                numberOfStickToItsLetter.put(6, result);
            }
        }


        currentPattern.append(calculateSequence(positionToString.get(1), currentPattern.toString(), Operation.SUBTRACTION)); // 3 = {1} - {W}
        numberOfStickToItsLetter.put(3, Character.toString(currentPattern.toString().charAt(currentPattern.length() - 1)));


        currentPattern.append(calculateSequence(positionToString.get(8), currentPattern.toString(), Operation.SUBTRACTION)); // 2 = {8} - {W}
        numberOfStickToItsLetter.put(2, Character.toString(currentPattern.toString().charAt(currentPattern.length() - 1)));


        // System.out.println(currentPattern.toString());
        // numberOfStickToItsLetter.entrySet().forEach(pair -> System.out.println(pair.getValue()));
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


    private void processDigitsComingAfterPipe(Map<String, Integer> numberToItsSticks, HashMap<Integer, String> numberOfStickToItsLetter, ArrayList<String> digitsAfterPipe) {
        Map<String, Integer> stickedStringToRealNumber = new TreeMap<>();

        for (var entry : numberToItsSticks.entrySet()) {
            StringBuilder s = new StringBuilder();

            for (char c : entry.getKey().toCharArray()) {
                s.append(numberOfStickToItsLetter.get(c - '0'));
            }

            String sorted = s.chars()        // IntStream
            .sorted()
            .collect(StringBuilder::new,
                    StringBuilder::appendCodePoint,
                    StringBuilder::append)
            .toString();

            stickedStringToRealNumber.put(sorted, entry.getValue());
        }


        StringBuilder currentAfterPipeNumber = new StringBuilder();
        for (String digit : digitsAfterPipe) {
            String sorted = digit.chars()
                                    .sorted()
                                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                                    .toString();

            currentAfterPipeNumber.append(stickedStringToRealNumber.get(sorted));
        }

        System.out.println(currentAfterPipeNumber.toString());
        // stickedStringToRealNumber.entrySet().forEach(entry -> System.out.println(entry.getKey()));
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