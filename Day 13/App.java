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
    // No same coords in input (x <-> y)
    private static Map<Integer, Set<Integer>> coordinatesMap = new HashMap<>();
    private static int xBound = 0;
    private static int yBound = 0;


    public void readFromFile() {
        try (Stream<String> inputStream = Files.lines(Paths.get(Day13.class.getResource("/input.txt").toURI()))) { 
            inputStream.forEach(line -> {
                if (line.isEmpty()) { // Ignore 1 blank row
                    return ;
                }

                if (line.contains("fold")) { // If it is a fold procedure
                    Matcher matcher = Pattern.compile("[0-9]+").matcher(line);
                    matcher.find();

                    int foldCoordinate = Integer.parseInt(matcher.group()); // Get the coordinate
                    boolean foldAlongX = line.contains("x="); // Check if coordinate is 'x' or 'y'
                    foldSheet(foldCoordinate, foldAlongX); // Fold sheet

                    return ;
                }

                String[] lineCoords = line.split(",");
                int x = Integer.parseInt(lineCoords[0]); // Extract 'x' coord
                int y = Integer.parseInt(lineCoords[1]); // Extract 'y' coord

                // If value is null, create set and insert 'y' into it
                if (coordinatesMap.computeIfAbsent(x, set -> new HashSet<>()) != null) {
                    coordinatesMap.get(x).add(y);
                }

            });

            // Calculate max 'y' bound
            yBound = coordinatesMap.values().stream()
                                            .mapToInt(set -> Collections.max(set))
                                            .max()
                                            .orElse(-1) + 1;

            // Calculate max 'x' bound
            xBound = Collections.max(coordinatesMap.keySet()) + 1;


            char[][] grid = new char[yBound][xBound];
            for (char[] row : grid) {
                Arrays.fill(row, '.');
            }

            coordinatesMap.entrySet().stream()
                                     .forEach(e -> {
                                         e.getValue().forEach(y -> {
                                             grid[y][e.getKey()] = '#';
                                         });
                                     });

            Stream.of(grid)
                  .flatMap(Stream::of)
                  .forEach(System.out::println);
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void foldSheet(int foldCoordinate, boolean foldAlongX) {
        Map<Integer, Set<Integer>> copy = new HashMap<>(coordinatesMap);

        // Create deep copy of all sets of map
        for (Entry<Integer, Set<Integer>> e : coordinatesMap.entrySet()) {
            copy.put(e.getKey(), new HashSet<>(e.getValue()));
        }
        
        if (foldAlongX) { // Fold along 'x'
            coordinatesMap.entrySet().stream()
                                     .filter(e -> e.getKey() > foldCoordinate) // Get all 'x' greater than fold coord
                                     .forEach(e -> {
                                        copy.remove(e.getKey()); // Remove current 'x' from copy map

                                         // Avoid "null pointer exception"
                                         if (copy.computeIfAbsent(foldCoordinate - e.getKey() + foldCoordinate, set -> new HashSet<>()) != null) {
                                            copy.get(foldCoordinate - e.getKey() + foldCoordinate).addAll(e.getValue());
                                         }

                                     });
        }

        else { // Fold along 'y'
            coordinatesMap.entrySet().stream()
                                     .forEach(entry -> {
                                         entry.getValue().stream()
                                                .filter(y -> y > foldCoordinate) // Get all 'y' greater than fold coord
                                                .forEach(y -> {
                                                    copy.get(entry.getKey()).remove(y); // Remove current 'y' from copy map
                                                    copy.get(entry.getKey()).add(foldCoordinate - y + foldCoordinate);
                                                });
                                     });
            
        }

        // Print amount of cells of copy
        // System.out.println(copy.values().stream().mapToInt(set -> set.size()).sum());

        coordinatesMap = new HashMap<>(copy);
    }
}

public class App {
    public static void main(String[] args) throws Exception {
        Day13 object = new Day13();
        object.readFromFile();
    }
}
