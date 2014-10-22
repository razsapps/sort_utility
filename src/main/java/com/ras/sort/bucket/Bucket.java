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

package com.ras.sort.bucket;

import java.util.Arrays;

/**
 * Created by RAS on 4/24/2014.
 */
public final class Bucket<T> {
    private T[] m_items;

    public Bucket(T... items) {
        m_items = items;
    }

    public int getBucketItemCount() {
        return m_items.length;
    }

    public T getBucketItem(int index) {
        return m_items[index];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Bucket that = (Bucket) o;

        if (!Arrays.equals(m_items, that.m_items)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(m_items);
    }
}
