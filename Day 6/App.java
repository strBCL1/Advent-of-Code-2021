import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;


/**
 * Effective solution based on Reddit posted Java solutions with explanation and example.
 * Calculates amount of fishes based on pairs: fishes' age <-> amount of fishes at this age.
 * It works both in part 1 and 2
 */
class Solution {
    //Pair of index <-> value: age of fishes <-> amount of fishes at this age - similar to HashMap. 
    // 'long' type is used to avoid stack overflow.
    private long[] fishesAmountAtAgeOf = new long[10];

    /**
     * Calculates amount of fishes using the following method:
     * 
     * There are ages: 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 ('9' is aux index used for overwriting amounts of fishes at age of '8' to avoid IndexOutOfBoundsException). 
     * Let's say we have 4 fishes of age '1', 5 fishes of age '2' and 7 fishes of age '3'.
     * In this case we have pairs: 0 <-> 0, 1 <-> 4, 2 <-> 5, 3 <-> 7, 4 <-> 0, 5 <-> 0, 6 <-> 0, 7 <-> 0, 8 <-> 0, 9 <-> 0.
     * 
     * During 0th day:
     *      fishes of age '0' will reproduce and die and appear with other fishes at age of '6' and '8' (before values overwriting, so add '1' to their current ages):
     *      0 <-> 0 (dead), 1 <-> 4, 2 <-> 5, 3 <-> 7, 4 <-> 0, 5 <-> 0, 6 <-> 0, 7 <-> 0, 8 <-> 0, 9 <-> 0.
     *      
     *      Then, they lived this (current) day, which decreases life of each lanternfish by 1, so we have to update the amount of fishes at their corresponding ages:
     *      0 <-> 4, 1 <-> 5, 2 <-> 7, 3 <-> 0, 4 <-> 0, 5 <-> 0, 6 <-> 0, 7 <-> 0, 8 <-> 0, 9 <-> 0.
     * 
     *      Day finishes.
     * 
     * 
     * During 1st day:
     *      fishes of age '0' will reproduce and die and appear at age of '6' and '8' (before values overwriting, so add '1' to their current ages):
     *      0 <-> 0 (dead), 1 <-> 5, 2 <-> 7, 3 <-> 0, 4 <-> 0, 5 <-> 0, 6 <-> 0, 7 <-> 4, 8 <-> 0, 9 <-> 4.
     *      
     *      Then, they lived this (current) day, which decreases life of each lanternfish by 1, so we have to update the amount of fishes at their corresponding ages:
     *      0 <-> 5, 1 <-> 7, 2 <-> 0, 3 <-> 0, 4 <-> 0, 5 <-> 0, 6 <-> 4, 7 <-> 0, 8 <-> 4, 9 <-> 0.
     * 
     *      Day finishes.
     * 
     * 
     * During 2nd day:
     *      fishes of age '0' will reproduce and die and appear at age of '6' and '8' (before values overwriting, so add '1' to their current ages):     
     *      0 <-> 0 (dead), 1 <-> 7, 2 <-> 0, 3 <-> 0, 4 <-> 0, 5 <-> 0, 6 <-> 4, 7 <-> 5, 8 <-> 4, 9 <-> 5.
     * 
     *      Then, they lived this (current) day, which decreases life of each lanternfish by 1, so we have to update the amount of fishes at their corresponding ages:
     *      0 <-> 7, 1 <-> 0, 2 <-> 0, 3 <-> 0, 4 <-> 0, 5 <-> 4, 6 <-> 5, 7 <-> 4, 8 <-> 5, 9 <-> 0.
     * 
     * Repeat (amountOdDays - 3) times.
     * 
     * 
     * @param amountOfDays total days to calculate the amount of lanternfishes
     * @return Arrays.stream(fishesAmountAtAgeOf).sum() if calculation has been performed, else 0L if an error has occurred
     */
    public long calculateAmountOfFishesAfterDay(int amountOfDays) {
        try (BufferedReader bf = new BufferedReader(new FileReader("path/to/input/file"))) {

            //Extract all fishes and map them to their ages
            for (String s : bf.readLine().split(",")) {
                fishesAmountAtAgeOf[Integer.parseInt(s)]++;
            }

            for (int currentDay = 0; currentDay < amountOfDays; ++currentDay) {
                //Add amount of fishes of age '0' to amount of fishes of age '6' and '8'
                //'7' and '9' are used as the values overwriting will occur
                fishesAmountAtAgeOf[7] += fishesAmountAtAgeOf[0];
                fishesAmountAtAgeOf[9] += fishesAmountAtAgeOf[0]; 

                //Overwrite fishes' amounts at each age
                for (int i = 0; i < 9; ++i) { 
                    fishesAmountAtAgeOf[i] = fishesAmountAtAgeOf[i + 1];
                }

                fishesAmountAtAgeOf[9] = 0; //Zero amount of fishes of age '8' as no fishes at age of '8' left after fishes amounts' overwriting
            }

            return Arrays.stream(fishesAmountAtAgeOf).sum();
        } 

        catch (FileNotFoundException FNFE) {
            System.out.println("Input file not found!");
            FNFE.printStackTrace();
        }
        
        catch (IOException IOE) {
            System.out.println("I/O error has occurred!");
            IOE.printStackTrace();
        }        
        
        return 0L;
    }
}


public class App {
    public static void main(String[] args) throws Exception {
        Solution object = new Solution();

        long totalAmountOfFishes = object.calculateAmountOfFishesAfterDay(80);
        System.out.println(totalAmountOfFishes);
    }
}
