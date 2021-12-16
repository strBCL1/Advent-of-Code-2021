import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

class Day8 {
    private List<String> inputLines = new ArrayList<>();
    private int totalSum = 0;


    public void readFromFile() {
        try (BufferedReader bf = new BufferedReader(new FileReader("input.txt"))) {
            String currentLine = "";

            while ((currentLine = bf.readLine()) != null) {
                inputLines.add(currentLine);
            }
        }
        
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
    }
}

public class App {
    public static void main(String[] args) {
        Day8 object = new Day8();
        object.readFromFile();
    }
}