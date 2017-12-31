/*
 * Copyright 2016 Open Geospatial Consortium.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.opengis.cite.sta10.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author jab
 */
public class Utils {

    private Utils() {
    }

    public static String urlEncode(String link) {
        try {
            return URLEncoder.encode(link, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException ex) {
        }
        return link;
    }

    /**
     * Quote the ID for use in json, if needed.
     *
     * @param id The id to quote.
     * @return The quoted id.
     */
    public static String quoteIdForJson(Object id) {
        if (id instanceof Number) {
            return id.toString();
        }
        return "\"" + id + "\"";
    }

    /**
     * Quote the ID for use in URLs, if needed.
     *
     * @param id The id to quote.
     * @return The quoted id.
     */
    public static String quoteIdForUrl(Object id) {
        if (id instanceof Number) {
            return id.toString();
        }
        return "'" + id + "'";
    }

    public static Object idObjectFromPostResult(String postResultLine) {
        int pos1 = postResultLine.lastIndexOf("(") + 1;
        int pos2 = postResultLine.lastIndexOf(")");
        String part = postResultLine.substring(pos1, pos2);
        try {
            return Long.parseLong(part);
        } catch (NumberFormatException exc) {
            // Id was not a long, thus a String.
            if (!part.startsWith("'") || !part.endsWith("'")) {
                throw new IllegalArgumentException("Strings in urls must be quoted with single quotes.");
            }
            return part.substring(1, part.length() - 1);
        }
    }
}
