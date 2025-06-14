package org.example;

import java.util.*;

public class LRUCache<K, V> {

    private final Map<K, Node> cache;
    private final int capacity;
    private final Node head;
    private final Node tail;

    private class Node {
        K key;
        V value;
        Node next;
        Node prev;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.cache = new HashMap<>();
        this.head = new Node(null, null);
        this.tail = new Node(null, null);
        head.next = tail;
        tail.prev = head;
    }

    public V get(K key) {
        Node node = cache.get(key);
        if (node == null) return null;

        moveToFront(node);
        return node.value;
    }

    public void put(K key, V value) {
        Node node = cache.get(key);

        if (node != null) {
            node.value = value;
            moveToFront(node);
        } else {
            Node newNode = new Node(key, value);
            cache.put(key, newNode);
            addToFront(newNode);
            if (cache.size() > capacity) {
                evictFromTail();
            }
        }
    }

    private void addToFront(Node node) {
        node.next = head.next;
        node.prev = head;

        head.next.prev = node;
        head.next = node;
    }

    private void moveToFront(Node node) {
        removeNode(node);
        addToFront(node);
    }

    private void removeNode(Node node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    private void evictFromTail() {
        Node lru = tail.prev;
        if (lru == head) return;
        removeNode(lru);
        cache.remove(lru.key);
    }

    public void printCache() {
        Node curr = head.next;
        while (curr != tail) {
            System.out.println(curr.key + " => " + curr.value);
            curr = curr.next;
        }
    }
}

