package pt.up.fc.dcc.asura.builder.base.utils.compression;

import pt.up.fc.dcc.asura.builder.base.utils.Strings;

import java.util.HashMap;
import java.util.Map;

public class LZW implements CompressionAlgorithm {
    private static final int DEFAULT_DICT_SIZE = 256;

    @Override
    public String compress(String in, Object...args) {

        // parse args
        int dictSize = args.length > 0 ? (int) args[0] : DEFAULT_DICT_SIZE;

        // encode string in UTF-8
        in = Strings.uft8Encode(in);

        // compress
        StringBuilder out = new StringBuilder();

        int index = dictSize;
        Map<String, Integer> dictionary = new HashMap<>();
        for (int i = 0; i < dictSize; i++)
            dictionary.put("" + (char) i, i);

        String w = "";
        for (char c : in.toCharArray()) {
            String wc = w + c;
            if (dictionary.containsKey(wc))
                w = wc;
            else {
                int i = dictionary.get(w);
                out.append((char) i);

                dictionary.put(wc, index++);
                w = "" + c;
            }
        }

        if (!w.equals("")) {
            int i = dictionary.get(w);
            out.append((char) i);
        }

        return out.toString();
    }

}