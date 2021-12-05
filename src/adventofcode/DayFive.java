package adventofcode;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DayFive extends AbstractAdventOfCode {

    @Override
    public Object solvePartOne() throws Exception {
        List<String> input = readInput("input/day5/data.txt");
        List<int[][]> points = input.stream()
                .map(l -> l.split(" -> "))
                .map(s -> new int[][] { commaSeparatedPointToIntArray(s[0]), commaSeparatedPointToIntArray(s[1]) })
                .collect(Collectors.toList());

        int[] maxPoints = findMaxes(points);
        int maxX = maxPoints[0];
        int maxY = maxPoints[1];

        int[][] lines = new int[maxX + 1][maxY + 1];
        for (int[][] point : points) {
            int[] firstPoint = point[0];
            int[] secondPoint = point[1];
            mapLines(lines, firstPoint, secondPoint);
        }

        return Stream.of(lines)
                .flatMapToInt(Arrays::stream)
                .filter(n -> n >= 2)
                .count();
    }

    @Override
    public Object solvePartTwo() throws Exception {
        List<String> input = readInput("input/day5/data.txt");
        List<int[][]> points = input.stream()
                .map(l -> l.split(" -> "))
                .map(s -> new int[][] { commaSeparatedPointToIntArray(s[0]), commaSeparatedPointToIntArray(s[1]) })
                .collect(Collectors.toList());

        int[] maxPoints = findMaxes(points);
        int maxX = maxPoints[0];
        int maxY = maxPoints[1];

        int[][] lines = new int[maxX + 1][maxY + 1];
        for (int[][] point : points) {
            int[] firstPoint = point[0];
            int[] secondPoint = point[1];
            mapLinesWithDiagonals(lines, firstPoint, secondPoint);
        }

        return Stream.of(lines)
                .flatMapToInt(Arrays::stream)
                .filter(n -> n >= 2)
                .count();
    }

    private int[] findMaxes(List<int[][]> points) {
        int maxX = -1;
        int maxY = -1;
        for (int[][] point : points) {
            int[] firstPoint = point[0];
            int[] secondPoint = point[1];
            maxX = Math.max(firstPoint[0], maxX);
            maxX = Math.max(secondPoint[0], maxX);
            maxY = Math.max(firstPoint[1], maxY);
            maxY = Math.max(secondPoint[1], maxY);
        }

        return new int[] {maxX, maxY};
    }

    private void mapLines(int[][] lines, int[] firstPoint, int[] secondPoint) {
        if (firstPoint[0] == secondPoint[0] || firstPoint[1] == secondPoint[1])  {
            boolean isRow = true;
            int fixed = -1;
            int start = -1;
            int end = -1;
            if (firstPoint[0] == secondPoint[0]) {
                fixed = firstPoint[0];
                start = Math.min(firstPoint[1], secondPoint[1]);
                end = Math.max(firstPoint[1], secondPoint[1]);
            } else {
                isRow = false;
                fixed = firstPoint[1];
                start = Math.min(firstPoint[0], secondPoint[0]);
                end = Math.max(firstPoint[0], secondPoint[0]);
            }

            for (int i = start; i <= end; i++) {
                if (isRow) {
                    lines[i][fixed]++;
                } else {
                    lines[fixed][i]++;
                }
            }
        }
    }

    private void mapLinesWithDiagonals(int[][] lines, int[] firstPoint, int[] secondPoint) {
        int xDiff = firstPoint[0] - secondPoint[0];
        int yDiff = firstPoint[1] - secondPoint[1];

        int slope = Integer.MAX_VALUE;
        if (xDiff != 0) {
            slope = yDiff / xDiff;
        }

        if (slope == -1 || slope == 1) {
            int minRow = -1;
            int maxRow = -1;
            int startCol = -1;
            if  (firstPoint[0] <= secondPoint[0]) {
                minRow = firstPoint[0];
                maxRow = secondPoint[0];
                if (slope == -1) {
                    startCol = secondPoint[1];
                } else {
                    startCol = firstPoint[1];
                }
            } else {
                minRow = secondPoint[0];
                maxRow = firstPoint[0];
                if (slope == -1) {
                    startCol = firstPoint[1];
                } else {
                    startCol = secondPoint[1];
                }
            }

            if (slope == 1) {
                for (int i = minRow, j = startCol; i <= maxRow; i++, j++) {
                    lines[j][i]++;
                }
            } else {
                for (int i = maxRow, j = startCol; i >= minRow; i--, j++) {
                    lines[j][i]++;
                }
            }
        } else {
            mapLines(lines, firstPoint, secondPoint);
        }
    }

    private int[] commaSeparatedPointToIntArray(String commaSeparatedValue) {
        String[] point = commaSeparatedValue.split(",");
        return new int[] { Integer.parseInt(point[0]), Integer.parseInt(point[1])};
    }


    public static void main(String[] args) {
        new DayFive().execute();
    }
}
