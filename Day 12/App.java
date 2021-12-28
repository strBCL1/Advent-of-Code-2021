import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

/* Algorithm
Создать граф (adj. list)

BFS(q, visited, currentPath):
    Если очередь пустая:
        Выписать кол-во путей
        Вернуть

    Если текущая вершина == "старт":
        Добавить всех ее соседей в очередь
        Продолжить итерацию

    Если текущая вершина == "конец":
        Выписать текущий путь
        Кол-во путей += 1
        Продолжить итерацию

    Если текущая вершина маленькая:
        Если текущая вершина посещена:
            Вернуть
        Пометить как посещенную

    Добавить всех соседей вершины в очередь
    BFS(q, visited, currentPath)
*/

/* Solution in pseudocode

Adj. list:
start = ['A', 'b']
A = ['c', 'start', 'b', 'end']
b = ['A', 'start', 'd', 'end']
c = ['A']
d = ['b']
end = ['A', 'b']


Visited: (HashMap)
c = false
b = false
d = false
*/

class Day12 {
    private static Map<String, ArrayList<String>> adjList = new HashMap<>();
    private static Set<String> paths = new HashSet<>(); // Set of distinct paths


    public void readFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader("/home/mertens/VSCode Workspace/Java Language/Advent-of-Code-2021/Day 12/src/input.txt"))) {
            String currentLine = "";
            
            while ((currentLine = br.readLine()) != null) {
                String[] halfes = currentLine.split("-");
                String firstNode = halfes[0];
                String secondNode = halfes[1];

                if (adjList.containsKey(firstNode)) {
                    adjList.get(firstNode).add(secondNode);
                }
                else {
                    adjList.put(firstNode, new ArrayList<>(List.of(secondNode)));
                }

                if (adjList.containsKey(secondNode)) {
                    adjList.get(secondNode).add(firstNode);
                }
                else {
                    adjList.put(secondNode, new ArrayList<>(List.of(firstNode)));
                }

            }


            // adjList.entrySet().forEach(e -> System.out.println(e.getKey() + " " + e.getValue())); System.out.println();
            Map<String, Boolean> visited = adjList.keySet().stream()
                                           .filter(key -> !key.equals("start") && !key.equals("end") && key.equals(key.toLowerCase()))
                                           .collect(Collectors.toMap(key -> key, value -> false));

            // visited.entrySet().forEach(e -> System.out.println(e.getKey() + " " + e.getValue())); // Print visited nodes


            Queue<String> q = new ArrayDeque<>();
            q.add("start");
            BFS(q, visited, "start");
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void BFS(Queue<String> q, Map<String, Boolean> visited, String currentPath) {
        if (q.isEmpty()) {
            System.out.println("Amount of paths: " + paths.size());
            return ;
        }

        String currentNode = q.poll();
        
        if (currentNode.equals("start")) { // If current node is "start"
            for (String neighbour : adjList.get(currentNode)) {
                q.add(neighbour);
            }
            BFS(q, visited, currentPath);
            return ;
        }

        currentPath += "," + currentNode; // Add current node to path

        if (currentNode.equals("end")) { // If current node is "end"
            paths.add(currentPath);
            System.out.println("Current path: " + currentPath);
            return ;
        }
        

        // Change current node to visited if it's lowercase node
        if (currentNode.equals(currentNode.toLowerCase()) && !visited.get(currentNode)) {
            visited.computeIfPresent(currentNode, (key, value) -> true);
        }

        // Add neighbours to queue
        for (String neighbour : adjList.get(currentNode)) {
            if (!neighbour.equals("start") && visited.getOrDefault(neighbour, false) == false) {
                q.add(neighbour);
            }
        }
        
        BFS(q, visited, currentPath);
    }
}


public class App {
    public static void main(String[] args) throws Exception {
        Day12 object = new Day12();
        object.readFromFile();
    }
}
