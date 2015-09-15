package uk.projectchronos.xplorationreader.utils;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * HTTPUtil class.
 *
 * @author pincopallino93
 * @version 1.1
 */
public class HTTPUtil {

    /**
     * This method allows to split and return a map containing qurey parameter as a key and value
     * from the URL passed.
     *
     * @param url the URL to parse and split.
     * @return the map of query parameters
     */
    public static Map<String, List<String>> splitQuery(URL url) {
        final Map<String, List<String>> queryPairs = new LinkedHashMap<>();
        final String[] pairs = url.getQuery().split("&");

        for (String pair : pairs) {
            final int idx = pair.indexOf("=");
            String key;
            try {
                key = idx > 0 ? URLDecoder.decode(pair.substring(0, idx), "UTF-8") : pair;
            } catch (UnsupportedEncodingException e) {
                throw new AssertionError("UTF-8 not supported");
            }

            if (!queryPairs.containsKey(key)) {
                queryPairs.put(key, new LinkedList<String>());
            }

            String value;
            try {
                value = idx > 0 && pair.length() > idx + 1 ? URLDecoder.decode(pair.substring(idx + 1), "UTF-8") : null;
            } catch (UnsupportedEncodingException e) {
                throw new AssertionError("UTF-8 not supported");
            }
            queryPairs.get(key).add(value);
        }

        return queryPairs;
    }
}
