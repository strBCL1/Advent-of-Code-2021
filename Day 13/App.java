import java.nio.file.Files;
import java.nio.file.Paths;
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

    public void readFromFile() {
        try (Stream<String> inputStream = Files.lines(Paths.get(Day13.class.getResource("/input.txt").toURI()))) { 
            inputStream.forEach(line -> {
                if (line.isEmpty()) { // Ignore 1 blank row
                    return ;
                }

                if (line.contains("fold")) { // If it is a fold procedure
                    Matcher matcher = Pattern.compile("[0-9]+").matcher(line);
                    matcher.find();

                    int coordinate = Integer.parseInt(matcher.group()); // Get the coordinate
                    boolean foldAlongX = line.contains("x="); // Check if coordinate is 'x' or 'y'
                    foldSheet(coordinate, foldAlongX); // Fold sheet

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

            System.out.println(coordinatesMap);
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void foldSheet(int coordinate, boolean foldAlongX) {
        Map<Integer, Set<Integer>> copy = new HashMap<>(coordinatesMap);

        // Create deep copy of all sets of map
        for (Entry<Integer, Set<Integer>> e : coordinatesMap.entrySet()) {
            copy.put(e.getKey(), new HashSet<>(e.getValue()));
        }
        
        if (foldAlongX) { // Fold along 'x'
            coordinatesMap.entrySet().stream()
                                     .filter(e -> e.getKey() > coordinate) // Get all 'x' greater than fold coord
                                     .forEach(e -> {
                                         // Avoid "null pointer exception"
                                         if (copy.computeIfAbsent(coordinate - e.getKey() + coordinate, set -> new HashSet<>()) != null) {
                                            copy.get(coordinate - e.getKey() + coordinate).addAll(e.getValue());
                                         }

                                         copy.remove(e.getKey()); // Remove current 'x' from copy map
                                     });
        }

        else { // Fold along 'y'
            coordinatesMap.entrySet().stream()
                                     .forEach(entry -> {
                                         entry.getValue().stream()
                                                .filter(y -> y > coordinate) // Get all 'y' greater than fold coord
                                                .forEach(y -> {
                                                    copy.get(entry.getKey()).remove(y); // Remove current 'y' from copy map
                                                    copy.get(entry.getKey()).add(coordinate - y + coordinate);
                                                });
                                     });
            
        }

        // Print amount of cells of copy
        System.out.println(copy.values().stream().mapToInt(set -> set.size()).sum());

        coordinatesMap = new HashMap<>(copy);
    }
}

public class App {
    public static void main(String[] args) throws Exception {
        Day13 object = new Day13();
        object.readFromFile();
    }
}
