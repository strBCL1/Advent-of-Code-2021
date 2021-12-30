import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


class Day12 {
    private static Map<String, ArrayList<String>> adjacencyList = new HashMap<>();
    private static Set<String> paths = new HashSet<>(); // Set of distinct paths


    public void readFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader("/home/mertens/VSCode Workspace/Java Language/Advent-of-Code-2021/Day 12/input.txt"))) {
            String currentLine = "";
            
            while ((currentLine = br.readLine()) != null) {
                String[] halfes = currentLine.split("-");
                String firstNode = halfes[0];
                String secondNode = halfes[1];

                if (adjacencyList.putIfAbsent(firstNode, new ArrayList<>(List.of(secondNode))) != null) {
                    adjacencyList.get(firstNode).add(secondNode);
                }

                if (adjacencyList.putIfAbsent(secondNode, new ArrayList<>(List.of(firstNode))) != null) {
                    adjacencyList.get(secondNode).add(firstNode);
                }

            }


            Map<String, Boolean> visitedNodes = adjacencyList.keySet().stream()
                                                .filter(key -> key.equals(key.toLowerCase()) && !key.equals("start") && !key.equals("end"))
                                                .collect(Collectors.toMap(key -> key, value -> false));


            DFS("start", visitedNodes, "");

            System.out.println(paths.size());
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void DFS(String currentNode, Map<String, Boolean> visitedNodes, String currentPath) {
        if (!currentPath.isEmpty()) {
            currentPath += "," + currentNode;
        }

        if (currentNode.equals("start")) {
            for (String neighbour : adjacencyList.get("start")) {
                DFS(neighbour, new HashMap<>(visitedNodes), "start");
            }
            return ;
        }

        else if (currentNode.equals("end")) {
            paths.add(currentPath);
            System.out.println(currentPath);
            return ;
        }

        else if (currentNode.equals(currentNode.toLowerCase())) { // If current cave is small
            visitedNodes.put(currentNode, true);
        }

        for (String neighbour : adjacencyList.get(currentNode)) {
            if (!neighbour.equals("start") && visitedNodes.getOrDefault(neighbour, false) == false) {
                DFS(neighbour, new HashMap<>(visitedNodes), currentPath);
            }
        }
    }
}


public class App {
    public static void main(String[] args) throws Exception {
        Day12 object = new Day12();
        object.readFromFile();
    }
}