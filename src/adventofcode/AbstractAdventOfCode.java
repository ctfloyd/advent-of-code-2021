package adventofcode;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractAdventOfCode {

    public List<String> readInput(String path) throws Exception {
        return Files.lines(Path.of(path)).collect(Collectors.toList());
    }

    public List<Integer> readInputAsInts(String path) throws Exception {
        return readInput(path).stream().map(Integer::parseInt).collect(Collectors.toList());
    }

    public abstract Object solvePartOne() throws Exception;
    public abstract Object solvePartTwo() throws Exception;

    public void execute() {
        try {
            Object answer = solvePartOne();
            System.out.println("The answer to part 1 is: " + answer);

            answer = solvePartTwo();
            System.out.println("The answer to part 2 is: " + answer);
        } catch (Exception ex) {
            System.err.println("An error occurred while solving advent of code problem!");
            ex.printStackTrace();
        }
    }


}
