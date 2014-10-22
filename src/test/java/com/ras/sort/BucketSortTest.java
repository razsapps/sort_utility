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
import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by RAS on 4/28/2014.
 */
public class BucketSortTest {
    @Test
    public void testBasicListSort() {
        List<Person> list = new ArrayList<Person>();
        list.add(new Person(7, 40, 60));
        list.add(new Person(6, 25, 60));
        list.add(new Person(0, 13, 48));
        list.add(new Person(3, 18, 70));
        list.add(new Person(2, 18, 62));
        list.add(new Person(5, 21, 10));
        list.add(new Person(4, 18, 70));
        list.add(new Person(1, 18, 55));

        SortUtility.bucket(list, new Comparator<Bucket<Integer>>() {
            @Override
            public int compare(Bucket<Integer> o1, Bucket<Integer> o2) {
                for (int i = 0; i < o1.getBucketItemCount(); i++) {
                    int compare = o1.getBucketItem(i) - o2.getBucketItem(i);
                    if (compare != 0)
                        return compare;
                }
                return 0;
            }
        });

        for (int i = 0; i < list.size(); i++) {
            assertEquals(i, list.get(i).getId());
        }
    }

    private static class Person implements IBucketProvider<Integer> {
        private int m_id;
        private int m_age;
        private int m_heightInInches;

        public Person(int id, int age, int heightInInches) {
            m_id = id;
            m_age = age;
            m_heightInInches = heightInInches;
        }

        public int getId() {
            return m_id;
        }

        @Override
        public Bucket<Integer> getBucket() {
            return new Bucket<Integer>(m_age, m_heightInInches);
        }
    }
}
