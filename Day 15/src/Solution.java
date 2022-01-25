import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.stream.Stream;

class Solution {
	private static List<List<Point>> grid = new ArrayList<>();
	private static Queue<Point> openedList = new PriorityQueue<>();
	private static List<Point> closedList = new ArrayList<>();
	private static int lineCounter = 0, sum = 0;
	private static Point nodeToPrint;
	char[][] characters;
	
	//TODO: все координаты родителей равны 0 в closedList
	public void readFromFile() {
		try (Stream<String> inputStream = Files.lines(Path.of(Solution.class.getResource("/input.txt").toURI()))) {
			inputStream.forEach(line -> {
				List<Point> currentLine = new ArrayList<>();
				
				for (int i = 0; i < line.length(); ++i) {
					currentLine.add(new Point(line.charAt(i) - '0', lineCounter, i));
				}
				
				lineCounter++;
				grid.add(currentLine);
			});
			
			
//			printGrid();
			characters = new char[grid.size()][grid.get(0).size()];
			findMinPath();
		}
		
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private void findMinPath() {
		openedList.add(grid.get(0).get(0));
		
		while (!openedList.isEmpty()) {
			Point cell = openedList.poll();
			
			// Mark cell as visited
			closedList.add(cell);

			// If cell is goal cell
			if (cell.y == grid.size() - 1 && cell.x == grid.get(0).size() - 1) {
					System.out.println("The path has been found!");
					for (int i = 0; i < grid.size(); ++i) {
						for (int j = 0; j < grid.get(0).size(); ++j) {
							characters[i][j] = '.';
						}
					}
					
					nodeToPrint = cell;
					traceBack();
					
					for (int i = 0; i < grid.size(); ++i) {
						for (int j = 0; j < grid.get(0).size(); ++j) {
							System.out.print(characters[i][j]);
						}
						System.out.println();
					}
					System.out.println(sum);
					return ;
			}
			
			List<Point> neighbours = getNeighboursOf(cell);
			
			neighbours.forEach(ns -> {
				var successor = new Point(ns);
				successor.parentY = cell.y;
				successor.parentX = cell.x;
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
				
				openedList.add(successor);
			});
		}
	}
	
	
	private void traceBack() {
		if (nodeToPrint.y == 0 && nodeToPrint.x == 0) {
			return ; 
		}
		characters[nodeToPrint.y][nodeToPrint.x] = '#';
		sum += nodeToPrint.riskLevel;
		
		for (Point nodePoint : closedList) {
			if (nodePoint.y == nodeToPrint.parentY && nodePoint.x == nodeToPrint.parentX) {
				nodeToPrint = nodePoint;

				traceBack();
			}
		}
	}


	private int calculateHeuristic(Point successor) {
		return (grid.size() - 1 - successor.y) + (grid.get(0).size() - 1 - successor.x);
	}

	
	private List<Point> getNeighboursOf(Point cell) {
		List<Point> neighbours = new ArrayList<>();
		
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
