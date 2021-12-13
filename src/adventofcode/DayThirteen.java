package adventofcode;

import java.awt.*;
import java.util.*;
import java.util.List;

public class DayThirteen extends AbstractAdventOfCode {

    @Override
    public Object solvePartOne() throws Exception {
        InputResult input = parseInput(readInput("input/day13/data.txt"));

        for (Fold f : input.folds) {
            input.points = fold(input.points, f);
            break;
        }

        return input.points.size();
    }

    @Override
    public Object solvePartTwo() throws Exception {
        InputResult input = parseInput(readInput("input/day13/data.txt"));

        for (Fold f : input.folds) {
            input.points = fold(input.points, f);
        }

        printGraph(input.points);
        return null;
    }

    private InputResult parseInput(List<String> input) {
        Set<Point> points = new HashSet<>();
        List<Fold> folds = new ArrayList<>();

        boolean isFolds = false;
        for (String str : input) {
            if (str.isBlank()) {
                isFolds = true;
            } else {
                if (!isFolds) {
                    String[] parts = str.split(",");
                    points.add(new Point(Integer.parseInt(parts[0]), Integer.parseInt(parts[1])));
                } else {
                    str = str.replace("fold along ", "");
                    String[] split = str.split("=");
                    folds.add(new Fold(split[0], Integer.parseInt(split[1])));
                }
            }
        }

        return new InputResult(points, folds);
    }

    private Set<Point> fold(Set<Point> points, Fold f) {
        Set<Point> updatedPoints = new HashSet<>();
        int foldLine = f.num;
        for (Point p : points) {
            if (f.axis.equals("y")) {
                updatedPoints.add(foldY(foldLine, p));
            } else if (f.axis.equals("x")){
                updatedPoints.add(foldX(foldLine, p));
            } else {
                throw new IllegalStateException("Illegal axis.");
            }
        }

        return updatedPoints;
    }

    private Point foldY(int foldLine, Point p) {
        if (p.y < foldLine) {
            return p;
        }

        int newY = foldLine - (p.y - foldLine);
        return new Point(p.x, newY);
    }

    private Point foldX(int foldLine, Point p) {
        if (p.x < foldLine) {
            return p;
        }

        int newX = foldLine - (p.x - foldLine);
        return new Point(newX, p.y);
    }

    private void printGraph(Set<Point> points) {
        int x = -1;
        int y = -1;
        for (Point p : points) {
            x = Math.max(p.x, x);
            y = Math.max(p.y, y);
        }

        char[][] hasPoint = new char[y + 1][x + 1];
        for (char[] chars : hasPoint) {
            Arrays.fill(chars, '.');
        }

        for (Point p : points) {
            hasPoint[p.y][p.x] = '#';
        }

        for (char[] row : hasPoint) {
            System.out.println(Arrays.toString(row));
        }
    }

    private static class Fold {
        public String axis;
        public int num;

        public Fold(String axis, int num) {
            this.axis = axis;
            this.num = num;
        }
    }

    private static class InputResult {
        public Set<Point> points;
        public List<Fold> folds;

        public InputResult(Set<Point> points, List<Fold> folds) {
            this.points = points;
            this.folds = folds;
        }
    }

    public static void main(String[] args) {
        new DayThirteen().execute();
    }
}
