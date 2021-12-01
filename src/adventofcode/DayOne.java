package adventofcode;

import java.util.LinkedList;
import java.util.List;

public class DayOne extends AbstractAdventOfCode {

    @Override
    public Object solvePartOne() throws Exception {
        List<Integer> input = readInputAsInts("input/day1/data.txt");
        return countNumberOfIncreasingElementsInSequence(input);
    }

    @Override
    public Object solvePartTwo() throws Exception {
        List<Integer> input = readInputAsInts("input/day1/data.txt");
        input = createWindowsFromSequence(input);
        return countNumberOfIncreasingElementsInSequence(input);
    }

    private int countNumberOfIncreasingElementsInSequence(List<Integer> sequence) {
        int increasingCount = 0;

        for (int i = 1; i < sequence.size(); i++) {
            if (sequence.get(i) > sequence.get(i - 1)) {
                increasingCount++;
            }
        }

        return increasingCount;
    }

    private List<Integer> createWindowsFromSequence(List<Integer> sequence) {
        List<Integer> windows = new LinkedList<>();

        for (int i = 2; i < sequence.size(); i++) {
            windows.add(sequence.get(i) + sequence.get(i - 1) + sequence.get(i - 2));
        }

        return windows;
    }

    public static void main(String[] args) {
        new DayOne().execute();
    }

}
