package adventofcode;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DayTwo extends AbstractAdventOfCode {

    private enum Direction {
        FORWARD("forward"),
        DOWN("down"),
        UP("up");

        private final String name;
        Direction(String name)  { this.name = name; }
        public String getName() {return name; }
        public static Direction fromName(String name) {
            return Stream.of(Direction.values()).filter(n -> n.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
        }
    }

    @Override
    public Object solvePartOne() throws Exception {
        List<String> lines = readInput("input/day2/data.txt");
        Map<Direction, List<Integer>> instructions = lines.stream()
                .map(l -> l.split(" "))
                .map(s -> Map.entry(Direction.fromName(s[0]), Integer.parseInt(s[1])))
                .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.mapping(Map.Entry::getValue, Collectors.toList())));

        int horizontal = instructions.get(Direction.FORWARD).stream().mapToInt(Integer::intValue).sum();
        int depth = instructions.get(Direction.DOWN).stream().mapToInt(Integer::intValue).sum();
        depth -= instructions.get(Direction.UP).stream().mapToInt(Integer::intValue).sum();
        return horizontal * depth;
    }

    @Override
    public Object solvePartTwo() throws Exception {
        List<String> lines = readInput("input/day2/data.txt");

        final Trackers trackers = new Trackers();
        lines.stream()
                .map(l -> l.split(" "))
                .map(s -> Map.entry(Direction.fromName(s[0]), Integer.parseInt(s[1])))
                .forEach(e -> updateNumbers(e, trackers));

        return trackers.horizontal * trackers.depth;
    }

    private void updateNumbers(Map.Entry<Direction, Integer> entry, Trackers trackers) {
        int value = entry.getValue();
        Direction d = entry.getKey();

        if (d == Direction.UP) {
            trackers.aim -= value;
        } else if (d == Direction.DOWN) {
            trackers.aim += value;
        } else {
            trackers.horizontal += value;
            trackers.depth += trackers.aim * value;
        }
    }

    private static final class Trackers {
        public int horizontal;
        public int aim;
        public int depth;
    }

    public static void main(String[] args) {
        new DayTwo().execute();
    }
}
