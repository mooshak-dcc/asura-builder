package pt.up.fc.dcc.asura.builder.base.utils;

/**
 * Utilities for manipulating strings
 *
 * @author José Carlos Paiva <code>josepaiva94@gmail.com</code>
 */
public class Strings {


    /**
     * Encode utf8 string into char 0 ~ 127
     *
     * @param text {@link String} the text to encode
     * @return {@link String} the encoded text
     */
    public static String uft8Encode(String text) {

        StringBuilder res = new StringBuilder();

        for (char c : text.toCharArray()) {
            int i = (int) c;
            if (i < 128) {
                res.append((char) i);
            } else if (i < 2048) {
                int j = (i >> 6) | 192;
                res.append((char) j);
                j = (i & 63) | 128;
                res.append((char) j);
            } else {
                int j = (i >> 12) | 224;
                res.append((char) j);
                j = ((i >> 6) & 63) | 128;
                res.append((char) j);
                j = (c & 63) | 128;
                res.append((char) j);
            }
        }

        return res.toString();
    }
}
