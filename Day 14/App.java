import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/* Solution methods copied from Reddit user "Natrium_Benzoat".
 * https://topaz.github.io/paste/#XQAAAQBgDgAAAAAAAAA0m0pnuFI8c9/wau+qtGVPyaqZd0m0oqd+WNK5FEOTExU/ASOFLgLNP5psw5/hebgHEk3uxIWrhzqs84ArCMACz8U4/RPQNfrk0ena7c7CQMPhAxelssPPBx5uxM4eKhwmETcud/IPuoU57yBRs6sxyamixa3MvlVEU1YlEO0uCqQB3fp1oeDQtH1CIWSn1fNbAOuQvF4R09bSlNQ7pZCl4/pkrwVITUoxW+RzmlhLqNSqNgLnmve4ngmmoKUOwME9okQNlx2qmpq24mfDOB+2SHHUoVZXssMUVILfeND/r4L+WFqSb5Q6pYadI7/1TNjxPAClF5GS5DyUZyTByd+vF4r7NDnjfg9gbBvZpdg4OjAQD/GNilDj2w0REI9iBImEBVNtw1l4gZlzEBEsbbTRgxDA7e5rbp+6bxnKK4Qagb2cfU/pM1rou5QchIBv/xoxqFC/Jupp47qmwq9VfdiqcGiQM9PtfhoPwZ2mj51tY0vyLIZTurUka1fWIF8ju/vptsXGNHddsY/UbXSTxDDWXxRNYq/tBH+bd3zix3Nx7hNyApDCYiWrYTD2SSu22CeaNOG2nBe82g8xDpSTlMvBfT49BJ957NIWhJvUmdv8wlqBHKV3OxlhjbwpCl+VwuN+SnPkoTtOcD5RxHDv07h+FG3x4MT6QmDnkah2Jhb49ugTIDxMshF+OzONbWvKrZix2SM+OdoM0Xze9cJh7hJsH8fOzmAGgLM8RJAJzQS9eLkuhz1+ERZn4cXCfWfZkoLp9C5MPDS5PXNOineSs6huhr7aStWhHy+RI/OjGUdMh2rn6hQ9S1jUKzzNsDnb7YYm8UnItL6FMTRjvl3e612khVmzW5eDhzFgCKDJeydcEG30MM0Gd9GB2pSXm8nOcyARNU8WKwJQ1sw3+Aeu4bQXZRcAR6/KdJOSiHWZpQ1bSp2rTaEsYeZJwrXFi686LCO6Vgrs4j6AqQt8wjRgNMTxMagg6fmxOqCNHjvBH2UORl2EIqY2TZG1fsXggKNOoMNVhz+gbNRYerpVllccYLIPFOyk1luL3pM2gG5n1M6psNlPda7t8O5Fq1Bpf6bMOOWOwugAab1Jpeab9kR6zMnUB4/s2a29/xhP5AGDpFzBuNqSyD40NlgXdVqj5QA0uUJMIQ5lyT2YjiG3iPFiyqUBkh8YZyluc9+FYrLneF9+PemcLHN8iiH5bObxY6AacGOfx8AaZgtGInNkw8Xg4usGqb0dUt0vo080Qs+qF8qIunqchy9gmK2ivfFEVeNSX+2hU4z26whbrHJGTb4TZuS5sDRjVDucRQYiXhOQ41pKiQEQvCZJXHLtA+IZ6HgJlvipt4Lzxs5++SCWVf1cDpwo1peVdb3AnCJIJ4SMwd85LUe2v/h9B7PuPNWyyI6rOfR+c9NxXdXG/gCrL8c3/1LxmJohfDsV1DBl5Evn//fQl64=
 * Contains explanations.
 */

class Day14 {
	// Map of input pairs, e.g. HN -> B results in HN -> {HB, BN}
	private static Map<String, String[]> insertionsPairs = new HashMap<>();
	
	// Amount of occurrences of each pair in template, e.g. LL: 0, HB: 1, ...
	private static Map<String, Long> templateMap = new HashMap<>();
	
	// Occurrences of each letter in template
	private static Map<Character, Long> charOccurrences = new HashMap<>(); 
	
	private static final int AMOUNT_OF_STEPS = 40;
	
	
	/**
	 * Reads and creates template and insertion pairs from text file.
	 */
	public void readFromFile() {
		try (Stream<String> inputStream = Files.lines(Path.of(Day14.class.getResource("/input.txt").toURI()))) {
			inputStream.forEach(line -> {
				if (line.isEmpty()) { // Ignore blank row
					return ;
				}
				
				if (templateMap.isEmpty()) { // Create template if it is empty
					// Add 1 to first template char's occurrences
					// It won't be processed during chars occurrences' counting
					charOccurrences.put(line.charAt(0), 1L);
					
					for (int i = 0; i < line.length() - 1; ++i) {
						String pair = line.substring(i, i + 2);
						
						// Pairs may repeat
						templateMap.put(pair, templateMap.getOrDefault(pair, 0L) + 1);
					}
					
					return ;
				}
				
				// Initialize insertion pairs, e.g. NH -> B results in NH -> {NB, BH}
				String[] parts = line.split(" -> ");
				String leftPart = parts[0];
				String rightPart = parts[1];
				
				String[] insertion = {leftPart.charAt(0) + rightPart, rightPart + leftPart.charAt(1)};
				
				insertionsPairs.put(leftPart, insertion);
			});
			
			
			for (int step = 0; step < AMOUNT_OF_STEPS; ++step) {				
				updateTemplate();
			}
			
			
			countCharsOccurrences();
		}
		
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * Inserts new pairs into current template and updates pairs' occurrences.
	 */
	private void updateTemplate() {
		Map<String, Long> newTemplateMap = new HashMap<>();
		
		for (String templateKey : templateMap.keySet()) {
			// Occurrences of current template key 
			Long keyOccurrs = templateMap.get(templateKey);
			
			// Pairs to be inserted into new template
			String firstPair = insertionsPairs.get(templateKey)[0];
			String secondPair = insertionsPairs.get(templateKey)[1];
			
			// Occurrences of pairs to be inserted 
			Long firstPairOccurences = newTemplateMap.getOrDefault(firstPair, 0L);
			Long secondPairOccurences = newTemplateMap.getOrDefault(secondPair, 0L);
			
			// Put extracted pairs and their amounts to new template
			newTemplateMap.put(firstPair, keyOccurrs + firstPairOccurences);
			newTemplateMap.put(secondPair, keyOccurrs + secondPairOccurences);
		}
		
		// Reassign current template
		templateMap = newTemplateMap;
	}

	
	/**
	 * Counts occurrences of each letter in template.
	 * Calculates max and min occurrences' difference.
	 */
	private void countCharsOccurrences() {		
		for (String pair : templateMap.keySet()) {
			char c = pair.charAt(1);
			long pairOccurences = templateMap.get(pair);
			
			long newPairOccurences = charOccurrences.getOrDefault(c, 0L);
			charOccurrences.put(c, pairOccurences + newPairOccurences);
		}		
		
		long maxValue = Long.MIN_VALUE;
		long minValue = Long.MAX_VALUE;
		
		for (long occurence : charOccurrences.values()) {
			maxValue = Math.max(maxValue, occurence);
			minValue = Math.min(minValue, occurence);
		}
		
		System.out.println("Difference between max occurrence and min occurrence: " + (maxValue - minValue));
	}
}


public class App {
    public static void main(String[] args) {
        Day14 object = new Day14();
        object.readFromFile();
    }
}