package org.gain2f.cachemap;

import junit.framework.TestCase;

import java.util.Iterator;

/**
 * JUnit test case for a CacheMap implementation.
 * <p>
 * Feel free to add more methods.
 */
public class CacheMap_UnitTest extends TestCase {
    CacheMap<Integer, String> cache = new CacheMapImpl<>();
    final static long TIME_TO_LIVE = 1000;

    public void setUp() throws Exception {
        Clock.setTime(1000);

        //TODO instantiate cache object

        cache.setTimeToLive(TIME_TO_LIVE);
    }

    public void testExpiry() throws Exception {
        cache.put(1, "apple");
        assertEquals("apple", cache.get(1));
        assertFalse(cache.isEmpty());
        Clock.setTime(3000);
        assertNull(cache.get(1));
        assertTrue(cache.isEmpty());

    }

    public void testSize() throws Exception {
        assertEquals(0, cache.size());
        cache.put(1, "apple");
        assertEquals(1, cache.size());
        Clock.setTime(3000);
        assertEquals(0, cache.size());
    }

    public void testPartialExpiry() throws Exception {
        //Add an apple, it will expire at 2000
        cache.put(1, "apple");
        Clock.setTime(1500);
        //Add an orange, it will expire at 2500
        cache.put(2, "orange");

        assertEquals("apple", cache.get(1));
        assertEquals("orange", cache.get(2));
        assertEquals(2, cache.size());

        //Set time to 2300 and check that only the apple has disappeared
        Clock.setTime(2300);

        assertNull(cache.get(1));
        assertEquals("orange", cache.get(2));
        assertEquals(1, cache.size());
    }

    public void testPutReturnValue() {
        cache.put(1, "apple");
        assertNotNull(cache.put(1, "banana"));
        Clock.setTime(3000);
        assertNull(cache.put(1, "mango"));
    }

    public void testRemove() throws Exception {
        assertNull(cache.remove(new Integer(1)));

        cache.put(new Integer(1), "apple");

        assertEquals("apple", cache.remove(new Integer(1)));

        assertNull(cache.get(new Integer(1)));
        assertEquals(0, cache.size());

    }

    public void testContainsKeyAndContainsValue() {
        assertFalse(cache.containsKey(1));
        assertFalse(cache.containsValue("apple"));
        assertFalse(cache.containsKey(2));
        assertFalse(cache.containsValue("orange"));

        cache.put(1, "apple");
        assertTrue(cache.containsKey(1));
        assertTrue(cache.containsValue("apple"));
        assertFalse(cache.containsKey(2));
        assertFalse(cache.containsValue("orange"));

        Clock.setTime(3000);
        assertFalse(cache.containsKey(1));
        assertFalse(cache.containsValue("apple"));
        assertFalse(cache.containsKey(2));
        assertFalse(cache.containsValue("orange"));


    }

    public void testExpiringElementAfterTTLReducing() {
        cache.setTimeToLive(2000);
        Clock.setTime(1000);
        cache.put(1, "a");
        cache.put(2, "b");
        Clock.setTime(2100);
        cache.put(3, "c");
        cache.put(4, "d");
        assertEquals(cache.size(), 4);

        assertTrue(cache.containsKey(1)); // still alive element
        assertTrue(cache.containsKey(2)); // still alive element
        assertTrue(cache.containsValue("a")); // still alive element
        assertTrue(cache.containsValue("b")); // still alive element
        assertTrue(cache.containsKey(3)); // still alive element
        assertTrue(cache.containsKey(4)); // still alive element
        assertTrue(cache.containsValue("c")); // still alive element
        assertTrue(cache.containsValue("d")); // still alive element

        cache.setTimeToLive(1000);
        assertFalse(cache.containsKey(1)); // expired element
        assertFalse(cache.containsKey(2)); // expired element
        assertFalse(cache.containsValue("a")); // expired element
        assertFalse(cache.containsValue("b")); // expired element
        assertTrue(cache.containsKey(3)); // still alive element
        assertTrue(cache.containsKey(4)); // still alive element
        assertTrue(cache.containsValue("c")); // still alive element
        assertTrue(cache.containsValue("d")); // still alive element
        assertEquals(cache.size(), 2);
    }

    public void testPuttingOrderControl() {
        CacheMapImpl<Integer, String> _cache = new CacheMapImpl<>();
        _cache.setTimeToLive(1000);
        _cache.put(1, "a");
        _cache.put(2, "b");
        _cache.put(3, "c");
        _cache.put(4, "d");
        _cache.put(2, "e");
        Iterator<Integer> iterator = _cache.map.keySet().iterator();
        assertEquals(iterator.next(), Integer.valueOf(1));
        assertEquals(iterator.next(), Integer.valueOf(3));
        assertEquals(iterator.next(), Integer.valueOf(4));
        assertEquals(iterator.next(), Integer.valueOf(2));
    }

}
