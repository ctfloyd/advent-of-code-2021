package adventofcode;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class DayEighteen extends AbstractAdventOfCode {
    @Override
    public Object solvePartOne() throws Exception {
        List<String> input = readInput("input/day18/data.txt");
        List<SnailfishNumber> numbers = parse(input);
        SnailfishNumber current = numbers.get(0);
        for (int i = 1; i < numbers.size(); i++) {
            current = current.addTo(numbers.get(i));
        }
        return current.calculateMagnitude();
    }

    @Override
    public Object solvePartTwo() throws Exception {
        List<String> input = readInput("input/day18/data.txt");
        List<SnailfishNumber> numbers = parse(input);
        int largestMagnitude = Integer.MIN_VALUE;
        for (int i = 0; i < numbers.size(); i++) {
            for (int j = 0; j < numbers.size(); j++) {
                largestMagnitude = Math.max(largestMagnitude, numbers.get(i).addTo(numbers.get(j)).calculateMagnitude());
                largestMagnitude = Math.max(largestMagnitude, numbers.get(j).addTo(numbers.get(i)).calculateMagnitude());
            }
        }
        return largestMagnitude;
    }

    private List<SnailfishNumber> parse(List<String> input) {
        List<SnailfishNumber> numbers = new ArrayList<>();
        for (String line : input) {
            Stack<SnailfishNumber>  numberStack = new Stack<>();
            SnailfishNumber current = new SnailfishNumber();

            boolean isLeft = true;
            int depth = 0;

            for (int i = 0; i < line.length(); i++) {
                char ch = line.charAt(i);
                if (ch == '[') {
                    if (i == 0) {
                        continue;
                    }
                    numberStack.push(current);

                    SnailfishNumber next = new SnailfishNumber();
                    next.parent = current;

                    if (!current.hasLeftValue()) {
                        current.leftSnail = next;
                    } else if (!current.hasRightValue()) {
                        current.rightSnail = next;
                    }

                    isLeft = true;
                    current = next;
                    depth += 1;
                } else if (ch == ']'){
                    current.depth = depth;
                    depth -= 1;

                    if (i != line.length() - 1) {
                        current = numberStack.pop();
                    }
                } else if (ch == ',') {
                    // no operation
                } else {
                    if (isLeft) {
                        current.left = Character.getNumericValue(ch);
                        isLeft = false;
                    } else {
                        current.right = Character.getNumericValue(ch);
                    }
                }
            }

            if (numberStack.size() != 0) {
                throw new IllegalStateException("First snail is not found on stack.");
            }

            numbers.add(current);
        }
        return numbers;
    }

    private static class SnailfishNumber implements Cloneable {
        SnailfishNumber leftSnail = null;
        SnailfishNumber rightSnail = null;
        SnailfishNumber parent = null;

        int left = Integer.MIN_VALUE;
        int right = Integer.MIN_VALUE;

        int depth;

        public boolean hasLeftValue() {
            return leftSnail != null || left != Integer.MIN_VALUE;
        }

        public boolean hasRightValue() {
            return rightSnail != null || right != Integer.MIN_VALUE;
        }

        public SnailfishNumber addTo(SnailfishNumber other) {
            SnailfishNumber result = new SnailfishNumber();

            SnailfishNumber left = clone(result);
            SnailfishNumber right = other.clone(result);

            result.leftSnail = left;
            result.rightSnail = right;

            left.parent = result;
            right.parent = result;

            left.increaseDepth();
            right.increaseDepth();

            boolean actionTaken = true;
            while (actionTaken) {
                actionTaken = result.reduce();
            }

            return result;
        }

        private void increaseDepth() {
           depth += 1;

           if (leftSnail != null) {
               leftSnail.increaseDepth();
           }

           if (rightSnail != null) {
               rightSnail.increaseDepth();
           }
        }

        private boolean reduce() {
            boolean action = tryExplode();
            if (!action) {
                action = trySplit();
            }
            return action;
        }

        private boolean tryExplode() {
            if (depth >= 4) {
                explode();
                return true;
            }

            boolean actionTaken = false;
            if (leftSnail != null) {
                actionTaken = leftSnail.tryExplode();
            }

            if (!actionTaken) {
                if (rightSnail != null) {
                    actionTaken = rightSnail.tryExplode();
                }
            }
            return actionTaken;
        }

        private boolean trySplit() {
            if (left >= 10) {
                split(true);
                return true;
            }

            boolean actionTaken = false;
            if (leftSnail != null) {
                actionTaken = leftSnail.trySplit();
            }

            if (!actionTaken) {
                if (right >= 10) {
                    split(false);
                    return true;
                }

                if (rightSnail != null) {
                    actionTaken = rightSnail.trySplit();
                }
            }

            return actionTaken;
        }

        private void explode() {

            explodeLeft();
            explodeRight();

            if (parent == null) {
                throw new IllegalStateException("Parent cannot be null if we are exploding.");
            }

            if (parent.leftSnail == this) {
                parent.leftSnail = null;
                parent.left = 0;
            } else if (parent.rightSnail == this) {
                parent.rightSnail = null;
                parent.right = 0;
            }
        }

        private void explodeLeft() {
            // we are somewhere in the tree and we need to find our nearest neighbor to the left
            SnailfishNumber modifyNumber =  walkTreeLeft(parent, this);
//            System.out.println(this + " Explode left: Got modify number: " + modifyNumber);
            if (modifyNumber != null) {
                if (modifyNumber.right != Integer.MIN_VALUE) {
                    modifyNumber.right += left;
                } else {
                    modifyNumber.left += left;
                }
            }
        }

        private void explodeRight() {
            SnailfishNumber modifyNumber = walkTreeRight(parent, this);
//            System.out.println(this + " Explode right: Got modify number: " + modifyNumber);
            if (modifyNumber != null) {
                if (modifyNumber.left != Integer.MIN_VALUE) {
                    modifyNumber.left += right;
                } else {
                    modifyNumber.right += right;
                }
            }
        }

        private static SnailfishNumber walkTreeLeft(SnailfishNumber current, SnailfishNumber lastSnail) {
            if (current == null) {
                return null;
            }

            if (current.leftSnail != null && current.leftSnail != lastSnail) {
                return traverseDown(current.leftSnail, current, true);
            }

            if (current.left != Integer.MIN_VALUE) {
                return current;
            }

            return walkTreeLeft(current.parent, current);
        }

        private static SnailfishNumber walkTreeRight(SnailfishNumber current, SnailfishNumber lastSnail) {
            if (current == null) {
                return null;
            }

            if (current.rightSnail != null && current.rightSnail != lastSnail) {
//                System.out.println("Traversing down. Current: " + current + " right snail: " + current.rightSnail);
                return traverseDown(current.rightSnail, current, false);
            }

            if (current.right != Integer.MIN_VALUE) {
                return current;
            }

            return walkTreeRight(current.parent, current);
        }

        public static SnailfishNumber traverseDown(SnailfishNumber current, SnailfishNumber last, boolean explodingLeft) {
            // potential node
            if (current.left != Integer.MIN_VALUE || current.right != Integer.MIN_VALUE) {
                if (explodingLeft) {
                    if (current.rightSnail != null && current.rightSnail != last) {
                        return traverseDown(current.rightSnail, current, true);
                    }
                } else {
                    if (current.leftSnail != null && current.leftSnail != last) {
                        return traverseDown(current.leftSnail, current, false);
                    }
                }

                return current;
            }

            if (explodingLeft) {
                return traverseDown(current.rightSnail, current, true);
            } else {
                return traverseDown(current.leftSnail, current, false);
            }
        }

        private void split(boolean isLeft) {
//            System.out.println("Splitting " + this + " with depth: " + depth);

            int splitNumber;
            if (isLeft) {
                splitNumber = left;
            } else {
                splitNumber = right;
            }

            SnailfishNumber replacement = new SnailfishNumber();
            replacement.left = (int)Math.floor(splitNumber / 2.0);
            replacement.right = (int)Math.ceil(splitNumber / 2.0);
            replacement.depth = depth + 1;
            replacement.parent = this;
//            System.out.println("Made replacement: " + replacement + " with depth: " + depth);

            if (isLeft) {
                leftSnail = replacement;
                left = Integer.MIN_VALUE;
            } else {
                rightSnail = replacement;
                right = Integer.MIN_VALUE;
            }

        }

        public int calculateMagnitude() {
            int leftValue = left;
            if (leftSnail != null) {
                leftValue = leftSnail.calculateMagnitude();
            }

            int rightValue = right;
            if (rightSnail != null) {
                rightValue = rightSnail.calculateMagnitude();
            }

            return 3 * leftValue + 2 * rightValue;
        }

        @Override
        public String toString() {
            StringBuilder b = new StringBuilder();
            b.append("[");
            if (leftSnail != null) {
                b.append(leftSnail);
            }

            if (left != Integer.MIN_VALUE) {
                b.append(left);
            }

            b.append(", ");

            if (rightSnail != null) {
                b.append(rightSnail);
            }

            if (right != Integer.MIN_VALUE) {
                b.append(right);
            }

            b.append("]");

            return b.toString();
        }

        public SnailfishNumber clone(SnailfishNumber parentClone) {
            SnailfishNumber clone = new SnailfishNumber();
            clone.left = left;
            clone.right = right;
            clone.depth = depth;
            if (rightSnail != null) clone.rightSnail = rightSnail.clone(clone);
            if (leftSnail != null) clone.leftSnail = leftSnail.clone(clone);
            clone.parent = parentClone;
            return clone;
        }
    }

    public static void main(String[] args) {
        new DayEighteen().execute();
    }
}
