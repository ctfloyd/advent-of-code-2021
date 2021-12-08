package adventofcode;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DayEight extends AbstractAdventOfCode {
    @Override
    public Object solvePartOne() throws Exception {
        List<String> lines = readInput("input/day8/data.txt");
        return countUniqueSimpleDigits(lines);
    }

    @Override
    public Object solvePartTwo() throws Exception {
        List<String> lines = readInput("input/day8/data.txt");
        int sum = 0;
        for (String line : lines) {
            String[] parts = line.split(" \\| ");
            String[] inputParts = parts[0].split(" ");
            String[] outputParts = parts[1].split(" ");
            String[] displaySegments = determineDisplaySegments(inputParts);
            sum += determineNumber(displaySegments, outputParts);
        }
        return sum;
    }

    private int determineNumber(String[] displaySegments, String[] outputParts) {
        List<String> displaySegmentsList = Arrays.asList(displaySegments);
        int number = 0;
        for (String output : outputParts) {
            Set<DisplaySegments> segments = new HashSet<>();

            List<String> chars = Stream.of(output.split("")).collect(Collectors.toList());
            for (String ch: chars) {
                int index = displaySegmentsList.indexOf(ch);
                segments.add(DisplaySegments.values()[index]);
            }

            int num = decodeSegmentsToNumber(segments);
            number = (number * 10) + num;
        }
        return number;
    }

    private int decodeSegmentsToNumber(Set<DisplaySegments> displaySegments) {
        Set<DisplaySegments> zero = Stream.of(DisplaySegments.TOP, DisplaySegments.TOP_LEFT, DisplaySegments.TOP_RIGHT,
                DisplaySegments.BOTTOM_LEFT, DisplaySegments.BOTTOM_RIGHT, DisplaySegments.BOTTOM).collect(Collectors.toSet());
        Set<DisplaySegments> one = Stream.of(DisplaySegments.TOP_RIGHT, DisplaySegments.BOTTOM_RIGHT).collect(Collectors.toSet());
        Set<DisplaySegments> two = Stream.of(DisplaySegments.TOP, DisplaySegments.TOP_RIGHT, DisplaySegments.MIDDLE,
                DisplaySegments.BOTTOM_LEFT, DisplaySegments.BOTTOM).collect(Collectors.toSet());
        Set<DisplaySegments> three = Stream.of(DisplaySegments.TOP, DisplaySegments.TOP_RIGHT, DisplaySegments.MIDDLE,
                DisplaySegments.BOTTOM_RIGHT, DisplaySegments.BOTTOM).collect(Collectors.toSet());
        Set<DisplaySegments> four = Stream.of(DisplaySegments.TOP_LEFT, DisplaySegments.MIDDLE, DisplaySegments.TOP_RIGHT,
                DisplaySegments.BOTTOM_RIGHT).collect(Collectors.toSet());
        Set<DisplaySegments> five = Stream.of(DisplaySegments.TOP, DisplaySegments.TOP_LEFT, DisplaySegments.MIDDLE,
                DisplaySegments.BOTTOM_RIGHT, DisplaySegments.BOTTOM).collect(Collectors.toSet());
        Set<DisplaySegments> six = Stream.of(DisplaySegments.TOP_LEFT, DisplaySegments.MIDDLE, DisplaySegments.BOTTOM_LEFT,
                DisplaySegments.BOTTOM, DisplaySegments.BOTTOM_RIGHT, DisplaySegments.TOP).collect(Collectors.toSet());
        Set<DisplaySegments> seven = Stream.of(DisplaySegments.TOP, DisplaySegments.BOTTOM_RIGHT, DisplaySegments.TOP_RIGHT).collect(Collectors.toSet());
        Set<DisplaySegments> eight = Stream.of(DisplaySegments.values()).collect(Collectors.toSet());
        Set<DisplaySegments> nine = Stream.of(DisplaySegments.TOP, DisplaySegments.TOP_LEFT, DisplaySegments.TOP_RIGHT,
                DisplaySegments.MIDDLE, DisplaySegments.BOTTOM_RIGHT, DisplaySegments.BOTTOM).collect(Collectors.toSet());
        Set<Set<DisplaySegments>> allDigits = Stream.of(zero, one, two, three, four, five, six, seven, eight, nine).collect(Collectors.toSet());

        for (Set<DisplaySegments> digit : allDigits) {
            if (displaySegments.size() == digit.size() && digit.containsAll(displaySegments)) {
                if (digit == zero) {
                    return 0;
                } else if (digit == one) {
                    return 1;
                } else if (digit == two) {
                    return 2;
                } else if (digit == three) {
                    return 3;
                } else if (digit == four) {
                    return 4;
                } else if (digit == five) {
                    return 5;
                } else if (digit == six)  {
                    return 6;
                } else if (digit == seven) {
                    return 7;
                } else if (digit == eight) {
                    return 8;
                } else if (digit == nine) {
                    return 9;
                }
            }
        }
        throw new IllegalStateException("Could not decode digit.");
    }

    private String[] determineDisplaySegments(String[] inputParts) {
        // top, left, right, middle, left, right, bottom
        final String[] segmentDisplay = new String[7];

        String one = "";
        String four = "";
        String seven = "";
        for (String input : inputParts) {
            if (input.length() == 2) {
                one = input;
            } else if (input.length() == 4) {
                four = input;
            } else if (input.length() == 3) {
                seven = input;
            }
        }

        Set<String> oneSet = Stream.of(one.split("")).collect(Collectors.toSet());
        Set<String> sevenSet = Stream.of(seven.split("")).collect(Collectors.toSet());
        sevenSet.removeAll(oneSet);
        String top = sevenSet.stream().findFirst().orElseThrow();
        segmentDisplay[DisplaySegments.TOP.ordinal()] = top;

        Set<String> topLeftOrMiddle = Stream.of(four.split("")).collect(Collectors.toSet());
        topLeftOrMiddle.removeAll(oneSet);

        Set<String> partialThree = new HashSet<>(oneSet);
        partialThree.add(top);

        String three = null;
        for (String input : inputParts) {
            Set<String> numberSet = Stream.of(input.split("")).collect(Collectors.toSet());
            numberSet.removeAll(partialThree);
            if (numberSet.size() == 2 && !input.equals(four)) {
               three = input;
               break;
            }
        }

        Set<String> threeSet = Stream.of(three.split("")).collect(Collectors.toSet());
        threeSet.removeAll(oneSet);
        threeSet.remove(top);
        Set<String> topLeftAndMiddleRemoved = new HashSet<>(topLeftOrMiddle);

        // top left
        topLeftAndMiddleRemoved.removeAll(threeSet);
        assert(topLeftAndMiddleRemoved.size() == 1);
        String topLeft = topLeftAndMiddleRemoved.stream().findFirst().orElseThrow();
        segmentDisplay[DisplaySegments.TOP_LEFT.ordinal()] = topLeft;

        // middle
        topLeftOrMiddle.remove(topLeft);
        assert(topLeftOrMiddle.size() == 1);
        String middle = topLeftOrMiddle.stream().findFirst().orElseThrow();
        segmentDisplay[DisplaySegments.MIDDLE.ordinal()] = middle;

        // bottom
        threeSet.remove(middle);
        assert(threeSet.size() == 1);
        String bottom = threeSet.stream().findFirst().orElseThrow();
        segmentDisplay[DisplaySegments.BOTTOM.ordinal()] = bottom;

        Set<String> partialFive = new HashSet<>();
        partialFive.add(segmentDisplay[DisplaySegments.TOP.ordinal()]);
        partialFive.add(segmentDisplay[DisplaySegments.TOP_LEFT.ordinal()]);
        partialFive.add(segmentDisplay[DisplaySegments.MIDDLE.ordinal()]);
        partialFive.add(segmentDisplay[DisplaySegments.BOTTOM.ordinal()]);

        String five = null;
        for (String input : inputParts) {
            Set<String> numberSet = Stream.of(input.split("")).collect(Collectors.toSet());
            numberSet.removeAll(partialFive);
            if (numberSet.size() == 1) {
                five = input;
                break;
            }
        }
        Set<String> fiveSet = Stream.of(five.split("")).collect(Collectors.toSet());
        fiveSet.removeAll(partialFive);
        assert(fiveSet.size() == 1);
        String bottomRight = fiveSet.stream().findFirst().orElseThrow();
        segmentDisplay[DisplaySegments.BOTTOM_RIGHT.ordinal()] = bottomRight;

        oneSet.remove(bottomRight);
        assert(oneSet.size() == 1);
        String topRight = oneSet.stream().findFirst().orElseThrow();
        segmentDisplay[DisplaySegments.TOP_RIGHT.ordinal()] = topRight;

        Set<String> validLetters = Stream.of("a", "b", "c", "d", "e", "f", "g").collect(Collectors.toSet());
        Set<String> lettersFound = Stream.of(segmentDisplay).collect(Collectors.toSet());
        validLetters.removeAll(lettersFound);
        assert(validLetters.size() == 1);
        String bottomLeft = validLetters.stream().findFirst().orElseThrow();
        segmentDisplay[DisplaySegments.BOTTOM_LEFT.ordinal()] = bottomLeft;


        return segmentDisplay;
    }

    private int countUniqueSimpleDigits(List<String> lines) {
        int uniqueSimpleDigits = 0;
        for (String line : lines) {
            String[] parts = line.split(" \\| ");
            String[] outputParts = parts[1].split(" ");
            uniqueSimpleDigits += countUniqueSimpleDigitsInLine(outputParts);
        }
        return uniqueSimpleDigits;
    }

    private int countUniqueSimpleDigitsInLine(String[] parts) {
        int count = 0;
        for (String part : parts) {
            // L 2 -> '1', L 4 -> '4', L 3 -> '7', L 7 -> '8'
            int length = part.length();
            if (length == 2 || length == 4 || length == 3 || length == 7) {
                count++;
            }
        }
        return count;
    }

    private enum DisplaySegments {
        TOP,
        TOP_LEFT,
        TOP_RIGHT,
        MIDDLE,
        BOTTOM_LEFT,
        BOTTOM_RIGHT,
        BOTTOM
    }

    public static void main(String[] args) {
        new DayEight().execute();
    }
}
