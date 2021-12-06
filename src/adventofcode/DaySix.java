package adventofcode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DaySix extends AbstractAdventOfCode {

    private static final int PART1_NUMBER_OF_DAYS_TO_SIMULATE = 80;
    private static final int PART2_NUMBER_OF_DAYS_TO_SIMULATE = 256;
    private static final int PARENT_FISH_REPRODUCE_VALUE = 6;
    private static final int CHILD_FISH_REPRODUCE_VALUE = 8;

    @Override
    public Object solvePartOne() throws Exception {
        List<Integer> lanternFishInput = readInputAsCsvInts("input/day6/data.txt");
        return simulate(lanternFishInput, PART1_NUMBER_OF_DAYS_TO_SIMULATE);
    }

    @Override
    public Object solvePartTwo() throws Exception {
        List<Integer> lanternFishInput = readInputAsCsvInts("input/day6/data.txt");
        return simulate(lanternFishInput, PART2_NUMBER_OF_DAYS_TO_SIMULATE);
    }

    private long simulate(List<Integer> lanternFishInput, int numberOfDaysToSimulate) {
        HashMap<Integer, Long> lanternFish = new HashMap<>();
        for (Integer integer : lanternFishInput) {
            long currentFish = lanternFish.getOrDefault(integer, 0L);
            lanternFish.put(integer, currentFish + 1);
        }

        for (int i = 0; i < numberOfDaysToSimulate; i++) {
            lanternFish = simulateDay(lanternFish);
        }

        return lanternFish.values().stream().mapToLong(Long::longValue).sum();
    }

    private HashMap<Integer, Long> simulateDay(HashMap<Integer, Long> lanternFish) {
        HashMap<Integer, Long> newLanternFish = new HashMap<>();

        for (Map.Entry<Integer, Long> entry : lanternFish.entrySet()) {
            int daysTillReproduction = entry.getKey() - 1;
            long numberOfFish = entry.getValue();

            if (daysTillReproduction == -1) {
                newLanternFish.put(PARENT_FISH_REPRODUCE_VALUE, numberOfFish);
                newLanternFish.put(CHILD_FISH_REPRODUCE_VALUE, numberOfFish);
            } else {
                long existingFish = newLanternFish.getOrDefault(daysTillReproduction, 0L);
                newLanternFish.put(daysTillReproduction, existingFish + numberOfFish);
            }
        }

        return newLanternFish;
    }


    public static void main(String[] args) {
        new DaySix().execute();
    }
}
