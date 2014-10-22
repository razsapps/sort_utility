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

import junit.framework.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static junit.framework.Assert.fail;
import static junit.framework.TestCase.assertEquals;

/**
 * Created by RAS on 4/24/2014.
 */
public class CountSortTest {
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

        SortUtility.count(list);

        for (int index = 0; index < list.size(); index++) {
            assertEquals(Integer.valueOf(index / 3), list.get(index));
        }
    }

    @Test
    public void testRandomListSort() {
        Random random = new Random();
        List<Integer> countSorted = new ArrayList<Integer>();
        List<Integer> standardJavaSorted = new ArrayList<Integer>();

        for (int i = 0; i < 20; i++) {
            int next = random.nextInt(5);
            countSorted.add(next);
            standardJavaSorted.add(next);
        }

        SortUtility.count(countSorted);
        Collections.sort(standardJavaSorted);

        assertEquals(standardJavaSorted, countSorted);
    }


    @Test
    public void testListNull() {
        try {
            SortUtility.count((List<String>)null);
        }
        catch (Throwable t) {
            fail("An error should not be thrown even if the list is null");
        }
    }

    @Test
    public void testListEmpty() {
        try {
            SortUtility.count(new ArrayList<String>());
        }
        catch (Throwable t) {
            fail("An error should not be thrown even if the list is empty");
        }
    }
}
