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

/* Algorithm of input processing:
    For each line in input file:
        Initialize firstNode as a first half of line
        Initialize secondNode as a second half of line

        Add secondNode to firstNode's adjacency list
        Add firstNode to secondNode's adjacency list
    
    Mark all small caves as unvisited before DFS traversal

    For each neighbour of "start" node:
        Start part 1 traversal from neighbour with a DEEP COPY of nodesVisitsAmount created to avoid changing the original map during traversal

    For each neighbour of "start" node:
        Start part 2 traversal from neighbour with a DEEP COPY of nodesVisitsAmount created to avoid changing the original map during traversal

    Print part 1 and part 2 answers
*/


/* Algorithm of partOneDFS(currentNode, visitedNodes, currentPath):
    Add current node to current path

    If current node is "end":
        Add current path to set of paths
        Print the current path
        Return to caller node

    Else if current node is small cave:
        Mark current node as visited (small caves can only be visited once)

    For each neighbour of current node:
        If neighbour isn't "start" node && (neighbour isn't visited or is a big cave):
            Call DFS on that neighbour with a DEEP COPY of visited nodes (visited nodes may be different for each path)
*/


/* Algorithm of partTwoDFS(currentNode, nodesVisitsAmount, currentPath, someNodeIsAlreadyVisitedTwice):
    Add current node to current path

    If current node is "end":
        Add current path to set of paths
        Print the current path
        Return to caller node

    Else if current node is small cave:
        Add 1 to its amount of visits

        If amount of visits of current node >= 2 && there is a node that has already been visited twice:
            Return (ignore this node)

        Else if amount of visits of current node == 2 && there is NO any node that has already been visited twice:
            Change flag to true

    For each neighbour of current node:
        If neighbour isn't "start" node && 
        (neighbour is a big cave or isn't visited || 
         (neighbour is a small cave and is visited once and there is no yet any node visited twice) ):
            Call DFS on that neighbour with a DEEP COPY of visited nodes (visited nodes may be different for each path)

*/

class Day12 {
    private static Map<String, ArrayList<String>> adjacencyList = new HashMap<>(); // Node -> its adjacent nodes
    private static Set<String> partOnePaths = new HashSet<>();
    private static Set<String> partTwoPaths = new HashSet<>();


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
                // Only AFTER FIRST NODE'S ADDITION second node is added to first node's adjacency list.
                if (adjacencyList.putIfAbsent(firstNode, new ArrayList<>(List.of(secondNode))) != null) {
                    adjacencyList.get(firstNode).add(secondNode);
                }

                if (adjacencyList.putIfAbsent(secondNode, new ArrayList<>(List.of(firstNode))) != null) {
                    adjacencyList.get(secondNode).add(firstNode);
                }
            });


            // Mark all lowercase nodes (small caves) with amount of visits as 0
            Map<String, Integer> nodesVisitsAmount = adjacencyList.keySet().stream()
                                                                           .filter(key -> key.equals(key.toLowerCase()) && 
                                                                                          !key.equals("start") && 
                                                                                          !key.equals("end"))
                                                                           .collect(Collectors.toMap(key -> key, value -> 0));


            for (String neighbour : adjacencyList.get("start")) {
                partOneDFS(neighbour, new HashMap<>(nodesVisitsAmount), "start");
            }

            for (String neighbour : adjacencyList.get("start")) {
                partTwoDFS(neighbour, new HashMap<>(nodesVisitsAmount), "start", false);
            }

            // Print part 1 and 2 answers
            System.out.println("Total amount of part 1 paths: " + partOnePaths.size());
            System.out.println("Total amount of part 2 paths: " + partTwoPaths.size());
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Performs DFS traversal of current node based on part 1 rules.
     * @param currentNode name of current node
     * @param nodesVisitsAmount list of small caves that are marked as visited or not
     * @param currentPath path with current node added to it
     */
    private void partOneDFS(String currentNode, Map<String, Integer> nodesVisitsAmount, String currentPath) {
        currentPath += "," + currentNode;

        if (currentNode.equals("end")) {
            partOnePaths.add(currentPath);
            System.out.println("Current path: " + currentPath);
            return ;
        }

        else if (currentNode.equals(currentNode.toLowerCase())) { // If current cave is small
            nodesVisitsAmount.put(currentNode, nodesVisitsAmount.getOrDefault(currentNode, 0) + 1);
        }

        for (String neighbour : adjacencyList.get(currentNode)) {
            // If neighbour isn't "start" node && isn't visited or a big cave - move to this neighbour
            if (!neighbour.equals("start") && nodesVisitsAmount.getOrDefault(neighbour, 0) == 0) {
                partOneDFS(neighbour, new HashMap<>(nodesVisitsAmount), currentPath);
            }
        }
    }


    /**
     * Performs DFS traversal of current node based on part 2 rules.
     * @param currentNode name of current node
     * @param nodesVisitsAmount amount of visits of all nodes in graph
     * @param currentPath path with current node added to it
     * @param someNodeIsAlreadyVisitedTwice true if some node on the current path has already been visited twice. Else false.
     */
    private void partTwoDFS(String currentNode, Map<String, Integer> nodesVisitsAmount, String currentPath, boolean someNodeIsAlreadyVisitedTwice) {
        currentPath += "," + currentNode;

        if (currentNode.equals("end")) {
            partTwoPaths.add(currentPath);
            System.out.println("Current path: " + currentPath);
            return ;
        }

        else if (currentNode.equals(currentNode.toLowerCase())) { // If current cave is small
            nodesVisitsAmount.put(currentNode, nodesVisitsAmount.get(currentNode) + 1);
            int currentNodeVisits = nodesVisitsAmount.get(currentNode);

            // If some node has already been visited twice on the current path &&
            // amount of visits of current node is more than 2 - stop current node processing
            if (currentNodeVisits >= 2 && someNodeIsAlreadyVisitedTwice) {
                return ;
            }

            // Else it means that current node is the node that has been visited twice; mark flag as true
            if (currentNodeVisits == 2 && !someNodeIsAlreadyVisitedTwice) {
                someNodeIsAlreadyVisitedTwice = true;
            }
        }

        for (String neighbour : adjacencyList.get(currentNode)) {
            // If neighbour isn't "start" node && 
            // (neighbour isn't visited or is a big cave || 
            //  neighbour is a small cave and visited once and there is no any node visited twice up to now) - move to this neighbour
            if (!neighbour.equals("start") && 
                (nodesVisitsAmount.getOrDefault(neighbour, 0) == 0 || 
                (nodesVisitsAmount.getOrDefault(neighbour, 1) == 1 && !someNodeIsAlreadyVisitedTwice))) {
                    partTwoDFS(neighbour, new HashMap<>(nodesVisitsAmount), currentPath, someNodeIsAlreadyVisitedTwice);
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
