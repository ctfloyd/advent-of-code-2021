package adventofcode;

import java.util.StringJoiner;

public class DaySeventeen extends AbstractAdventOfCode {

    private static int MAX_STEPS = 1000;
    private static int INITIAL_VELOCITY_LIMIT = 500;

    @Override
    public Object solvePartOne() throws Exception {
        Target target = init(readInput("input/day17/data.txt").get(0));

        int maxY = Integer.MIN_VALUE;
        for (int i = 0; i < INITIAL_VELOCITY_LIMIT; i++) {
            for (int j = 0; j < INITIAL_VELOCITY_LIMIT; j++) {
                Probe probe = new Probe();
                probe.vx = i;
                probe.vy = j;

                int thisY = Integer.MIN_VALUE;
                for (int k = 0; k < MAX_STEPS; k++) {
                    step(probe);
                    thisY = Math.max(thisY, probe.y);
                    if (target.intersects(probe)) {
                        maxY = Math.max(maxY, thisY);
                        break;
                    }
                }

            }
        }
        return maxY;

    }

    @Override
    public Object solvePartTwo() throws Exception {
        Target target = init(readInput("input/day17/data.txt").get(0));

        int count = 0;
        for (int i = -INITIAL_VELOCITY_LIMIT; i < INITIAL_VELOCITY_LIMIT; i++) {
            for (int j = -INITIAL_VELOCITY_LIMIT; j < INITIAL_VELOCITY_LIMIT; j++) {
                Probe probe = new Probe();
                probe.vx = i;
                probe.vy = j;

                for (int k = 0; k < MAX_STEPS; k++) {
                    step(probe);
                    if (target.intersects(probe)) {
                        count++;
                        break;
                    }
                }

            }
        }
        return count;
    }

    public Target init(String start) {
        start = start.replace("target area: ", "");
        String[] parts = start.split(", ");
        String[] x = parts[0].replace("x=", "").split("\\.\\.");
        String[] y = parts[1].replace("y=", "").split("\\.\\.");

        int x1 = Integer.parseInt(x[0]);
        int x2 = Integer.parseInt(x[1]);

        int y1 = Integer.parseInt(y[0]);
        int y2 = Integer.parseInt(y[1]);

        Target target = new Target();
        target.tlx = x1;
        target.tly = y2;
        target.brx = x2;
        target.bry = y1;
        return target;
    }

    public void step(Probe probe) {
        probe.x += probe.vx;
        probe.y += probe.vy;

        if (probe.vx < 0) {
            probe.vx += 1;
        } else if (probe.vx > 0) {
            probe.vx -= 1;
        }
        probe.vy -= 1;
    }

    private static class Probe {
        int vx;
        int vy;
        int x;
        int y;

        @Override
        public String toString() {
            return new StringJoiner(", ", Probe.class.getSimpleName() + "[", "]")
                    .add("vx=" + vx)
                    .add("vy=" + vy)
                    .add("x=" + x)
                    .add("y=" + y)
                    .toString();
        }
    }

    private static class Target {
        int tlx;
        int tly;
        int brx;
        int bry;

        public boolean intersects(Probe probe) {
            return probe.x >= tlx && probe.x <= brx && probe.y <= tly && probe.y >= bry;
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", Target.class.getSimpleName() + "[", "]")
                    .add("tlx=" + tlx)
                    .add("tly=" + tly)
                    .add("brx=" + brx)
                    .add("bry=" + bry)
                    .toString();
        }
    }

    public static void main(String[] args) {
        new DaySeventeen().execute();
    }
}
