import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

class Day13 {
    // Pairs of x <-> y coords. One column may contain multiple unique 'y' coords.
    private static Map<Integer, Set<Integer>> coordinatesMap = new HashMap<>();
    private static int dotsAmountAfterFirstFold = 0; // Part 1 answer


    /**
     * Reads input lines from file. Creates pairs of coords and calls sheet fold function.
     */
    public void readFromFile() {
        try (Stream<String> inputStream = Files.lines(Paths.get(Day13.class.getResource("/input.txt").toURI()))) { 
            inputStream.forEach(line -> {
                if (line.isEmpty()) { // Ignore 1 blank row
                    return ;
                }

                if (line.contains("fold")) { // If it is a fold procedure
                    Matcher matcher = Pattern.compile("[0-9]+").matcher(line);
                    matcher.find();

                    int foldCoordinate = Integer.parseInt(matcher.group()); // Extract the fold coordinate
                    boolean foldAlongX = line.contains("x="); // Check if coordinate is 'x' or 'y' axis
                    foldSheet(foldCoordinate, foldAlongX); // Fold sheet

                    return ;
                }

                String[] lineCoords = line.split(",");
                int x = Integer.parseInt(lineCoords[0]); // Extract 'x' coord
                int y = Integer.parseInt(lineCoords[1]); // Extract 'y' coord

                // Insert 'y' into an empty or already existing set
                if (coordinatesMap.computeIfAbsent(x, set -> new HashSet<>()) != null) {
                    coordinatesMap.get(x).add(y);
                }

            });

            // Calculate max 'y' bound
            int yBound = coordinatesMap.values().stream()
                                            .mapToInt(set -> Collections.max(set))
                                            .max()
                                            .orElse(-1) + 1;

            // Calculate max 'x' bound
            int xBound = Collections.max(coordinatesMap.keySet()) + 1;


            //Declare output grid that will contain a part 2 code
            char[][] grid = new char[yBound][xBound];
            for (char[] row : grid) {
                Arrays.fill(row, '.');
            }


            for (Entry<Integer, Set<Integer>> e : coordinatesMap.entrySet()) {
                for (int y : e.getValue()) {
                    grid[y][e.getKey()] = '#';
                }
            }


            // Part 1 answer
            System.out.println("Dots amount after first fold: " + dotsAmountAfterFirstFold + "\n");


            // Print code - part 2 answer
            Stream.of(grid)
                  .flatMap(Stream::of)
                  .forEach(System.out::println);
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Folds sheet using pairs of coords. 
     * @param foldCoordinate coordinate along which sheet must be folded
     * @param foldAlongX true if fold to be done is along 'x' axis. Else false.
     */
    private void foldSheet(int foldCoordinate, boolean foldAlongX) {
        // Create deep copy of current 'x' axis (without sets deep copying)
        Map<Integer, Set<Integer>> coordinatesDeepCopyMap = new HashMap<>(coordinatesMap);

        // Create deep copy of all sets of map ('y' axis)
        for (Entry<Integer, Set<Integer>> e : coordinatesMap.entrySet()) {
            coordinatesDeepCopyMap.put(e.getKey(), new HashSet<>(e.getValue()));
        }
        
        if (foldAlongX) { // Fold along 'x'
            coordinatesMap.entrySet().stream()
                                     .filter(e -> e.getKey() > foldCoordinate) // Get all 'x' coords that are greater than fold coord
                                     .forEach(e -> {
                                         coordinatesDeepCopyMap.remove(e.getKey()); // Remove all filtered 'x' coords from copy map

                                         // Add all sets of 'y' coords to their new corresponding columns 
                                         // foldCoordinate - (e.getKey() - foldCoordinate) == foldCoordinate - e.getKey() + foldCoordinate
                                         if (coordinatesDeepCopyMap.computeIfAbsent(foldCoordinate - e.getKey() + foldCoordinate, set -> new HashSet<>()) != null) {
                                            coordinatesDeepCopyMap.get(foldCoordinate - e.getKey() + foldCoordinate).addAll(e.getValue());
                                         }

                                     });
        }

        else { // Fold along 'y'
            coordinatesMap.entrySet().stream()
                                     .forEach(entry -> {
                                         entry.getValue().stream()
                                                         .filter(y -> y > foldCoordinate) // Get all 'y' greater than fold coord
                                                         .forEach(y -> {
                                                             coordinatesDeepCopyMap.get(entry.getKey()).remove(y); // Remove current 'y' from copy map

                                                             // Add new 'y' folded coord corresponding to current 'y'
                                                             // foldCoordinate - (y - foldCoordinate) == foldCoordinate - y + foldCoordinate
                                                             coordinatesDeepCopyMap.get(entry.getKey()).add(foldCoordinate - y + foldCoordinate);
                                                         });
                                     });
            
        }

        // Initialize amount of dots after first fold if it hasn't been initialized yet
        if (dotsAmountAfterFirstFold == 0) {
            dotsAmountAfterFirstFold = coordinatesDeepCopyMap.values().stream()
                                                                      .mapToInt(set -> set.size())
                                                                      .sum();
        }

        // Update reference to current map state
        coordinatesMap = coordinatesDeepCopyMap;
    }
}

public class App {
    public static void main(String[] args) throws Exception {
        Day13 object = new Day13();
        object.readFromFile();
    }
}
