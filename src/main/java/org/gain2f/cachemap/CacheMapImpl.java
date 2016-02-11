package org.gain2f.cachemap;

import java.time.Duration;
import java.time.Instant;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Created by Kiselov on 2/11/2016.
 */
public class CacheMapImpl<KeyType, ValueType> implements CacheMap<KeyType, ValueType> {
    private volatile long timeToLive;
    private LinkedHashMap<KeyType, WrappedValue> map = new LinkedHashMap<>();

    private class WrappedValue {
        transient Instant fixedTime;
        ValueType value;

        private WrappedValue(ValueType value) {
            this.value = value;
            //this.fixedTime = Instant.now();
        }

        WrappedValue fixTime() {
            this.fixedTime = Instant.now();
            return this;
        }

        boolean isExpired() {
            return Duration.between(Instant.now(), fixedTime).toMillis() - timeToLive > 0;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            WrappedValue that = (WrappedValue) o;

            return value != null ? value.equals(that.value) : that.value == null;

        }

        @Override
        public int hashCode() {
            return value != null ? value.hashCode() : 0;
        }
    }

    @SuppressWarnings("unchecked")
    private WrappedValue wrap(Object value) {
        return new WrappedValue((ValueType) value);
    }

    public void setTimeToLive(long timeToLive) {
        this.timeToLive = timeToLive;
    }

    public long getTimeToLive() {
        return this.timeToLive;
    }

    public ValueType put(KeyType key, ValueType value) {
        return map.put(key, wrap(value).fixTime()).value;
    }

    public CacheMap<KeyType, ValueType> _clearExpired() {
        Iterator<Map.Entry<KeyType, WrappedValue>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getValue().isExpired()) {
                iterator.remove();
            }
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
        Iterator<WrappedValue> iterator = map.values().stream().filter(new Predicate<WrappedValue>() {
            @Override
            public boolean test(WrappedValue valueWrapper) {
                return valueWrapper.equals(value);
            }
        }).iterator();

        while (iterator.hasNext()) {
            if (!iterator.next().isExpired()) return true;
            iterator.remove();
        }
        return false;
    }

    public ValueType get(Object key) {
        WrappedValue wrappedValue = getWrappedValue(key);
        return wrappedValue != null ? wrappedValue.value : null;
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public ValueType remove(Object key) {
        WrappedValue wrappedValue = map.remove(key);
        return wrappedValue != null ? wrappedValue.value : null;
    }

    public int size() {
        return map.size();
    }
}
