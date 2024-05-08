package org.example.model;

import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.Map;

@Setter
public class LruCache<K, V> extends LinkedHashMap<K, V> {
    private int maxEntries;

    public LruCache(int maxEntries) {
        super(maxEntries + 1, 1f, true);
        this.maxEntries = maxEntries;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return maxEntries < this.size();
    }

}
