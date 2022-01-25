import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.stream.Stream;

class Solution {
	private static List<List<Point>> grid = new LinkedList<>();
	private static int lineCounter = 0;
	
	public void readFromFile() {
		try (Stream<String> inputStream = Files.lines(Path.of(Solution.class.getResource("/input.txt").toURI()))) {
			inputStream.forEach(line -> {
				List<Point> currentLine = new LinkedList<>();
				
				for (int i = 0; i < line.length(); ++i) {
					currentLine.add(new Point(line.charAt(i) - '0', lineCounter, i));
				}
				
				lineCounter++;
				grid.add(currentLine);
			});
			
			
//			printGrid();
			findMinPath();
		}
		
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private void findMinPath() {
		Queue<Point> openedList = new PriorityQueue<>();
		List<Point> closedList = new LinkedList<>();
		openedList.add(grid.get(0).get(0));
		
		while (!openedList.isEmpty()) {
			var cell = openedList.poll();
			
			// Mark cell as visited
			closedList.add(cell);
			
			// If cell is goal cell
			if (cell.y == grid.size() - 1 && 
				cell.x == grid.get(0).size() - 1) {
					System.out.println("The path has been found!");
					closedList.forEach(p -> System.out.println(p));
					
					int sum = 0;
					for (var s : closedList) {
						sum += s.riskLevel;
					}
					
					System.out.println(sum);
//					System.out.println(closedList.size());
					return ;
			}
			
			List<Point> neighbours = getNeighboursOf(cell);
			
			neighbours.forEach(successor -> {
				successor.h = calculateHeuristic(successor);
				successor.g = cell.g + successor.riskLevel;
				successor.f = successor.h + successor.g;
				
				for (var s : openedList) {
					if (s.y == successor.y && s.x == successor.x && s.f <= successor.f) {
						return ;
					}
				}
				
				for (var s : closedList) {
					if (s.y == successor.y && s.x == successor.x && s.f <= successor.f) {
						return ;
					}
				}
				
				openedList.remove(successor);
				closedList.remove(successor);
				
//				openedList.add(successor);
				openedList.add(new Point(successor));
			});
		}
	}
	
	
	private int calculateHeuristic(Point successor) {
		return (grid.size() - 1 - successor.y) + (grid.get(0).size() - 1 - successor.x);
	}

	
	private List<Point> getNeighboursOf(Point cell) {
		List<Point> neighbours = new LinkedList<>();
		
		int y = cell.y;
		int x = cell.x;
		
		if (isValid(y - 1, x)) {
			neighbours.add(grid.get(y - 1).get(x));
		}
		
		if (isValid(y + 1, x)) {
			neighbours.add(grid.get(y + 1).get(x));
		}
		
		if (isValid(y, x - 1)) {
			neighbours.add(grid.get(y).get(x - 1));
		}
		
		if (isValid(y, x + 1)) {
			neighbours.add(grid.get(y).get(x + 1));
		}
		
		return neighbours;
	}


	private void printGrid() {
		grid.forEach(line -> System.out.println(line));
	}
	
	
	boolean isValid(int y, int x) {
		return y >= 0 && y < grid.size() && x >= 0 && x < grid.get(0).size(); 
	}
}
