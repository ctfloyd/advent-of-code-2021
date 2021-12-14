package adventofcode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DayFourteen extends AbstractAdventOfCode {

    private static int PART_1_POLYMERIZATION_STEPS = 10;
    private static int PART_2_POLYMERIZATION_STEPS = 40;

    @Override
    public Object solvePartOne() throws Exception {
        return solve(PART_1_POLYMERIZATION_STEPS);
    }

    @Override
    public Object solvePartTwo() throws Exception {
        return solve(PART_2_POLYMERIZATION_STEPS);
    }

    private long solve(int polymerizationSteps) throws Exception {
        List<String> lines = readInput("input/day14/data.txt");

        Map<String, Long> template = new HashMap<>();
        String templateStr = lines.get(0);
        for (int i = 1; i < templateStr.length(); i++) {
            String pair = "" + templateStr.charAt(i - 1) + templateStr.charAt(i);
            template.put(pair, template.getOrDefault(pair, 0L) + 1);
        }

        Map<String, String> insertionPatterns = new HashMap<>();
        for (int i = 2; i < lines.size(); i++) {
            String[] parts = lines.get(i).split(" -> ");
            insertionPatterns.put(parts[0], parts[1]);
        }

        Map<Character, Long> counts = new HashMap<>();
        for (int i = 0; i < polymerizationSteps; i++) {
            counts = polymerize(template, insertionPatterns);
        }
        // count the last letter which isn't counted in polymerize
        char last = templateStr.charAt(templateStr.length() - 1);
        counts.put(last, counts.get(last) + 1);

        long max = counts.values().stream().mapToLong(Long::longValue).max().orElseThrow();
        long min = counts.values().stream().mapToLong(Long::longValue).min().orElseThrow();

        return max - min;
    }

    private Map<Character, Long> polymerize(Map <String, Long> template, Map<String, String> insertionPatterns) {
        Map<String, Long> polymerizedPairs = new HashMap<>();
        Map<Character, Long> counts = new HashMap<>();

        for (Map.Entry<String, Long> pairEntry : template.entrySet()) {
            String pair = pairEntry.getKey();
            Long amount = pairEntry.getValue();

            if (insertionPatterns.containsKey(pair)) {
                String insertChar = insertionPatterns.get(pair);
                String leftPair = pair.charAt(0) + insertChar;
                String rightPair = insertChar + pair.charAt(1);
                polymerizedPairs.put(leftPair, polymerizedPairs.getOrDefault(leftPair, 0L) + amount);
                polymerizedPairs.put(rightPair, polymerizedPairs.getOrDefault(rightPair, 0L) + amount);

                counts.put(insertChar.charAt(0), counts.getOrDefault(insertChar.charAt(0), 0L) + amount);
                counts.put(pair.charAt(0), counts.getOrDefault(pair.charAt(0), 0L) + amount);
            } else {
                throw new IllegalStateException("No match for pair: " + pair);
            }
        }

        template.clear();
        template.putAll(polymerizedPairs);

        return counts;
    }

    public static void main(String[] args) {
        new DayFourteen().execute();
    }
}
