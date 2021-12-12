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

        if (!current.isUpper()) {
            visited.add(current);
        }

        int numPaths = 0;
        for (Node n : current.getNodes()) {
            HashSet<Node> visitedInCurrentPath = new HashSet<>(visited);
            numPaths += dfs(n, visitedInCurrentPath, smallCaveExceptionMade);
        }
        return numPaths;
    }


    private Node parseInput(List<String> input) {
        HashMap<String, Node> nodes = new HashMap<>();
        for (String line : input) {
            String[] parts = line.split("-");
            Node left = nodes.computeIfAbsent(parts[0], Node::new);
            Node right = nodes.computeIfAbsent(parts[1], Node::new);
            left.addNode(right);
            right.addNode(left);
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

        public boolean isUpper() {
            for (int i = 0; i < name.length(); i++) {
                if(!Character.isUpperCase(name.charAt(i))) {
                    return false;
                }
            }
            return true;
        }
    }

    public static void main(String[] args) {
        new DayTwelve().execute();
    }
}
