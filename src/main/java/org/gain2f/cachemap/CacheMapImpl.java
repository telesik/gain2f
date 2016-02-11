package org.gain2f.cachemap;

import org.gain2f.cachemap.exceptions.CacheMapException;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Kiselov on 2/11/2016.
 */
public class CacheMapImpl<KeyType, ValueType> implements CacheMap<KeyType, ValueType> {
    private volatile long timeToLive;
    LinkedHashMap<KeyType, WrappedValue> map = new LinkedHashMap<>();

    private class WrappedValue {
        //        transient Instant fixedTime;
        transient long fixedTime;
        ValueType value;

        private WrappedValue(ValueType value) {
            this.value = value;
//            this.fixedTime = Instant.now();
            this.fixedTime = Clock.getTime();
        }

        boolean isExpired() {
//            return timeToLive - Duration.between(fixedTime, Instant.now()).toMillis() < 0;
            return timeToLive - Clock.getTime() + fixedTime < 0;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            WrappedValue that = (WrappedValue) o;

            return valueEquals(that.value);
        }

        @Override
        public int hashCode() {
            return value != null ? value.hashCode() : 0;
        }

        public boolean valueEquals(Object value) {
            return this.value != null && this.value.equals(value);
        }

        @Override
        public String toString() {
            return "WrappedValue{" +
                    "fixedTime=" + fixedTime +
                    ", value=" + value +
                    '}';
        }
    }

    @SuppressWarnings("unchecked")
    private WrappedValue wrap(Object value) {
        return new WrappedValue((ValueType) value);
    }

    public void setTimeToLive(long timeToLive) {
        if (timeToLive < 0) throw new CacheMapException("Time to live could not be less than zero");
        this.timeToLive = timeToLive;
    }

    public long getTimeToLive() {
        return this.timeToLive;
    }

    public ValueType put(KeyType key, ValueType value) {
        WrappedValue wrappedValue = map.remove(key);
        map.put(key, wrap(value));
        return extractValue(wrappedValue);
    }

    public CacheMapImpl<KeyType, ValueType> _clearExpired() {
        Iterator<Map.Entry<KeyType, WrappedValue>> iterator = map.entrySet().iterator();
        while (iterator.hasNext() && iterator.next().getValue().isExpired()) {
            iterator.remove();
        }
        return this;
    }

    public void clearExpired() {
        _clearExpired();
    }

    public void clear() {
        map.clear();
    }

    public boolean containsKey(Object key) {
        return this.getWrappedValue(key) != null;
    }

    private WrappedValue getWrappedValue(Object key) {
        WrappedValue wrappedValue = map.get(key);
        if (wrappedValue != null) {
            if (wrappedValue.isExpired()) {
                map.remove(key);
                return null;
            }
            return wrappedValue;
        }
        return null;
    }

    public boolean containsValue(final Object value) {
        return _clearExpired().map.values().stream().filter(valueWrapper -> valueWrapper.valueEquals(value)).findAny().isPresent();
    }

    public ValueType get(Object key) {
        WrappedValue wrappedValue = getWrappedValue(key);
        return wrappedValue != null ? wrappedValue.value : null;
    }

    public boolean isEmpty() {
        return _clearExpired().map.isEmpty();
    }

    public ValueType remove(Object key) {
        WrappedValue wrappedValue = map.remove(key);
        return extractValue(wrappedValue);
    }

    private ValueType extractValue(WrappedValue wrappedValue) {
        return wrappedValue != null
                ? wrappedValue.isExpired() ? null : wrappedValue.value
                : null;
    }

    public int size() {
        return _clearExpired().map.size();
    }
}
