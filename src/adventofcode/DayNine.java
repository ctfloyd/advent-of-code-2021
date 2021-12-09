package adventofcode;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DayNine extends AbstractAdventOfCode {
    @Override
    public Object solvePartOne() throws Exception {
        List<String> input = readInput("input/day9/data.txt");
        int[][] board = new int[input.size()][input.get(0).length()];
        for (int i = 0; i < input.size(); i++) {
            String[] chs = input.get(i).split("");
            for (int j = 0; j < chs.length; j++) {
                board[i][j] = Integer.parseInt(chs[j]);
            }
        }

        int risk = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (isLocalMinima(board, i, j)) {
                    risk += board[i][j] + 1;
                }
            }
        }
        return risk;
    }

    @Override
    public Object solvePartTwo() throws Exception {
        List<String> input = readInput("input/day9/data.txt");
        int[][] board = new int[input.size()][input.get(0).length()];
        for (int i = 0; i < input.size(); i++) {
            String[] chs = input.get(i).split("");
            for (int j = 0; j < chs.length; j++) {
                board[i][j] = Integer.parseInt(chs[j]);
            }
        }

        List<Integer> basinSizes = new ArrayList<>();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (isLocalMinima(board, i, j)) {
                    basinSizes.add(exploreBasin(board, i, j, new HashSet<>()));
                }
            }
        }

        assert(basinSizes.size() >= 3);
        basinSizes.sort((a, b) -> b - a);
        return basinSizes.get(0) * basinSizes.get(1) * basinSizes.get(2);
    }

    private int exploreBasin(int[][] board, int i, int j, Set<Point> seen) {
        if (i < 0 || i >= board.length || j < 0 || j >= board[i].length) {
            return 0;
        }

        if (seen.contains(new Point(i, j))) {
            return 0;
        } else {
            seen.add(new Point(i, j));
        }


        if(board[i][j] == 9) {
            return 0;
        }

        int basinSize = 1;
        basinSize += exploreBasin(board, i + 1, j, seen);
        basinSize += exploreBasin(board, i - 1, j, seen);
        basinSize += exploreBasin(board, i, j + 1, seen);
        basinSize += exploreBasin(board, i, j - 1, seen);
        return basinSize;
    }

    private boolean isLocalMinima(int[][] board, int i, int j) {
        int numberAtPosition = board[i][j];

        if (i - 1 >= 0 && board[i - 1][j] <= numberAtPosition) {
            return false;
        } else if (i + 1 < board.length && board[i + 1][j] <= numberAtPosition) {
            return false;
        } else if (j - 1 >= 0 && board[i][j - 1] <= numberAtPosition) {
            return false;
        } else if (j + 1 < board[i].length && board[i][j + 1] <= numberAtPosition) {
            return false;
        }

        return true;
    }

    public static void main(String[] args) {
        new DayNine().execute();
    }
}
