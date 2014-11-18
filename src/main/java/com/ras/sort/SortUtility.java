/*
 * Copyright 2014 Richard So
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ras.sort;

import com.ras.sort.bucket.Bucket;
import com.ras.sort.bucket.IBucketProvider;

import java.util.*;

/**
 * Created by RAS on 4/23/2014.
 */
public class SortUtility {
    public static <T extends Comparable<? super T>> void merge(List<T> list) {
        //Standard java uses merge sort
        Collections.sort(list);
    }

    public static <T> void merge(List<T> list, Comparator<? super T> c) {
        //Standard java uses merge sort
        Collections.sort(list, c);
    }

    /**
     * Sorts the specified list into ascending order, according to the natural ordering of its elements. All elements
     * in the list must implement the Comparable interface. Furthermore, all elements in the list must be mutually
     * comparable (that is, e1.compareTo(e2) must not throw a ClassCastException for any elements e1 and e2 in the list).
     * All elements that are considered the same value must have the same {@link Object#hashCode()}.
     *
     * This sort is not stable.
     *
     * The specified list must be modifiable, but need not be resizable.
     *
     * The sorting algorithm is a count sort. Each unique item (specified by {@link Object#hashCode()}) is counted how
     * many times it appears. The unique items are sorted using {@link java.util.Collections#sort(java.util.List)}. This
     * algorithm has a guaranteed O(n) + O(k log(k)) performance where n is the amount of elements in the list and k is
     * the amount of unique elements in the list.
     * @param list the list of comparable objects to be sorted
     */
    public static <T extends Comparable<? super T>> void count(List<T> list) {
        if (list == null || list.isEmpty())
            return;

        HashMap<T, Integer> counts = new HashMap<T, Integer>();
        NullCountZeroHandler<T> handler = new NullCountZeroHandler<T>();
        countGenerator(list, counts, handler);

        ArrayList<T> keys = handler.getKeys();
        Collections.sort(keys);

        countResetList(list, counts, keys);
    }

    /**
     * Sorts the specified list into ascending order, according to the order provided by orderedUniques. All elements
     * in the list must have a representative value in orderedUniques. All elements that are considered the same value
     * must have the same {@link Object#hashCode()} across the list and orderedUniques.
     *
     * This sort is not stable.
     *
     * The specified list must be modifiable, but need not be resizable.
     *
     * The sorting algorithm is a count sort. Each unique item (specified by {@link Object#hashCode()} and provided by
     * orderedUniques) is counted how many times it appears. This algorithm has a guaranteed O(n) performance.
     * @param list The list of objects to be sorted
     * @param orderedUniques A collection of unique objects found in the list in the order they should be sorted
     */
    public static <T> void count(List<T> list, Collection<T> orderedUniques) {
        if (list == null || list.isEmpty())
            return;
        else if (orderedUniques == null || orderedUniques.isEmpty())
            throw new IllegalArgumentException("The orderedUniques parameter must contain data.");

        HashMap<T, Integer> counts = new HashMap<T, Integer>();

        for (T key: orderedUniques)
            counts.put(key, Integer.valueOf(0));

        INullCountHandler handler = new NullCountExceptionHandler();
        countGenerator(list, counts, handler);

        countResetList(list, counts, orderedUniques);
    }

    private static <T> void countGenerator(List<T> list, HashMap<T, Integer> counts, INullCountHandler handler) {
        for (T item: list) {
            Integer count = counts.get(item);
            if (count == null)
                count = handler.countNull(item);

            count = Integer.valueOf(count.intValue() + 1);
            counts.put(item, count);
        }
    }

    private static <T> void countResetList(List<T> list, HashMap<T, Integer> counts, Collection<T> orderedUniques) {
        ListIterator<T> iterator = list.listIterator();
        for (T key: orderedUniques) {
            int count = counts.get(key).intValue();
            for (int i = 0; i < count; i++) {
                iterator.next();
                iterator.set(key);
            }
        }
    }

    private static interface INullCountHandler<T> {
        public Integer countNull(T item);
    }

    private static class NullCountZeroHandler<T> implements INullCountHandler<T> {
        private ArrayList<T> keys = new ArrayList<T>();

        @Override
        public Integer countNull(T item) {
            //If the value is null return a 0
            keys.add(item);
            return Integer.valueOf(0);
        }

        private ArrayList<T> getKeys() {
            return keys;
        }
    }

    private static class NullCountExceptionHandler<T> implements INullCountHandler<T> {
        @Override
        public Integer countNull(T item) {
            //If value is null throw an exception
            throw new IllegalArgumentException(item + " is not present in the orderedUniques parameter.");
        }
    }

    /**
     * Sorts the specified list into ascending order, according to the order provided by orderedUniques. All elements
     * in the list must have a representative value in orderedUniques. All elements that are considered the same value
     * must have the same {@link Object#hashCode()} across the list and orderedUniques.
     *
     * This sort is not stable.
     *
     * The specified list must be modifiable, but need not be resizable.
     *
     * The sorting algorithm is a count sort. Each unique item (specified by {@link Object#hashCode()} and provided by
     * orderedUniques) is counted how many times it appears. This algorithm has a guaranteed O(n) performance.
     * @param list the list to be sorted
     * @param bucketComparator A comparator to put the unique bucket values in correct sort order
     */
    public static <E extends IBucketProvider<T>, T> void bucket(List<E> list, Comparator<Bucket<T>> bucketComparator) {
        if (list == null || list.isEmpty())
            return;

        HashMap<Bucket<T>, LinkedList<E>> buckets = new HashMap<Bucket<T>, LinkedList<E>>();

        for(E item: list) {
            Bucket<T> bucket = item.getBucket();
            LinkedList<E> bucketList = buckets.get(bucket);
            if (bucketList == null) {
                bucketList = new LinkedList<E>();
                buckets.put(bucket, bucketList);
            }
            bucketList.add(item);
        }

        ArrayList<Bucket<T>> keys = new ArrayList<Bucket<T>>(buckets.keySet());
        Collections.sort(keys, bucketComparator);

        ListIterator<E> iterator = list.listIterator();
        for (Bucket<T> key: keys) {
            LinkedList<E> bucketList = buckets.get(key);
            for (E item: bucketList) {
                iterator.next();
                iterator.set(item);
            }
        }
    }

    /**
     * Sorts the specified list into ascending order, according to the natural ordering of its elements. All elements
     * in the list must implement the Comparable interface. Furthermore, all elements in the list must be mutually
     * comparable (that is, e1.compareTo(e2) must not throw a ClassCastException for any elements e1 and e2 in the list).
     *
     * This sort is not stable.
     *
     * The specified list must be modifiable, but need not be resizable.
     *
     * The sorting algorithm is a quick sort. This algorithm has an average case of O(n log(n)) and a worst case of
     * O(n^2) performance. The algorithm sorts in place thus using less memory overhead.
     *
     * @param list the list of comparable objects to be sorted
     * @param startIndex This is the starting index of the list where to start quicksort
     * @param endIndex This is the ending index of the list where to end quicksort
     */
    public static <T extends Comparable<? super T>> void quickSort(List<T> list, int startIndex, int endIndex) {
        quickSort(list, new ComparableComparator<T>(), startIndex, endIndex);
    }

    /**
     * Sorts the specified list into ascending order, according to the comparator.
     *
     * This sort is not stable.
     *
     * The specified list must be modifiable, but need not be resizable.
     *
     * The sorting algorithm is a quick sort. This algorithm has an average case of O(n log(n)) and a worst case of
     * O(n^2) performance. The algorithm sorts in place thus using less memory overhead.
     *
     * @param list the list of comparable objects to be sorted
     * @param comparator the comparator for the class the list specifies
     * @param startIndex This is the starting index of the list where to start quicksort
     * @param endIndex This is the ending index of the list where to end quicksort
     */
    public static <T> void quickSort(List<T> list, Comparator<T> comparator, int startIndex, int endIndex) {
        if (list == null || list.isEmpty() || startIndex == endIndex || (endIndex - startIndex == 1))
            return;
        else if (endIndex >= list.size() || endIndex < 0)
            throw new ArrayIndexOutOfBoundsException(endIndex + " is less than zero or is greater than list size: " + list.size());
        else if (startIndex < 0)
            throw new ArrayIndexOutOfBoundsException(startIndex + " is less than zero.");
        else if (startIndex > endIndex)
            throw new IllegalArgumentException("startIndex cannot be greater than endIndex");

        int middleIndex = startIndex + (endIndex - startIndex)/2;
        int pivotIndex = middleIndex;

        //select the median value as the pivot index.  This is for efficiency
        T startValue = list.get(startIndex);
        T middleValue = list.get(middleIndex);
        T endValue = list.get(endIndex);
        T pivotValue = middleValue;
        if (comparator.compare(middleValue, startValue) < 0 && comparator.compare(startValue, endValue) > 0) {
            pivotIndex = startIndex;
            pivotValue = startValue;
        }
        else if (comparator.compare(middleValue, endValue) < 0 && comparator.compare(endValue, startValue) > 0) {
            pivotIndex = endIndex;
            pivotValue = endValue;
        }

        Collections.swap(list, pivotIndex, endIndex);
        int swapIndex = startIndex;
        for (int i = startIndex; i < endIndex; i++) {
            if (comparator.compare(list.get(i), pivotValue) < 0) {
                Collections.swap(list, i, swapIndex);
                swapIndex++;
            }
        }
        Collections.swap(list, swapIndex, endIndex);

        if (swapIndex - 1 > startIndex)
            quickSort(list, comparator, startIndex, swapIndex - 1);
        if (swapIndex + 1 < endIndex)
            quickSort(list, comparator, swapIndex + 1, endIndex);
    }

    public static <T extends Comparable<? super T>> void bubbleSort(List<T> list) {
        bubbleSort(list, new ComparableComparator<T>());
    }

    public static <T> void bubbleSort(List<T> list, Comparator<T> comparator) {
        if (list == null || list.isEmpty()) {
            return;
        }

        boolean changes = true;
        while (changes) {
            changes = false;

            for (int i = 1; i < list.size(); i++) {
                if (comparator.compare(list.get(i - 1), list.get(i)) > 0) {
                    Collections.swap(list, i -1, i);
                    changes = true;
                }
            }
        }
    }

    private static class ComparableComparator<T extends Comparable> implements Comparator<T> {
        @Override
        public int compare(T o1, T o2) {
            return o1.compareTo(o2);
        }
    }
}
