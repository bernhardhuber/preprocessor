/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.huberb.preprocessor.simple;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author berni3
 */
public class MapBuilder {

    private final Map<String, String> m = new HashMap<>();
    private String delimiter = "${*}";

    public MapBuilder delimiter(String b, String e) {
        this.delimiter = b + "*" + e;
        return this;
    }

    public MapBuilder delimiter(String delimiter) {
        this.delimiter = delimiter;
        return this;
    }

    public MapBuilder add(String key, String value) {
        m.put(key, value);
        return this;
    }

    public Map<String, String> build() {
        String[] beginEndMarker = calcBeginEndMarker(this.delimiter);
        Map<String, String> result = new HashMap<>();
        m.entrySet().stream().forEach((Map.Entry<String, String> t) -> {
            final String delimitedKey = beginEndMarker[0] + t.getKey() + beginEndMarker[1];
            result.put(delimitedKey, t.getValue());
        });
        return result;
    }

    String[] calcBeginEndMarker(String delimiter) {
        final String[] beginEndMarker;
        int index = delimiter.indexOf('*');
        if (index == -1) {
            beginEndMarker = new String[]{delimiter, delimiter};
        } else {
            String beginMarker = delimiter.substring(0, index);
            String endMarker = delimiter.substring(index + 1, delimiter.length());
            beginEndMarker = new String[]{beginMarker, endMarker};
        }
        return beginEndMarker;
    }
}
