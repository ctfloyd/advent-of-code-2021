package adventofcode;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DayTen extends AbstractAdventOfCode {

    private static final Set<String> VALID_OPENING_STRS = Stream.of("(", "{", "[", "<").collect(Collectors.toSet());
    private static final Map<String, String> OPENING_CHAR_TO_CLOSING_CHAR = Stream.of(
            new String[][] {{"(", ")"}, {"{", "}"}, {"[", "]"}, {"<", ">"}}
    ).collect(Collectors.toMap(e -> e[0], e -> e[1]));
    private static final Map<String, Integer> CLOSING_CHAR_TO_POINTS = Stream.of(
            new Object[][] {{")", 3}, {"]", 57}, {"}", 1197}, {">", 25137}}
    ).collect(Collectors.toMap(e -> (String)e[0], e -> (Integer)e[1]));
    private static final Map<String, Integer> OPENING_CHAR_TO_POINTS = Stream.of(
            new Object[][] {{"(", 1}, {"[", 2}, {"{", 3}, {"<", 4}}
    ).collect(Collectors.toMap(e -> (String)e[0], e -> (Integer)e[1]));


    @Override
    public Object solvePartOne() throws Exception {
        return readInput("input/day10/data.txt").stream().mapToLong(l -> getScoreForLine(l, true)).sum();
    }

    @Override
    public Object solvePartTwo() throws Exception {
        List<Long> scores = readInput("input/day10/data.txt").stream()
                .map(l -> getScoreForLine(l, false))
                .filter(s -> s > 0).sorted()
                .sorted()
                .collect(Collectors.toList());
        return scores.get(scores.size() / 2);
    }

    private long getScoreForLine(String line, boolean scoreCorrupt) {
        Stack<String> stack = new Stack<>();
        String[] chs = line.split("");
        for (String ch : chs) {
            if (VALID_OPENING_STRS.contains(ch)) {
                stack.push(ch);
            } else {
                String topOfStack = stack.peek();
                if (OPENING_CHAR_TO_CLOSING_CHAR.get(topOfStack).equals(ch)) {
                    stack.pop();
                } else {
                    return scoreCorrupt ? CLOSING_CHAR_TO_POINTS.get(ch) : -1;
                }
            }
        }

        return scoreCorrupt ? 0 : drainAndScoreStack(stack);
    }

    private long drainAndScoreStack(Stack<String> stack) {
        long score = 0;
        while (!stack.isEmpty()) {
            String ch = stack.remove(stack.size() - 1);
            score = (score * 5) + OPENING_CHAR_TO_POINTS.get(ch);
        }
        return score;
    }

    public static void main(String[] args) {
        new DayTen().execute();
    }
}
