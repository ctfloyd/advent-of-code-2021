package adventofcode;

import java.util.HashSet;
import java.util.Set;

public class DayEleven extends AbstractAdventOfCode {

    private static final int NUM_SIMULATION_STEPS = 100;

    @Override
    public Object solvePartOne() throws Exception {
        int[][] board = read2dBoard("input/day11/data.txt");

        int flashes = 0;
        for (int i = 0; i < NUM_SIMULATION_STEPS; i++) {
            flashes += simulate(board);
        }
        return flashes;
    }

    @Override
    public Object solvePartTwo() throws Exception {
        int[][] board = read2dBoard("input/day11/data.txt");

        int flashes = 0;
        int step = 0;
        while (flashes != (board.length * board[0].length)) {
            flashes = simulate(board);
            step++;
        }
        return step;
    }

    private int simulate(int[][] board) {

        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                board[row][col]++;
            }
        }

        int flashes = 0;
        Set<int[]> flashed = new HashSet<>();
        boolean foundFlash = true;

        while (foundFlash) {
            foundFlash = false;

            for (int row = 0; row < board.length; row++) {
                for (int col = 0; col < board[row].length; col++) {
                    if (board[row][col] > 9 && !flashed.contains(new int[]{row, col})) {
                        flashes++;
                        foundFlash = true;
                        flashed.add(new int[]{row, col});
                        board[row][col] = 0;

                        if (row - 1 >= 0) {
                            board[row - 1][col]++;
                        }
                        if (row + 1 < board.length) {
                            board[row + 1][col]++;
                        }
                        if (col - 1 >= 0) {
                            board[row][col - 1]++;
                        }
                        if (col + 1 < board[row].length) {
                            board[row][col + 1]++;
                        }
                        if (row - 1 >= 0 && col - 1 >= 0) {
                            // top left
                            board[row - 1][col - 1]++;
                        }
                        if (row + 1 < board.length && col - 1 >= 0) {
                            // bottom left
                            board[row + 1][col - 1]++;
                        }
                        if (row + 1 < board.length && col + 1 < board[row + 1].length) {
                            // bottom right
                           board[row + 1][col + 1]++;
                        }
                        if (row - 1 >= 0 && col + 1 < board[row].length) {
                            // top right
                            board[row - 1][col + 1]++;
                        }
                    }
                }
            }
        }

        for (int[] rowCol : flashed) {
            board[rowCol[0]][rowCol[1]] = 0;
        }

        return flashes;
    }


    public static void main(String[] args) {
        new DayEleven().execute();
    }
}
