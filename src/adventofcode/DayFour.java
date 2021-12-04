package adventofcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DayFour extends AbstractAdventOfCode {

    @Override
    public Object solvePartOne() throws Exception {
        List<String> input = readInput("input/day4/data.txt");
        List<Integer> randomSequence = Stream.of(input.get(0).split(",")).map(Integer::parseInt).collect(Collectors.toList());
        List<Board> boards = parseInputToBoards(input);

        for (Integer selectedNumber : randomSequence) {
            for (Board b : boards) {
                b.markNumber(selectedNumber);
                if (b.hasBingo()) {
                    return handleBingo(selectedNumber, b);
                }
            }
        }

        throw new IllegalStateException("Should not get to this state, a bingo should have been found.");
    }

    @Override
    public Object solvePartTwo() throws Exception {
        List<String> input = readInput("input/day4/data.txt");
        List<Integer> randomSequence = Stream.of(input.get(0).split(",")).map(Integer::parseInt).collect(Collectors.toList());
        List<Board> boards = parseInputToBoards(input);

        for (Integer selectedNumber : randomSequence) {
            for (Board b : new ArrayList<>(boards)) {
                b.markNumber(selectedNumber);
                if (b.hasBingo()) {
                    if (boards.size() == 1) {
                        return handleBingo(selectedNumber, b);
                    } else {
                        boards.remove(b);
                    }
                }
            }
        }
        throw new IllegalStateException("Should not get to this state, a bingo should have been found.");
    }

    private List<Board> parseInputToBoards(List<String> input) {
        List<Board> boards = new ArrayList<>();
        int[][] board = new int[5][5];
        int row = 0;
        for (int i = 1; i < input.size(); i++) {
            if (input.get(i).isBlank()) {
                if (i == 1) {
                    continue;
                }
                boards.add(new Board(board));
                board = new int[5][5];
                row = 0;
                continue;
            }

            List<Integer> cols = Stream.of(input.get(i).split("\\s+"))
                    .filter(Predicate.not(String::isBlank))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());

            for (int j = 0; j < cols.size(); j++) {
                board[row][j] = cols.get(j);
            }
            row++;
        }
        boards.add(new Board(board));
        return boards;
    }


    private int handleBingo(int lastNumber, Board b) {
        boolean[][] marked = b.getMarked();
        int[][] board = b.getBoard();

        int boardSum = 0;
        for (int i = 0; i < marked.length; i++) {
            for (int j = 0; j < marked[0].length; j++) {
                if (!marked[i][j]) {
                    boardSum += board[i][j];
                }
            }
        }

        return boardSum * lastNumber;
    }

    private static class Board {

        private final int[][] board;
        private final boolean[][] marked;
        private final int[] numRowsMarked;
        private final int[] numColsMarked;
        private Map<Integer, int[]> boardMap;

        public Board(int[][] board) {
            this.board = board;
            initBoardMap();

            marked = new boolean[board.length][board[0].length];
            numRowsMarked = new int[board.length];
            numColsMarked = new int[board[0].length];
        }

        public boolean[][] getMarked() {
            return marked;
        }

        public int[][] getBoard() {
            return board;
        }

        public boolean hasBingo() {
            for (int j : numRowsMarked) {
                if (j == board.length) {
                    return true;
                }
            }

            for (int i : numColsMarked) {
                if (i == board[0].length) {
                    return true;
                }
            }

            return false;
        }

        public void markNumber(int number) {
            if(boardMap.containsKey(number)) {
                int[] position = boardMap.get(number);
                int row = position[0];
                int col = position[1];
                marked[row][col] = true;
                numRowsMarked[row]++;
                numColsMarked[col]++;
            }
        }

        private void initBoardMap() {
            boardMap = new HashMap<>();
            for (int row = 0; row < board.length; row++) {
                for (int col = 0; col < board[0].length; col++) {
                    int number = board[row][col];
                    boardMap.put(number, new int[]{ row, col });
                }
            }
        }
    }

    public static void main(String[] args) {
        new DayFour().execute();
    }
}
