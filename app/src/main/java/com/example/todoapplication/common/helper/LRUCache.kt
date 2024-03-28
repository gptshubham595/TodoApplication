package com.example.todoapplication.common.helper

class LRUCache<K, V>(private val capacity: Int) {
    private val cache: LinkedHashMap<K, V> = object : LinkedHashMap<K, V>(capacity, 0.75f, true) {
        override fun removeEldestEntry(eldest: MutableMap.MutableEntry<K, V>?): Boolean {
            return size > capacity
        }
    }

    fun get(key: K): V? {
        return cache[key]
    }

    fun put(key: K, value: V) {
        cache[key] = value
    }
}

class LRUCache2<K, V>(private val capacity: Int) {
    private val cache: MutableMap<K, V> = mutableMapOf()
    private val accessOrder: MutableList<K> = mutableListOf() // index -> keys

    fun get(key: K): V? {
        return cache[key]?.also {
            updateAccessOrder(key)
        }
    }

    fun updateAccessOrder(key: K) {
        accessOrder.remove(key)
        accessOrder.add(key)
    }

    fun put(key: K, value: V) {
        if (cache.size >= capacity) {
            evict()
        }

        cache[key] = value
        updateAccessOrder(key)
    }

    fun evict() {
        val accessOrderKey = accessOrder.removeAt(0)
        cache.remove(accessOrderKey)
    }
}

class LRUCache3<K, V>(private val capacity: Int) {
    private val cache: MutableMap<K, V> = mutableMapOf()
    private val accessOrder: MutableList<K> = mutableListOf()

    fun get(key: K): V? {
        return cache[key]?.also {
            updateAccessOrder(key)
        }
    }

    private fun updateAccessOrder(key: K) {
        accessOrder.remove(key)
        accessOrder.add(key)
    }

    fun put(key: K, value: V) {
        if (cache.size >= capacity) {
            evict()
        }
        cache[key] = value
        updateAccessOrder(key)
    }

    fun evict() {
        val accessOrderKey = accessOrder.removeAt(0)
        cache.remove(accessOrderKey)
    }
}

class LRUCache4<K, V>(private val capacity: Int) : LinkedHashMap<K, V>() {
    private val cache = object : LinkedHashMap<K, V>(capacity, 0.75f, true) {
        override fun removeEldestEntry(eldest: MutableMap.MutableEntry<K, V>?): Boolean {
            return size > capacity
        }
    }

    fun insert(k: K, v: V) {
        cache[k] = v
    }

    fun getVal(k: K): V? {
        return cache[k]
    }
}

class LRUCache5<K, V>(private val capacity: Int) {
    private val cache: MutableMap<K, V> = mutableMapOf()
    private val accessOrder = mutableListOf<K>()

    fun insert(k: K, v: V) {
        if (cache.size >= capacity) {
            evict()
        }
        cache[k] = v
        updateAccessOrder(k)
    }

    fun updateAccessOrder(k: K) {
        accessOrder.remove(k)
        accessOrder.add(k)
    }

    fun evict() {
        val accessOrderKey = accessOrder.removeAt(0)
        cache.remove(accessOrderKey)
    }

    fun getVal(k: K): V? {
        return cache[k]?.also { updateAccessOrder(k) }
    }
}
