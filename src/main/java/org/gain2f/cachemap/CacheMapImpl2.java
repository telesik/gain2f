package org.gain2f.cachemap;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Kiselov on 2/11/2016.
 */
public class CacheMapImpl2<KeyType, ValueType> implements CacheMap<KeyType, ValueType> {
    private volatile long timeToLive;
    private LinkedHashMap<KeyType, ValueType> map = new LinkedHashMap<>();
    private List<KeyType> keys = new LinkedList<>();
    private volatile int startIndex;

    @Override
    public void setTimeToLive(long timeToLive) {

    }

    @Override
    public long getTimeToLive() {
        return 0;
    }

    @Override
    public ValueType put(KeyType key, ValueType value) {
        keys.add(key);
        return map.put(key, value);
    }

    @Override
    public void clearExpired() {

    }

    @Override
    public void clear() {

    }

    @Override
    public boolean containsKey(Object key) {
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public ValueType get(Object key) {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public ValueType remove(Object key) {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }
}
