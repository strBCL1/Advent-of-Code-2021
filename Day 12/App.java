import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*
Algorithm of input processing:
    For each line in input file:
        Initialize firstNode as a first half of line
        Initialize secondNode as a second half of line

        Add secondNode to firstNode's adjacency list
        Add firstNode to secondNode's adjacency list
    
    Mark all small caves as unvisited before DFS traversal

    For each neighbour of "start" node:
        Start DFS traversal from current neighbour with a SHALLOW COPY of visitedNodes to save memory


Algorithm of DFS(currentNode, visitedNodes, currentPath):
    Add current node to current path

    If current node is "end":
        Add current path to set of paths
        Print the current path
        Return to caller node

    Else if current node is small cave:
        Mark current node as visited (small caves can only be visited once)

    For each neighbour of current node:
        If neighbour isn't "start" node && neighbour isn't visited or is a big cave:
            Call DFS on that neighbour with a DEEP COPY of visited nodes (visited nodes may be different for each path)
*/

class Day12 {
    private static Map<String, ArrayList<String>> adjacencyList = new HashMap<>(); // Node -> its adjacent nodes
    private static Set<String> paths = new HashSet<>(); // Contains distinct paths


    /**
     * Reads nodes from text file, creates adjacency list and traverses it using DFS traversal.
     */
    public void readFromFile() {
        try (Stream<String> inputStream = Files.lines(Paths.get(Day12.class.getResource("input.txt").toURI()))) {
            inputStream.forEach(line -> {
                String[] halfes = line.split("-");
                String firstNode = halfes[0];
                String secondNode = halfes[1];

                // If first node isn't in adjacency list, it is put into adj. list. 
                // Only AFTER FIRST NODE'S ADDITION second nodes is added to first node's adjacency list.
                if (adjacencyList.putIfAbsent(firstNode, new ArrayList<>(List.of(secondNode))) != null) {
                    adjacencyList.get(firstNode).add(secondNode);
                }

                if (adjacencyList.putIfAbsent(secondNode, new ArrayList<>(List.of(firstNode))) != null) {
                    adjacencyList.get(secondNode).add(firstNode);
                }
            });


            // Mark all lowercase nodes (small caves) as unvisited
            Map<String, Boolean> visitedNodes = adjacencyList.keySet().stream()
                                                .filter(key -> key.equals(key.toLowerCase()) && !key.equals("start") && !key.equals("end"))
                                                .collect(Collectors.toMap(key -> key, value -> false));

            // Start DFS traversal from each neighbour of "start" cave
            for (String neighbour : adjacencyList.get("start")) {
                DFS(neighbour, visitedNodes, "start");
            }

            // Print part 1 answer
            System.out.println("Total amount of paths: " + paths.size());
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Performs DFS traversal of current node.
     * @param currentNode name of current node
     * @param visitedNodes list of small caves that are marked with visited or not
     * @param currentPath path with current node added to it
     */
    private void DFS(String currentNode, Map<String, Boolean> visitedNodes, String currentPath) {
        currentPath += "," + currentNode;

        if (currentNode.equals("end")) {
            paths.add(currentPath);
            System.out.println("Current path: " + currentPath);
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