import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
<<<<<<< HEAD
import java.util.List;
=======
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;
>>>>>>> b25886616dc1f03d79c3a764066aebc3071800b5

class Day8 {
    private List<String> inputLines = new ArrayList<>();
    private int totalSum = 0;

    private enum Operation {
        ADDITION, SUBTRACTION
    }

<<<<<<< HEAD
    public void readFromFile() {
        try (BufferedReader bf = new BufferedReader(new FileReader("input.txt"))) {
            String currentLine = "";

            while ((currentLine = bf.readLine()) != null) {
                inputLines.add(currentLine);
            }
        }
=======
    private int totalSum = 0;

    Map<Integer, String> numberOfStickToItsLetter = new HashMap<>(); // 1 -> 'a', 2 -> 'b', ...
    { // Initialize map
        for (int i = 1; i <= 7; ++i) { 
            numberOfStickToItsLetter.put(i, ""); 
        }
    }
    

    public void processInput() {

        Map<Integer, String> uniqueDigits = new HashMap<>(); // Digits: 1, 4, 7, 8. 
        List<String> fragmentsOfLenFive = new ArrayList<>(); // Fragments of length 5 for numbers: 2, 3 and 5.
        List<String> fragmentsOfLenSix = new ArrayList<>();  // Fragments of length 6 for numbers: 0, 6 and 9.
        List<String> digitsAfterPipe = new ArrayList<>(); // Digits coming after pipe, e.g. "| cdfeb fcadb cdfeb cdbaf".

        try (BufferedReader bf = new BufferedReader(new FileReader("/home/mertens/VSCode Workspace/Java Language/Advent-of-Code-2021/Day 8/input.txt"))) {
            // String currentLine = "";
            String currentLine = bf.readLine();
            boolean pipeIsReached = false;

            // while ((currentLine = bf.readLine()) != null) {
                for (String currentSequence : currentLine.split(" ")) {
                    if (currentSequence.equals("|")) {
                        pipeIsReached = true; continue;
                    }

                    // Sort current fragment to properly compare digits coming after pipe
                    currentSequence = currentSequence.chars()
                                      .sorted()
                                      .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                                      .toString();
                    // java.util.Arrays.sort(currentSequence.toCharArray()) -- doesn't work


                    if (pipeIsReached) {
                        digitsAfterPipe.add(currentSequence); continue;
                    }

                    switch (currentSequence.length()) {
                        case 2:
                            uniqueDigits.put(1, currentSequence); break;
                            
                        case 3:
                            uniqueDigits.put(7, currentSequence); break;

                        case 4:
                            uniqueDigits.put(4, currentSequence); break;

                        case 7:
                            uniqueDigits.put(8, currentSequence); break;

                        case 5: 
                            fragmentsOfLenFive.add(currentSequence); break;

                        case 6: 
                            fragmentsOfLenSix.add(currentSequence); break;
                    }
                }
            // }
        }
        catch (Exception e) {}

        discoverPattern(uniqueDigits, fragmentsOfLenFive, fragmentsOfLenSix);
        processDigitsComingAfterPipe(uniqueDigits, digitsAfterPipe);
        System.out.println(totalSum);
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
    private void discoverPattern(Map<Integer, String> positionToString, List<String> fragmentsOfLenFive, List<String> fragmentsOfLenSix) {
        StringBuilder currentPattern = new StringBuilder();

        // 1 = {7} - {1}
        String result = calculateSequence(positionToString.get(7), positionToString.get(1), Operation.SUBTRACTION); 
        numberOfStickToItsLetter.put(1, result);
        currentPattern.append(result);


        result = calculateSequence(fragmentsOfLenFive.get(0), fragmentsOfLenFive.get(1), Operation.ADDITION); // 147 = {2 / 3 / 5} + {2 / 3 / 5} 
        currentPattern.append(calculateSequence(result, positionToString.get(4), Operation.ADDITION)); // 4 = {147} + {4}
        numberOfStickToItsLetter.put(4, Character.toString(currentPattern.toString().charAt(currentPattern.length() - 1))); 

>>>>>>> b25886616dc1f03d79c3a764066aebc3071800b5
        
        catch (Exception e) {}


        processAllLines();

        System.out.println(totalSum);
    }


    private void processAllLines() {
        for (String currentLine : inputLines) {
            String[] partsOfcurrentLine = currentLine.split("\\|");
            String[] digitsBeforePipe = partsOfcurrentLine[0].split(" ");
            String[] digitsAfterPipe = partsOfcurrentLine[1].split(" ");
            String[] digits = new String[10];


            for (String currentDigit : digitsBeforePipe) { // Get all unique length digits: 1, 4, 7 and 8
                switch (currentDigit.length()) { // Sort only unique length digits
                    case 2:
                    case 3:
                    case 4:
                    case 7:
                        currentDigit = sortString(currentDigit);
                }

                switch (currentDigit.length()) {
                    case 2:
                        digits[1] = currentDigit; break;

                    case 3:
                        digits[7] = currentDigit; break;

                    case 4:
                        digits[4] = currentDigit; break;

                    case 7:
                        digits[8] = currentDigit; break;
                }
            }
            

            for (String currentDigit : digitsBeforePipe) { // Get all digits of length 6: 0, 6 and 9
                if (currentDigit.length() == 6) {
                    currentDigit = sortString(currentDigit);

                    if (currentDigitIs(9, currentDigit, digits)) {
                        digits[9] = currentDigit;
                    }

                    else if (currentDigitIs(0, currentDigit, digits)) {
                        digits[0] = currentDigit;
                    }

                    else {
                        digits[6] = currentDigit;
                    }
                }
            }
        
        
            for (String currentDigit : digitsBeforePipe) { // Get all digits of length 5: 2, 3 and 5
                if (currentDigit.length() == 5) {
                    currentDigit = sortString(currentDigit);

                    if (currentDigitIs(3, currentDigit, digits)) {
                        digits[3] = currentDigit;
                    }

                    else if (currentDigitIs(5, currentDigit, digits)) {
                        digits[5] = currentDigit;
                    }

                    else {
                        digits[2] = currentDigit;
                    }
                }
            }


            processDigitsAfterPipe(digits, digitsAfterPipe);
        }
    }


    private void processDigitsAfterPipe(String[] digits, String[] digitsAfterPipe) {
        int sumOfCurrentDigits = 0;

        for (String currentDigit : digitsAfterPipe) {
            currentDigit = sortString(currentDigit);

            for (int i = 0; i < digits.length; ++i) {
                if (currentDigit.equals(digits[i])) {
                    sumOfCurrentDigits = sumOfCurrentDigits * 10 + i;
                }
            }
        }

        totalSum += sumOfCurrentDigits;
    }
    

    private String sortString(String s) {
        return s.chars()
               .sorted()
               .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
               .toString();
    }


<<<<<<< HEAD
    private boolean currentDigitIs(int digit, String currentDigit, String[] digits) {
        int numAsBaseOfCheck = 0;
        int countMissing = 0; // Needed only currentDigit to be checked is 5

        switch (digit) {
            case 9:
                numAsBaseOfCheck = 4; break;
            
            case 0:
                numAsBaseOfCheck = 1; break;

            case 3:
                numAsBaseOfCheck = 1; break;

            case 5:
                numAsBaseOfCheck = 6; break;
        }

        for (char c : digits[numAsBaseOfCheck].toCharArray()) {
            if (!currentDigit.contains(String.valueOf(c))) {
                if (numAsBaseOfCheck != 6) {
                    return false;
                }
                else {
                    countMissing += 1;
                }
            }
        }

        if (numAsBaseOfCheck == 6) {
            return countMissing == 1;
        }

        return true;
=======
    private void processDigitsComingAfterPipe(Map<Integer, String> uniqueDigits, List<String> digitsAfterPipe) {
        Map<String, Integer> stickedStringToRealNumber = new TreeMap<>();
        final Map<String, Integer> STICKS_TO_NUMBER = Stream.of(new Object[][] { // Order of each digit, grouped by sticks' numbers
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


        for (var entry : STICKS_TO_NUMBER.entrySet()) {
            Set<String> sortedDigit = new TreeSet<>();

            for (char c : entry.getKey().toCharArray()) {
                String letter = numberOfStickToItsLetter.get(c - '0');
                sortedDigit.add(letter);
            }

            stickedStringToRealNumber.put(String.join("", sortedDigit), entry.getValue());
        }


        StringBuilder digitsAfterPipeNumber = new StringBuilder();
        for (String digit : digitsAfterPipe) {
            digitsAfterPipeNumber.append(stickedStringToRealNumber.get(digit));
        }

        totalSum += Integer.parseInt(digitsAfterPipeNumber.toString());

        // System.out.println(digitsAfterPipeNumber.toString());
        // stickedStringToRealNumber.entrySet().forEach(entry -> System.out.println(entry.getKey()));
>>>>>>> b25886616dc1f03d79c3a764066aebc3071800b5
    }
}


public class App {
    public static void main(String[] args) {
        Day8 object = new Day8();
        object.readFromFile();
    }
}