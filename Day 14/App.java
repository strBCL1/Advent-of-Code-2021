import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*
Queue for processing, LinkedList for template storing, Stack for processing

*/

class Day14 {
    // Sorted by keys
    private static Map<String, String> pairInsertions = new HashMap<>();
    private static List<Character> template;


    public void readFromFile() {
        try (Stream<String> inputLines = Files.lines(Path.of(Day14.class.getResource("/input.txt").toURI()))) {
            inputLines.forEach(line -> {
                if (line.isEmpty()) {
                    return ;
                }

                if (template == null) {
                    template = line.chars()
                                   .mapToObj(num -> (char) num)
                                   .collect(Collectors.toCollection(LinkedList::new));

                    return ;
                }

                String[] pair = line.split(" -> ");
                pairInsertions.put(pair[0], pair[1]);
            });

            // processTemplate();
            System.out.println(template);
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }


    // private void processTemplate() {
    //     String copyTemplate = new StringBuilder(template).toString();

    //     for (int step = 0; step < 40; ++step) {
    //         int templateInsertIndex = 1;
    //         for (int i = 0; i < copyTemplate.length() - 1; ++i) {
    //             String pair = new StringBuilder().append(copyTemplate.charAt(i)).append(copyTemplate.charAt(i + 1)).toString();
    //             template.insert(templateInsertIndex, pairInsertions.get(pair));
    //             templateInsertIndex += 2;
    //         }
    //         copyTemplate = template.toString();
    //     }

    //     // System.out.println(template.toString());
    //     // Map<Character, Long> charOccurences = copyTemplate.chars()
    //     //                                                   .mapToObj(c -> (char) c)
    //     //                                                   .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    //     // // System.out.println(charOccurences);
    //     // var min = Long.MAX_VALUE;
    //     // var max = Long.MIN_VALUE;

    //     // for (var value : charOccurences.values()) {
    //     //     min = Math.min(value, min);
    //     //     max = Math.max(value, max);
    //     // }

    //     // System.out.println(max - min);
    //     System.out.println(template.length());
    // }
}

public class App {
    public static void main(String[] args) throws Exception {
        Day14 object = new Day14();
        object.readFromFile();
    }
}
