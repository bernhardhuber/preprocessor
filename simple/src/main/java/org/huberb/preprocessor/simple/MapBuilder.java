/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.huberb.preprocessor.simple;

import java.util.HashMap;
import java.util.Map;

/**
 * Simple builder for creating Map with keys in the form like "${" + key +"}".
 * The begin-("${"), and end-marker ("}") is configurable,
 * using delimiter( "${", "}"), or delimiter( "${*}").
 * 
 * @author berni3
 */
public class MapBuilder {

    private final Map<String, String> m = new HashMap<>();
    private final char beginEndMarkerDelimiterStar = '*';
    private String delimiter = "${" + beginEndMarkerDelimiterStar + "}";

    public MapBuilder delimiter(String b, String e) {
        this.delimiter = b + beginEndMarkerDelimiterStar + e;
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
        final String[] beginEndMarker = calcBeginEndMarker(this.delimiter);
        final Map<String, String> result = new HashMap<>();
        m.entrySet().stream().forEach((Map.Entry<String, String> t) -> {
            final String delimitedKey = beginEndMarker[0] + t.getKey() + beginEndMarker[1];
            result.put(delimitedKey, t.getValue());
        });
        return result;
    }

    String[] calcBeginEndMarker(String delimiter) {
        final String[] beginEndMarkers;
        final int index = delimiter.indexOf("" + beginEndMarkerDelimiterStar);
        if (index == -1) {
            beginEndMarkers = new String[]{delimiter, delimiter};
        } else {
            final String beginMarker = delimiter.substring(0, index);
            final String endMarker = delimiter.substring(index + 1, delimiter.length());
            beginEndMarkers = new String[]{beginMarker, endMarker};
        }
        return beginEndMarkers;
    }
}
