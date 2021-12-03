package adventofcode;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class DayThree extends AbstractAdventOfCode{

    private static final byte BIT_MASK = 0b1;
    private static final int BINARY_NUMBER_LEN = 12;
    private static final int FINAL_BIT_MASK_EXAMPLE = 0x0000001F;
    private static final int FINAL_BIT_MASK_DATA = 0x00000FFF;

    @Override
    public Object solvePartOne() throws Exception {
        List<Integer> binaryNumbers = readInputAsInts("input/day3/data.txt", 2);

        int gammaRate = 0;
        for (int i = BINARY_NUMBER_LEN - 1; i >= 0; i--) {
            BitCounts bits = countBitsAt(binaryNumbers, i);
            if (bits.one > bits.zero) {
                gammaRate |= (0b1 << i);
            }
        }

        return gammaRate * (FINAL_BIT_MASK_DATA & ~gammaRate);
    }

    @Override
    public Object solvePartTwo() throws Exception {
        List<Integer> binaryNumbers = readInputAsInts("input/day3/data.txt", 2);

        List<Integer> oxygenNumbers = new ArrayList<>(binaryNumbers);
        List<Integer> co2ScrubberNumbers = new ArrayList<>(binaryNumbers);

        for (int i = BINARY_NUMBER_LEN - 1; i >= 0; i--) {
            BitCounts oxygenBits = countBitsAt(oxygenNumbers, i);
            BitCounts co2Bits = countBitsAt(co2ScrubberNumbers, i);

            oxygenNumbers = filterOxygenList(oxygenNumbers, oxygenBits, i);
            co2ScrubberNumbers = filterCo2List(co2ScrubberNumbers, co2Bits, i);
        }

        return oxygenNumbers.get(0) * co2ScrubberNumbers.get(0);
    }

    private List<Integer> filterOxygenList(List<Integer> oxygenNumbers, BitCounts oxygenBits, int i) {
        Predicate<Integer> keepOnesPredicate = n -> (n & BIT_MASK << i) > 0;

        if (oxygenBits.zero > oxygenBits.one) {
            // If one is LSB flip the predicate, keep 0s
            keepOnesPredicate = keepOnesPredicate.negate();
        }

        if (oxygenNumbers.size() > 1) {
            return oxygenNumbers.stream().filter(keepOnesPredicate).collect(Collectors.toList());
        }

        return oxygenNumbers;
    }

    private List<Integer> filterCo2List(List<Integer> co2Numbers, BitCounts co2Bits, int i) {
        Predicate<Integer> keepOnesPredicate = n -> (n & BIT_MASK << i) > 0;

        if (co2Bits.zero <= co2Bits.one) {
            // If zero is LSB flip the predicate, keep 0s
            keepOnesPredicate = keepOnesPredicate.negate();
        }

        if (co2Numbers.size() > 1) {
            return co2Numbers.stream().filter(keepOnesPredicate).collect(Collectors.toList());
        }

        return co2Numbers;
    }

    private BitCounts countBitsAt(List<Integer> numbers, int pos) {
        int oneCount = 0;
        int zeroCount = 0;

        for (int number : numbers) {
            int bit = number & (BIT_MASK << pos);
            if (bit > 0) {
                oneCount++;
            } else {
                zeroCount++;
            }
        }

        return new BitCounts(zeroCount, oneCount);
    }

    private static final class BitCounts {
        public int zero;
        public int one;

        BitCounts(int zero, int one) { this.zero = zero; this.one = one;}
    }

    public static void main (String[] args) {
        new DayThree().execute();
    }

}
