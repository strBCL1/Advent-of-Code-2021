import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;


/**
 * Solution based on: https://github.com/TheTurkeyDev/Advent-of-Code-2021/blob/main/src/dev/theturkey/aoc2021/Day08.java
 * Contains explanations to each calculation step.
 */
class Day8 {
    private List<String> inputLines = new ArrayList<>();
    private int totalSum = 0;


    /**
     * Reads all lines from .txt file and puts them to list.
     */
    public void readFromFile() {
        try (BufferedReader bf = new BufferedReader(new FileReader("path/to/input/file.txt"))) {
            String currentLine = "";

            while ((currentLine = bf.readLine()) != null) {
                inputLines.add(currentLine);
            }
        }
        
        catch (Exception e) {
            e.printStackTrace();
        }


        processAllLines();
        System.out.println(totalSum);
    }


    /**
     * Creates array of all digits' letter patterns.
     */
    private void processAllLines() {
        for (String currentLine : inputLines) {
            // Suppose the current line is: "acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab | cdfeb fcadb cdfeb cdbaf"
            // Then we have this pattern:
            //  dddd
            // e    a
            // e    a
            //  ffff
            // g    b
            // g    b
            //  cccc
            // The process of this pattern calculation:


            // 0: "acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab "
            // 1: " cdfeb fcadb cdfeb cdbaf"
            String[] partsOfcurrentLine = currentLine.split("\\|"); 


            // 0: "acedgfb"
            // 1: "cdfbe"
            // 2: "gcdfa"
            // 3: "fbcad"
            // 4: "dab"
            // 5: "cefabd"
            // 6: "cdfgeb"
            // 7: "eafb"
            // 8: "cagedb"
            // 9: "ab"
            String[] digitsBeforePipe = partsOfcurrentLine[0].strip().split(" ");


            // 0: "cdfeb"
            // 1: "fcadb"
            // 2: "cdfeb"
            // 3: "cdbaf"
            String[] digitsAfterPipe = partsOfcurrentLine[1].strip().split(" ");
            

            // Contains pairs of digits and their patterns, e.g. 0: "abcefg", 1: "cf", ...
            // 'null' values before calculation.
            String[] currentLineDigits = new String[10];


            // Loop 1:
            for (String currentDigit : digitsBeforePipe) { // Get all digits of unique segments' amount: 1 (2), 4 (4), 7 (3) and 8 (7)
                switch (currentDigit.length()) { // Sort digits to get uniform digits standard to avoid 'null references' exceptions: "bd" and "db" both mean '1'.
                    case 2:
                        currentLineDigits[1] = sortString(currentDigit); break;

                    case 3:
                        currentLineDigits[7] = sortString(currentDigit); break;

                    case 4:
                        currentLineDigits[4] = sortString(currentDigit); break;

                    case 7:
                        currentLineDigits[8] = sortString(currentDigit); break;
                }
            }

            //currentLineDigits after loop 1 iteration:
            // 0: "null"
            // 1: "ab"
            // 2: "null"
            // 3: "null"
            // 4: "abef"
            // 5: "null"
            // 6: "null"
            // 7: "abd"
            // 8: "abcdefg"
            // 9: "null"

            
            // Loop 2:
            for (String currentDigit : digitsBeforePipe) { // Get all digits of segments' amount of 6: 0, 6 and 9
                if (currentDigit.length() == 6) {
                    currentDigit = sortString(currentDigit);

                    if (currentDigitIs(9, currentDigit, currentLineDigits)) {
                        currentLineDigits[9] = currentDigit;
                    }

                    else if (currentDigitIs(0, currentDigit, currentLineDigits)) {
                        currentLineDigits[0] = currentDigit;
                    }

                    else {
                        currentLineDigits[6] = currentDigit;
                    }
                }
            }

            //currentLineDigits after loop 2 iteration:
            // 0: "abcdeg"
            // 1: "ab"
            // 2: "null"
            // 3: "null"
            // 4: "abef"
            // 5: "null"
            // 6: "bcdefg"
            // 7: "abd"
            // 8: "abcdefg"
            // 9: "abcdef"
        
            
            // Loop 3:
            for (String currentDigit : digitsBeforePipe) { // Get all digits of segments' amount of 5: 2, 3 and 5
                if (currentDigit.length() == 5) {
                    currentDigit = sortString(currentDigit);

                    if (currentDigitIs(3, currentDigit, currentLineDigits)) {
                        currentLineDigits[3] = currentDigit;
                    }

                    else if (currentDigitIs(5, currentDigit, currentLineDigits)) {
                        currentLineDigits[5] = currentDigit;
                    }

                    else {
                        currentLineDigits[2] = currentDigit;
                    }
                }
            }

            //currentLineDigits after loop 3 iteration:
            // 0: "abcdeg"
            // 1: "ab"
            // 2: "acdfg"
            // 3: "abcdf"
            // 4: "abef"
            // 5: "bcdef"
            // 6: "bcdefg"
            // 7: "abd"
            // 8: "abcdefg"
            // 9: "abcdef"


            processDigitsAfterPipe(currentLineDigits, digitsAfterPipe);
        }
    }


    /**
     * Creates the number from digits coming after pipe and adds it to total sum.
     * @param currentLineDigits pairs of digits and their letter patterns, e.g. 0: "abcefg", 1: "cf", ...
     * @param digitsAfterPipe digits coming after pipe, e.g. "| cf abcefg"
     */
    private void processDigitsAfterPipe(String[] currentLineDigits, String[] digitsAfterPipe) {
        int sumOfCurrentDigits = 0;

        for (String letterPattern : digitsAfterPipe) {
            letterPattern = sortString(letterPattern);

            for (int currentDigitAsINT = 0; currentDigitAsINT < currentLineDigits.length; ++currentDigitAsINT) {
                if (letterPattern.equals(currentLineDigits[currentDigitAsINT])) {
                    sumOfCurrentDigits = sumOfCurrentDigits * 10 + currentDigitAsINT;
                }
            }
        }

        totalSum += sumOfCurrentDigits;
    }
    

    /**
     * Sorts the current digit to be in alphabet order to avoid 'null reference' exception
     * @param letterPattern letter representation of some digit.
     * @return sorted digit in alphabet order
     */
    private String sortString(String letterPattern) {
        return letterPattern.chars()
               .sorted()
               .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
               .toString();
    }


    /**
     * Checks if current letters pattern is digital representation of 'digit'.
     * @param digit digital representation to be compared to letters pattern.
     * @param letterPattern letters' pattern of some digit.
     * @param currentLineDigits all digits of current line.
     * @return true if current letter pattern matches given digital representation. Otherwise false.
     */
    private boolean currentDigitIs(int digit, String letterPattern, String[] currentLineDigits) {
        int numAsBaseOfCheck = 0;

        // Needed only to check if letterPattern is '5'
        // Used to count missing segments of letterPattern and '6'
        // If the number of missing segments is '1', it means letterPattern is '5'
        int countMissing = 0; 
        
        // For example: 
        //  --a--
        // |     |
        // b     c
        // |     |
        //  --d--
        //       |
        // e     f
        //       |
        //  --g--
        // Letters are the equivalents to their segments.

        //   a
        //|     |
        //b     c
        //|     |
        // --d--
        //      |
        //e     f
        //      |
        //   g

        // Then same part of 9 and 4 is "bdcf". There are no 'a', 'e' and 'g' in 4 comparing to 9.

        switch (digit) { // Choose the number to find similar segments with (see previous comments).
            case 9:
                numAsBaseOfCheck = 4; break;
            
            case 0:
                numAsBaseOfCheck = 1; break;

            case 3:
                numAsBaseOfCheck = 1; break;

            case 5:
                numAsBaseOfCheck = 6; break;
        }

        for (char c : currentLineDigits[numAsBaseOfCheck].toCharArray()) {
            if (!letterPattern.contains(String.valueOf(c))) {
                if (numAsBaseOfCheck != 6) { // Needed only to check if letterPattern is '5'
                    return false;
                }
                else {
                    countMissing += 1;
                }
            }
        }

        if (numAsBaseOfCheck == 6) { // Needed only to check if letterPattern is '5'
            return countMissing == 1;
        }

        return true;
    }
}


public class App {
    public static void main(String[] args) {
        Day8 object = new Day8();
        object.readFromFile();
    }
}