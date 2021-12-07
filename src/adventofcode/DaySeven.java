package adventofcode;

import java.util.List;

public class DaySeven extends AbstractAdventOfCode {

    @Override
    public Object solvePartOne() throws Exception {
        List<Integer> input = readInputAsCsvInts("input/day7/data.txt");
        int max = input.stream().mapToInt(Integer::intValue).max().orElseThrow();

        int min = Integer.MAX_VALUE;
        for (int i = 0; i < max; i++) {
            min = Math.min(min, calculateFuel(input, i));
        }

        return min;
    }

    @Override
    public Object solvePartTwo() throws Exception {
        List<Integer> input = readInputAsCsvInts("input/day7/data.txt");
        int max = input.stream().mapToInt(Integer::intValue).max().orElseThrow();

        int min = Integer.MAX_VALUE;
        for (int i = 0; i < max; i++) {
            min = Math.min(min, calculateIncreasingFuel(input, i));
        }

        return min;
    }

    private int calculateFuel(List<Integer> input, int targetPosition) {
        int fuel = 0;
        for (Integer i : input) {
            fuel += Math.abs(targetPosition - i);
        }
        return fuel;
    }

    private int calculateIncreasingFuel(List<Integer> input, int targetPosition) {
        int fuel = 0;
        for (Integer i : input) {
            int distance = Math.abs(targetPosition - i);
            fuel += (distance * (distance + 1)) / 2;
        }
        return fuel;
    }

    public static void main(String[] args) {
        new DaySeven().execute();
    }
}
