// The MIT License (MIT)
//
// Copyright (c) 2015 Claudio Pastorini
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.

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
