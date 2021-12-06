package adventofcode;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractAdventOfCode {

    public List<String> readInput(String path) throws Exception {
        return Files.lines(Path.of(path)).collect(Collectors.toList());
    }

    public List<String> readInputAsCsv(String path) throws Exception {
        return Files.lines(Path.of(path)).map(l -> l.split(",")).flatMap(Stream::of).collect(Collectors.toList());
    }

    public List<Integer> readInputAsInts(String path) throws Exception {
        return readInputAsInts(path, 10);
    }

    public List<Integer> readInputAsInts(String path, int radix) throws Exception {
        return readInput(path).stream().map(i -> Integer.parseInt(i, radix)).collect(Collectors.toList());
    }

    public List<Integer> readInputAsCsvInts(String path, int radix) throws Exception {
        return readInputAsCsv(path).stream().map(i -> Integer.parseInt(i, radix)).collect(Collectors.toList());
    }

    public List<Integer> readInputAsCsvInts(String path) throws Exception {
        return readInputAsCsvInts(path, 10);
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
