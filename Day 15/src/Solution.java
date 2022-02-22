// https://www.reddit.com/r/adventofcode/comments/rgqzt5/2021_day_15_solutions/
// User: clumsveed

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

public class Solution {
	private static List<String> lines = new LinkedList<>();
	private static int[][] riskMap;
	
	
	public static void part1() {
		int risk = evaluateRisk(riskMap);
		System.out.println("part 1: " + risk);
	}
	
	
	private static int evaluateRisk(int[][] riskMap) {
		// SHORTEST cost to go from every node to bottom-right corner
		int[][] riskSums = new int[riskMap.length][riskMap[0].length];
		
		// Initialize each path to bottom-right corner as 1000000 (for future min comparison)
		Arrays.stream(riskSums).forEach(row -> Arrays.fill(row, 1_000_000));
		
		// Initialize bottom-right corner as 0 (path to it equals to 0)
		riskSums[riskSums.length - 1][riskSums[0].length - 1] = 0;
		
		boolean changeMade = true;
		
		while (changeMade) {
			changeMade = false;
			for (int r = riskSums.length - 1; r >= 0; --r) {
				for (int c = riskSums[0].length - 1; c >= 0; --c) {
					// Four neighbours:
					// riskMap[r][c] : risk to enter this particular node
					// riskSums[r][c] : currently lowest risk to travel from this node to bottom-right corner
					
					int min = Integer.MAX_VALUE;
					
					if (r - 1 >= 0) {
						min = Math.min(min, riskMap[r - 1][c] + riskSums[r - 1][c]);
					}
					
					if (r + 1 < riskSums.length) {
						min = Math.min(min, riskMap[r + 1][c] + riskSums[r + 1][c]);
					}
					
					if (c - 1 >= 0) {
						min = Math.min(min, riskMap[r][c - 1] + riskSums[r][c - 1]);
					}
					
					if (c + 1 < riskSums[0].length) {
						min = Math.min(min, riskMap[r][c + 1] + riskSums[r][c + 1]);
					}
					
					// Check if change must be made
					int oldRisk = riskSums[r][c];
					riskSums[r][c] = Math.min(min, riskSums[r][c]);
					if (oldRisk != riskSums[r][c]) {
						changeMade = true;
					}
				}
			}
		}
		
		return riskSums[0][0];
	}
	

	private static void part2() {
		int[][] biggerMap = expandMap(riskMap);
		int risk = evaluateRisk(biggerMap);
		System.out.println("part 2: " + risk);
	}


	private static int[][] expandMap(int[][] riskMap) {
		int[][] newMap = new int[riskMap.length * 5][riskMap[0].length * 5];
		
		for (int r = 0; r < riskMap.length; ++r) {
			for (int c = 0; c < riskMap[0].length; ++c) {
				int oldVal = riskMap[r][c];
				
				for (int i = 0; i < 5; ++i) {
					for (int j = 0; j < 5; ++j) {
						int newVal = oldVal + i + j;
						if (newVal > 9) {
							newVal -= 9;
						}
						newMap[i * riskMap.length + r][j * riskMap[0].length + c] = newVal;
					}
				}
			}
		}
		return newMap;
	}


	private static void createRiskMap() {
		riskMap = new int[lines.size()][lines.get(0).length()];
		
		for (int i = 0; i < lines.size(); ++i) {
			String lineString = lines.get(i);
			
			for (int j = 0; j < lineString.length(); ++j) {
				riskMap[i][j] = lineString.charAt(j) - '0';
			}
		}
	}
	
	
	public static void main(String[] args) {
		try (Stream<String> inputStream = Files.lines(Path.of(Solution.class.getResource("/input.txt").toURI()))) {
			inputStream.forEach(line -> {
				lines.add(line);
			});
			
			createRiskMap();
			part1();
			part2();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
