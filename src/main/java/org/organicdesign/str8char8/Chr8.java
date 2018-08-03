package org.organicdesign.str8char8;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Pronounced, "Crate", this class represents a UTF-8 character as an offset into an array of bytes.  This allows
 * individual multi-byte characters to use the byte representations in a Str8 and can save some memory allocation.
 * ASCII characters are cached (other common characters may be too).
 */
public class Chr8 {
    private final byte[] bytes;
    private final int offset;
    private Chr8(byte[] bs, int off) { bytes = bs;offset = off; }

    @Override public int hashCode() { return toInt(bytes, offset); }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        return (o instanceof Chr8) &&
               this.hashCode() == o.hashCode();
    }

    // This is a cache of ascii characters to prevent instances of these common characters from being created.
    private static final transient byte[] ASCII_BYTES = new byte[128];
    private static final transient Chr8[] ASCII_CHR_8s = new Chr8[128];
    private static transient final byte[] REPLACEMENT_CHARACTER_BYTES =
            new byte[] {(byte) 0xEF, (byte) 0xBF, (byte) 0xBD};
    private static transient final Chr8 REPLACEMENT_CHARACTER_CHR_8 = new Chr8(REPLACEMENT_CHARACTER_BYTES, 0);
    private static transient final Map<byte[], Chr8> POPULAR_CHARS;

    static {
        for (int i = 0; i < 128; i++) {
            ASCII_BYTES[i] = (byte) i;
            ASCII_CHR_8s[i] = new Chr8(ASCII_BYTES, i);
        }
        Map<byte[], Chr8> tempMap = new HashMap<byte[], Chr8>();
        tempMap.put(REPLACEMENT_CHARACTER_BYTES, REPLACEMENT_CHARACTER_CHR_8);
        POPULAR_CHARS = Collections.unmodifiableMap(tempMap);
    }

    public static final byte HIGH_BIT =    (byte) 0b10000000;
    public static final byte HIGH_2_BITS = (byte) 0b11000000;
    public static final byte HIGH_3_BITS = (byte) 0b11100000;
    public static final byte HIGH_4_BITS = (byte) 0b11110000;
    public static final byte HIGH_5_BITS = (byte) 0b11111000;

    public static final byte LOW_6_BITS = (byte) 0b00111111;
    public static final byte LOW_5_BITS = (byte) 0b00011111;
    public static final byte LOW_4_BITS = (byte) 0b00001111;
    public static final byte LOW_3_BITS = (byte) 0b00000111;

    // Chart from Wikipedia: https://en.wikipedia.org/wiki/UTF-8#Description
    //        Bits
    //         For   First     Last
    //   Num  Code    Code     Code
    // Bytes Point   Point    Point   Byte 1   Byte 2   Byte 3   Byte 4
    // ----- -----  ------   ------ -------- -------- -------- --------
    //     1     7  U+0000   U+007F 0xxxxxxx
    //     2    11  U+0080   U+07FF 110xxxxx 10xxxxxx
    //     3    16  U+0800   U+FFFF 1110xxxx 10xxxxxx 10xxxxxx
    //     4    21 U+10000 U+10FFFF 11110xxx 10xxxxxx 10xxxxxx 10xxxxxx

    public static int numBytes(byte[] bs, int off) {
        byte firstByte = bs[off];
        if ( (firstByte & HIGH_BIT) == 0 ) {
            return 1;
        } else if ( (firstByte & HIGH_3_BITS) == HIGH_2_BITS ) {
            return 2;
        } else if ( (firstByte & HIGH_4_BITS) == HIGH_3_BITS ) {
            return 3;
        } else if ( (firstByte & HIGH_5_BITS) == HIGH_4_BITS ) {
            return 4;
        }
        throw new IllegalArgumentException("Not a valid utf8 character at offset " + off);
    }

    public static int toInt(byte[] bs, int off) {
        byte firstByte = bs[off];
        if ( (firstByte & HIGH_BIT) == 0 ) {
            return (int) firstByte;
        } else if ( (firstByte & HIGH_3_BITS) == HIGH_2_BITS ) {
            int ret = firstByte & LOW_5_BITS;
            ret = ret << 6;
            ret &= bs[off + 1] & LOW_6_BITS;
            return ret;
        } else if ( (firstByte & HIGH_4_BITS) == HIGH_3_BITS ) {
            int ret = firstByte & LOW_4_BITS;
            ret = ret << 6;
            ret &= bs[off + 1] & LOW_6_BITS;
            ret = ret << 6;
            ret &= bs[off + 2] & LOW_6_BITS;
            return ret;
        } else if ( (firstByte & HIGH_5_BITS) == HIGH_4_BITS ) {
            int ret = firstByte & LOW_3_BITS;
            ret = ret << 6;
            ret &= bs[off + 1] & LOW_6_BITS;
            ret = ret << 6;
            ret &= bs[off + 2] & LOW_6_BITS;
            ret = ret << 6;
            ret &= bs[off + 3] & LOW_6_BITS;
            return ret;
        }
        throw new IllegalArgumentException("Not a valid utf8 character at offset " + off);
    }

    /**
     * Public static factory method.  The byte array you pass here is assumed to be immutable, but there is no way to
     * enforce this in Java.  If you pass a byte array that you later change, the behavior of this class will be
     * undefined (that would be bad).
     * @param bs byte array containing the utf8 character to parse - never alter this array once you pass it!
     * @param off the offset of the first byte of the character.
     * @return a Char8 (possibly from cache) representing that character.
     */
    public static Chr8 char8(byte[] bs, int off) {
        if ((bs[off] & HIGH_BIT) == 0) {
            return ASCII_CHR_8s[bs[off]];
        }

        // TODO: Validity check and return the Replacement Character or throw exception if invalid.
        return new Chr8(bs, off);
    }
}
