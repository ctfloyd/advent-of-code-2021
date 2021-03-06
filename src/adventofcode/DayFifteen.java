package adventofcode;

import java.util.*;

public class DayFifteen extends AbstractAdventOfCode {
    @Override
    public Object solvePartOne() throws Exception {
        return dijkstra(read2dBoard("input/day15/data.txt"));
    }

    @Override
    public Object solvePartTwo() throws Exception {
        return dijkstra(scaleBoard(read2dBoard("input/day15/data.txt")));
    }

    private int[][] scaleBoard(int[][] board) {
        int[][] scaled = new int[5 * board.length][5 * board[0].length];

        for (int i = 0; i < scaled.length; i++) {
            int rowRiskFactor = i / board.length;
            for (int j = 0; j < scaled.length; j++) {
                int colRiskFactor = j / board[0].length;
                int value = board[i % board.length][j % board[0].length];
                value += rowRiskFactor + colRiskFactor;
                if (value > 9) {
                    scaled[i][j] = value % 9;
                } else {
                    scaled[i][j] = value;
                }
            }
        }

        return scaled;
    }

    private int dijkstra(int[][] board) {
        boolean[][] visited = new boolean[board.length][board[0].length];
        int[][] tentativeDistance = new int[board.length][board[0].length];

        Queue<int[]> nextNodeCandidates = new PriorityQueue<>(100, Comparator.comparingInt(a -> tentativeDistance[a[0]][a[1]]));

        for (int[] row : tentativeDistance) {
            Arrays.fill(row, Integer.MAX_VALUE);
        }
        tentativeDistance[0][0] = 0;

        int[] current = new int[] {0, 0};
        while (!visited[board.length - 1][board[0].length - 1]) {
            List<int[]> neighbors = getNeighbors(current, board);
            for (int[] neighbor : neighbors) {
                if (!visited[neighbor[0]][neighbor[1]]) {
                    int distance = tentativeDistance[current[0]][current[1]] + board[neighbor[0]][neighbor[1]];
                    tentativeDistance[neighbor[0]][neighbor[1]] = Math.min(distance, tentativeDistance[neighbor[0]][neighbor[1]]);
                    nextNodeCandidates.add(neighbor);
                }
            }
            visited[current[0]][current[1]] = true;

            current = null;
            while (current == null) {
                current = nextNodeCandidates.poll();
                if (current == null) {
                    break;
                }

                if (visited[current[0]][current[1]]) {
                    current = null;
                }
            }
        }

        return tentativeDistance[board.length - 1][board[0].length - 1];
    }

    private List<int[]> getNeighbors(int[] current, int[][] board) {
        List<int[]> neighbors = new ArrayList<>();
        if (current[0] - 1 >= 0) {
            neighbors.add(new int[] { current[0] - 1, current[1]});
        }
        if (current[0] + 1 < board.length) {
            neighbors.add(new int[] { current[0] + 1, current[1]});
        }
        if (current[1] - 1 >= 0) {
            neighbors.add(new int[] { current[0], current[1] - 1});
        }
        if (current[1] + 1 < board[0].length) {
            neighbors.add(new int[] { current[0], current[1] + 1});
        }
        return neighbors;
    }

    public static void main(String [] args) {
        new DayFifteen().execute();
    }
}
