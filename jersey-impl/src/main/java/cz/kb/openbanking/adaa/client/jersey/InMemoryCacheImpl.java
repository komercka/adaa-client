package cz.kb.openbanking.adaa.client.jersey;

import org.apache.commons.collections4.map.LRUMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Map;

/**
 * Simple singleton in-memory thread-safe cache implementation that based on the Apache Commons {@link LRUMap}.
 *
 * @param <K> type of the cache item's key
 * @param <V> type of the cache item's value
 * @author <a href="mailto:aleh_kuchynski@kb.cz">Aleh Kuchynski</a>
 * @see LRUMap
 * @since 1.0
 */
class InMemoryCacheImpl<K, V> {
    private static final Logger log = LoggerFactory.getLogger(InMemoryCacheImpl.class);
    /**
     * Cache's maximum size (after limit exceeding - the oldest cache item will be automatically removed).
     */
    private static final Integer CACHE_MAX_SIZE = 100;
    /**
     * Map implementation of a cache.
     */
    private final Map<K, V> cacheMap;

    @SuppressWarnings("rawtypes")
    private static InMemoryCacheImpl instance = null;

    /**
     * New instance.
     */
    private InMemoryCacheImpl() {
        cacheMap = Collections.synchronizedMap(new LRUMap<>(CACHE_MAX_SIZE));
    }

    /**
     * Gets cached item by correlated key.
     *
     * @param key key of the cached value
     * @return cached item
     */
    @Nullable
    public V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException("key must not be null");
        }

        V cacheItem = cacheMap.get(key);
        if (cacheItem == null) {
            log.debug("No cache item was found for key '{}'", key.toString());
            return null;
        }

        return cacheItem;
    }

    /**
     * Puts new item to the cache.
     *
     * @param key   key of the value
     * @param value value for putting to the cache
     */
    public void put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("key must not be null");
        }
        if (value == null) {
            throw new IllegalArgumentException("value must not be null");
        }

        cacheMap.put(key, value);
    }

    /**
     * Removes cached item from the the cache.
     *
     * @param key key of the cached value
     */
    public void remove(K key) {
        if (key == null) {
            throw new IllegalArgumentException("key must not be null");
        }

        cacheMap.remove(key);
    }

    /**
     * Gets current size of the cache.
     *
     * @return current size of the cache
     */
    public int size() {
        return cacheMap.size();
    }

    /**
     * Gets singleton instance of this class.
     *
     * @return singleton instance of the {@link InMemoryCacheImpl}
     */
    @SuppressWarnings("unchecked")
    public static <K, V> InMemoryCacheImpl<K, V> getInstance() {
        if (instance == null) {
            instance = new InMemoryCacheImpl<K, V>();
        }

        return (InMemoryCacheImpl<K, V>) instance;
    }
}
