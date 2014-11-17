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

import org.junit.Test;

import java.util.*;

import static junit.framework.Assert.fail;
import static junit.framework.TestCase.assertEquals;

/**
 * Created by RAS on 11/17/2014.
 */
public class QuickSortTest {
    @Test
    public void testBasicListSort() {
        List<Integer> list = new ArrayList<Integer>();
        list.add(3);
        list.add(1);
        list.add(0);
        list.add(2);
        list.add(3);
        list.add(3);
        list.add(1);
        list.add(0);
        list.add(2);
        list.add(1);
        list.add(0);
        list.add(2);

        SortUtility.quickSort(list, 0, list.size() - 1);

        //Values were chosen to make them coincide with their index
        for (int index = 0; index < list.size(); index++) {
            assertEquals(Integer.valueOf(index / 3), list.get(index));
        }
    }

    @Test
    public void testListSortWithComparator() {
        List<String> list = new ArrayList<String>();
        list.add("A");
        list.add("B");
        list.add("C");

        //Reverse order
        Comparator<String> comparator = new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o2.compareTo(o1);
            }
        };

        SortUtility.quickSort(list, comparator, 0, list.size() - 1);
        assertEquals("C", list.get(0));
        assertEquals("B", list.get(1));
        assertEquals("A", list.get(2));
    }

    @Test
    public void testRandomListSort() {
        Random random = new Random();
        List<Integer> quickSorted = new ArrayList<Integer>();
        List<Integer> standardJavaSorted = new ArrayList<Integer>();

        for (int i = 0; i < 20; i++) {
            int next = random.nextInt(5);
            quickSorted.add(next);
            standardJavaSorted.add(next);
        }

        SortUtility.quickSort(quickSorted, 0, quickSorted.size() - 1);
        Collections.sort(standardJavaSorted);

        assertEquals(standardJavaSorted, quickSorted);
    }


    @Test
    public void testListNull() {
        try {
            SortUtility.quickSort((List<String>)null, 0, 0);
        }
        catch (Throwable t) {
            fail("An error should not be thrown even if the list is null");
        }
    }

    @Test
    public void testListEmpty() {
        try {
            SortUtility.quickSort(new ArrayList<String>(), 0, 0);
        }
        catch (Throwable t) {
            fail("An error should not be thrown even if the list is empty");
        }
    }

    @Test
    public void testSubsetSort() {
        List<Integer> list = new ArrayList<Integer>();
        list.add(9);
        list.add(3);
        list.add(3);
        list.add(1);
        list.add(0);
        list.add(2);
        list.add(1);
        list.add(0);
        list.add(2);
        list.add(-1);

        //Sort all but the first and last element
        SortUtility.quickSort(list, 1, list.size() - 2);

        assertEquals(9, list.remove(0).intValue());
        assertEquals(-1, list.remove(list.size() - 1).intValue());

        //Values were chosen to make them coincide with their index after start and end were removed
        for (int index = 0; index < list.size(); index++) {
            assertEquals(Integer.valueOf(index / 2), list.get(index));
        }
    }
}
