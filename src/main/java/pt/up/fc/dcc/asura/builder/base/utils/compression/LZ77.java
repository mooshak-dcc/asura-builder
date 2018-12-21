package pt.up.fc.dcc.asura.builder.base.utils.compression;

import pt.up.fc.dcc.asura.builder.base.utils.Strings;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * Lempel–Ziv 77 (LZ77) is a lossless compression algorithm that replaces
 * repeated occurrences of data with references to a single copy of that data
 * existing earlier in the uncompressed data stream. A match is encoded by a
 * pair of numbers called a length-distance pair, which is equivalent to the
 * statement "each of the next length characters is equal to the characters
 * exactly distance characters behind it in the uncompressed stream".
 *
 * @author José C. Paiva <code>josepaiva94@gmail.com</code>
 */
public class LZ77 implements CompressionAlgorithm {
    private static final int DEFAULT_WINDOW_SIZE = 8192;

    @Override
    public String compress(String in, Object... args) {

        // parse args
        int windowSize = args.length > 0 ? (int) args[0] : DEFAULT_WINDOW_SIZE;

        // encode string in UTF-8
        in = Strings.uft8Encode(in);

        // compress
        StringBuilder out = new StringBuilder();

        int n = in.length();
        for (int i = 0; i < n; i++) {

            char target = in.charAt(i);
            boolean found = false;
            int start = 0;
            int matchLen = 0;
            char nonMatchChar = 0xff;

            for (int s = Math.max(0, i - windowSize); s < i; s++) {
                if (target == in.charAt(s)) {
                    int len = getMatchedLen(in, s + 1, i + 1, n) + 1;
                    if (len > matchLen) {
                        start = i - s;
                        matchLen = len;
                        nonMatchChar = (char) 0xff;
                        if ((i + matchLen) < n) {
                            nonMatchChar = in.charAt(i + matchLen);
                        }
                    }
                    found = true;
                }
            }

            if (found) {
                out.append((char) start)
                        .append((char) matchLen)
                        .append(nonMatchChar);
                i += matchLen;
            } else {
                out.append((char) 0x00).append((char) 0x00).append(target);
            }
        }

        return out.toString();
    }

    private int getMatchedLen(CharSequence src, int i1, int i2, int end) {
        int n = Math.min(i2 - i1, end - i2);
        for (int i = 0; i < n; i++) {
            if (src.charAt(i1++) != src.charAt(i2++)) return i;
        }
        return 0;
    }
}
