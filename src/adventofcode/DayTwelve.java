package adventofcode;

import java.util.*;

public class DayTwelve extends AbstractAdventOfCode {

    @Override
    public Object solvePartOne() throws Exception {
        List<String> input = readInput("input/day12/data.txt");
        return dfs(parseInput(input), new HashSet<>(), true);
    }

    @Override
    public Object solvePartTwo() throws Exception {
        List<String> input = readInput("input/day12/data.txt");
        return dfs(parseInput(input), new HashSet<>(), false);
    }

    private int dfs(Node current, Set<Node> visited, boolean smallCaveExceptionMade) {
        if (current.getName().equals("end")) {
            return 1;
        }

        if (visited.contains(current)) {
            if (current.getName().equals("start") || smallCaveExceptionMade) {
                return 0;
            } else {
                smallCaveExceptionMade = true;
            }
        }

        if (!isUppercase(current.getName())) {
            visited.add(current);
        }

        int numPaths = 0;
        for (Node n : current.getNodes()) {
            HashSet<Node> visitedInCurrentPath = new HashSet<>(visited);
            numPaths += dfs(n, visitedInCurrentPath, smallCaveExceptionMade);
        }
        return numPaths;
    }

    private boolean isUppercase(String str) {
        for (int i = 0; i < str.length(); i++) {
            if(!Character.isUpperCase(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private Node parseInput(List<String> input) {
        HashMap<String, Node> nodes = new HashMap<>();
        for (String line : input) {
            String[] parts = line.split("-");
            Node left = nodes.getOrDefault(parts[0], new Node(parts[0]));
            Node right = nodes.getOrDefault(parts[1], new Node(parts[1]));
            left.addNode(right);
            right.addNode(left);
            nodes.put(parts[0], left);
            nodes.put(parts[1], right);
        }
        return nodes.get("start");
    }

    private static final class Node {

        private final String name;
        private final List<Node> nodes;

        public Node(final String name) {
            this.name = name;
            this.nodes = new ArrayList<>();
        }

        public void addNode(final Node n) {
            this.nodes.add(n);
        }

        public String getName() {
            return name;
        }

        public List<Node> getNodes() {
            return nodes;
        }
    }

    public static void main(String[] args) {
        new DayTwelve().execute();
    }
}
