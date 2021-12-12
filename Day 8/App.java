import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Day8 {
    private Map<Integer, Integer> m = Stream.of(new Object[][] {
        {1, 2}, 
        {4, 4}, 
        {7, 3},
        {8, 7},
        }).collect(Collectors.toMap(data -> (Integer) data[0], data -> (Integer) data[1]));

    public int readFromFile() {
        try (BufferedReader bf = new BufferedReader(new FileReader("/home/mertens/Рабочий стол/VSCode Workspace/Java Language/Advent-of-Code-2021/Day 8/src/input.txt"))) {
            String currentLine = "";
            int counter = 0;

            while ((currentLine = bf.readLine()) != null) {
                boolean flag = false;
                for (String s : currentLine.split(" ")) {
                    if (s.equals("|")) {
                        flag = true;
                    }
                    else if (flag == true) {
                        for (int length : m.values()) {
                            if (s.length() == length) {
                                counter++;
                                break;
                            }
                        }
                    }
                }
            }
            
            return counter;
        }

        catch (Exception e) { }

        return 0;
    }
}

public class App {
    public static void main(String[] args) throws Exception {
        Day8 object = new Day8();
        System.out.println(object.readFromFile());

    }
}