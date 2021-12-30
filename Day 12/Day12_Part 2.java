import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

/* Algorithm
Создать граф (adj. list)

DFS(stack, visited, currentPath):
    Если стак пустой:
        Выписать кол-во путей
        Вернуть

    Текущая вершина = стек.pop()

    Если текущая вершина == "старт":
        Для всех соседей "старт": 
            Добавить соседа в стак
            DFS(stack, visited, currentPath)
        Вернуть

    Добавить текущую вершину в текущий путь

    Если текущая вершина == "конец":
        Выписать текущий путь
        Кол-во путей += 1
        Вернуть / DFS(q, visited, currentPath)

    Если текущая вершина маленькая:
        Если текущая вершина посещена:
            Вернуть
        Пометить как посещенную

    Для всех соседей текущей вершины:
        Если сосед != "старт" и сосед не посещен:
            Добавить соседа в стак
            DFS(stack, visited, currentPath)
*/

/* Algorithm part 2:
DFS(stack, visitedAmount, currentPath, presentVisitedTwice):
    Если стак пустой:
        Выписать кол-во путей
        Вернуть

    Текущая вершина = стек.pop()

    Если текущая вершина == "старт":
        Для всех соседей "старт": 
            Добавить соседа в стак
            DFS(stack, visitedAmount, "start", false)
        Вернуть

    Добавить текущую вершину в текущий путь

    Если текущая вершина == "конец":
        Выписать текущий путь
        Добавить текущий путь в список путей
        DFS(stack, visitedAmount, currentPath, presentVisitedTwice)

    Если текущая вершина маленькая:
        Кол-во посещений текущей вершины += 1

        // Если вышел на самого себя (вершину, посещенную более 2-х раз) - вернуть
        Если presentVisitedTwice == true && кол-во посещений текущей вершины > 2:
            Вернуть

        // Если кол-во посещений текущей вершины == 2, но еще нету вершины, посещенной два раза - сделать текущую вершину посещенной два раза
        Если presentVisitedTwice == false && кол-во посещений текущей вершины == 2:
            presentVisitedTwice = true
        
        Если кол-во посещений текущей вершины == 1:
            Пометить текущую вершину как посещенную


    Для всех соседей текущей вершины:
        Если сосед != "старт" || сосед большой || сосед == "конец" || (сосед посещен и presentVisitedTwice == false):
            Добавить соседа в стак
            DFS(stack, visited, currentPath)
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
        try (BufferedReader br = new BufferedReader(new FileReader("/home/mertens/VSCode Workspace/Java Language/Advent-of-Code-2021/Day 12/input.txt"))) {
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


            // adjList.entrySet().forEach(e -> System.out.println(e.getKey() + " " + e.getValue())); System.out.println(); // Print adjacency list
            // Map<String, Boolean> visited = adjList.keySet().stream()
            //                                .filter(key -> !key.equals("start") && !key.equals("end") && key.equals(key.toLowerCase()))
            //                                .collect(Collectors.toMap(key -> key, value -> false));

            // visited.entrySet().forEach(e -> System.out.println(e.getKey() + " " + e.getValue())); // Print visited nodes

            Map<String, Integer> visitedAmount = adjList.keySet().stream()
                                                .filter(key -> !key.equals("start") && !key.equals("end") && key.equals(key.toLowerCase()))
                                                .collect(Collectors.toMap(key -> key, value -> 0));

            Queue<String> stack = Collections.asLifoQueue(new ArrayDeque<>());
            stack.add("start");
            DFS(stack, visitedAmount, "start", false);
        }

        catch (Exception e) {
            e.printStackTrace();
            paths.forEach(System.out::println);
        }
    }


    private void DFS(Queue<String> stack, Map<String, Integer> visitedAmount, String currentPath, boolean presentVisitedTwice) {
        if (stack.isEmpty()) {
            System.out.println("Amount of paths: " + paths.size());
            return ;
        }

        String currentNode = stack.poll();

        if (currentNode.equals("start")) {
            for (String neighbour : adjList.get("start")) {
                stack.add(neighbour);
                DFS(stack, new HashMap<>(visitedAmount), "start", false);
            }
            return ;
        }

        currentPath += "," + currentNode;

        if (currentNode.equals("end")) {
            System.out.println("Current path: " + currentPath);
            paths.add(currentPath);
            DFS(stack, new HashMap<>(visitedAmount), currentPath, presentVisitedTwice);
            return ;
        }

        if (currentNode.equals(currentNode.toLowerCase())) {
            visitedAmount.put(currentNode, visitedAmount.get(currentNode) + 1);
            int visitedTimes = visitedAmount.get(currentNode);

            if (presentVisitedTwice == true && visitedTimes > 2) {
                return ;
            }

            if (presentVisitedTwice == false && visitedTimes == 2) {
                presentVisitedTwice = true;
            }
        }

        for (String neighbour : adjList.get(currentNode)) {
            if (!neighbour.equals("start") && (visitedAmount.getOrDefault(neighbour, 0) == 0 || neighbour.equals("end") || 
                (visitedAmount.getOrDefault(neighbour, 1) == 1 && presentVisitedTwice == false))) {
                    stack.add(neighbour);
                    DFS(stack, new HashMap<>(visitedAmount), currentPath, presentVisitedTwice);
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
