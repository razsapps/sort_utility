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
public class BubbleSortTest {
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

        SortUtility.bubbleSort(list);

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

        SortUtility.bubbleSort(list, comparator);
        assertEquals("C", list.get(0));
        assertEquals("B", list.get(1));
        assertEquals("A", list.get(2));
    }

    @Test
    public void testRandomListSort() {
        Random random = new Random();
        List<Integer> bubbleSorted = new ArrayList<Integer>();
        List<Integer> standardJavaSorted = new ArrayList<Integer>();

        for (int i = 0; i < 20; i++) {
            int next = random.nextInt(5);
            bubbleSorted.add(next);
            standardJavaSorted.add(next);
        }

        SortUtility.bubbleSort(bubbleSorted);
        Collections.sort(standardJavaSorted);

        assertEquals(standardJavaSorted, bubbleSorted);
    }


    @Test
    public void testListNull() {
        try {
            SortUtility.bubbleSort((List<String>)null);
        }
        catch (Throwable t) {
            fail("An error should not be thrown even if the list is null");
        }
    }

    @Test
    public void testListEmpty() {
        try {
            SortUtility.bubbleSort(new ArrayList<String>());
        }
        catch (Throwable t) {
            fail("An error should not be thrown even if the list is empty");
        }
    }
}
