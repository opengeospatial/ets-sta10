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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author jab
 */
public class Utils {

    private Utils() {
    }

    public static <T> Set<Set<T>> powerSet(Set<T> originalSet) {
        Set<Set<T>> sets = new HashSet<>();
        if (originalSet.isEmpty()) {
            sets.add(new HashSet<>());
            return sets;
        }
        List<T> list = new ArrayList<>(originalSet);
        T head = list.get(0);
        Set<T> rest = new HashSet<>(list.subList(1, list.size()));
        for (Set<T> set : powerSet(rest)) {
            Set<T> newSet = new HashSet<>();
            newSet.add(head);
            newSet.addAll(set);
            sets.add(newSet);
            sets.add(set);
        }
        return sets;
    }

    public static <T> Collection<? extends List<T>> permutations(List<? extends Collection<T>> collections) {
        if (collections == null || collections.isEmpty()) {
            return Collections.emptyList();
        } else {
            Collection<List<T>> res = new LinkedList<>();
            permutationsImpl(collections, res, 0, new LinkedList<T>());
            return res;
        }
    }

    private static <T> void permutationsImpl(List<? extends Collection<T>> ori, Collection<List<T>> res, int d, List<T> current) {
        if (d == ori.size()) {
            res.add(current);
            return;
        }
        Collection<T> currentCollection = ori.get(d);
        for (T element : currentCollection) {
            List<T> copy = new LinkedList<>(current);
            copy.add(element);
            permutationsImpl(ori, res, d + 1, copy);
        }
    }
}
