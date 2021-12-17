package adventofcode;

import java.util.*;

public class DaySixteen extends AbstractAdventOfCode {

    @Override
    public Object solvePartOne() throws Exception {
        String hexstring = readInput("input/day16/data.txt").get(0);
        Packet p = parse(new BitArray(hexStringToByteArray(hexstring)));
        return versionCount(p);
    }

    @Override
    public Object solvePartTwo() throws Exception {
        String hexstring = readInput("input/day16/data.txt").get(0);
        Packet p = parse(new BitArray(hexStringToByteArray(hexstring)));
        return p.evaluate();
    }

    private int versionCount(Packet head) {
        if (head.subPackets.size() == 0) {
            return head.getVersion();
        }

        int version = head.getVersion();
        for (Packet p : head.subPackets) {
            version += versionCount(p);
        }
        return version;
    }

    private Packet parse(BitArray bits) {
        return parsePacket(bits, 0).packet;
    }

    private PacketResult parsePacket(BitArray bits, int i) {
        Packet p = new Packet();

        byte version = (byte)bits.getNextBits(i, 3);
        p.setVersion(version);
        i += 3;

        byte type = (byte)bits.getNextBits(i, 3);
        p.setType(type);
        i += 3;
        if (type == 4) {
            boolean lastGroup = false;
            while(!lastGroup) {
                lastGroup = bits.getNextBits(i, 1) <= 0;
                i += 1;
                byte value = (byte)bits.getNextBits(i, 4);
                i += 4;
                p.addByte(value);
            }
        } else {
            byte lengthTypeId = (byte)bits.getNextBits(i, 1);
            i += 1;
            if (lengthTypeId == 0) {
                int totalLengthOfBits = bits.getNextBits(i, 15);
                i += 15;

                int currentBits = 0;
                while (currentBits != totalLengthOfBits) {
                    PacketResult pr = parsePacket(bits, i);
                    p.addSubPacket(pr.packet);
                    currentBits += pr.newIdx - i;
                    i = pr.newIdx;
                }
            } else {
                int numberOfSubPackets = bits.getNextBits(i, 11);
                i += 11;
                for (int j = 0; j < numberOfSubPackets; j++) {
                    PacketResult pr = parsePacket(bits, i);
                    p.addSubPacket(pr.packet);
                    i = pr.newIdx;
                }
            }
        }

        PacketResult res = new PacketResult();
        res.packet = p;
        res.newIdx = i;
        return res;
    }

    private static class PacketResult {
        Packet packet;
        int newIdx;
    }

    private static class Packet {

        private byte version;
        private byte type;
        private List<Byte> payload = new ArrayList<>();
        private List<Packet> subPackets = new ArrayList<>();

        public long evaluate() {
            if (type == 4) {
                return getPayloadValue();
            }

            List<Long> values = new ArrayList<>();
            for (Packet p : subPackets) {
                values.add(p.evaluate());
            }

            long result;
            if (type == 0) {
                result = values.stream().mapToLong(Long::longValue).sum();
            } else if (type == 1) {
                long product = 1;
                for (long val : values) {
                    product *= val;
                }
                result = product;
            } else if (type == 2) {
                result = values.stream().mapToLong(Long::longValue).min().orElseThrow();
            } else if (type == 3) {
                result = values.stream().mapToLong(Long::longValue).max().orElseThrow();
            } else if (type == 5) {
               long first = values.get(0);
               long second = values.get(1);
               result = first > second ? 1 : 0;
            } else if (type == 6) {
                long first = values.get(0);
                long second = values.get(1);
                result = first < second ? 1 : 0;
            } else if (type == 7) {
                long first = values.get(0);
                long second = values.get(1);
                result = first == second ? 1 : 0;
            } else {
                throw new IllegalStateException("Type out of range.");
            }
            return result;
        }

        public void addSubPacket(Packet p) {
            subPackets.add(p);
        }

        public byte getVersion() {
            return version;
        }

        public void setVersion(byte version) {
            this.version = version;
        }

        public void setType(byte type) {
            this.type = type;
        }

        public void addByte(byte b) {
            payload.add(b);
        }

        private long getPayloadValue() {
            StringBuilder payloadStr = new StringBuilder(payload.size() * 4);
            for (Byte b : payload) {
                String thisByte = String.format("%4s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
                payloadStr.append(thisByte);
            }
            if (payloadStr.toString().equals("")) {
                return -1;
            }
            return Long.parseLong(payloadStr.toString(), 2);
        }
    }

    private static class BitArray {
        private Bit[] bits;
        public BitArray(byte[] bytes) {
            List<Bit> bits = new ArrayList<>();
            for (int i = 0; i < bytes.length; i++) {
                for (int j = 0x80; j > 0; j >>= 1) {
                    bits.add(new Bit(bytes[i] & j));
                }
            }
            this.bits = bits.toArray(new Bit[0]);
        }
        public int getNextBits(int idx, int numBits) {
            int b = 0;
            for (int i = 0; i < numBits; i++) {
                b |= (bits[idx + i].getValue() << (numBits - i - 1));
            }
            return b;
        }

    }

    private static class Bit {
        private final byte value;
        public Bit(int value) {
            this.value = value > 0 ? (byte)1 : (byte)0;
        }
        public byte getValue() {
            return value;
        }
    }

    private byte[] hexStringToByteArray(String str) {
        int len = str.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(str.charAt(i), 16) << 4) +
                    Character.digit(str.charAt(i + 1), 16));
        }
        return data;
    }

    public static void main(String[] args) {
        new DaySixteen().execute();
    }
}
