package org.example;
public class LinkedList {

    private Node head;
    private int size;
    private long comparisons;
    private long accesses;

    private static class Node {
        int value;
        Node next;
        Node(int value) {
            this.value = value;
            this.next = null;
        }
    }

    public LinkedList() {
        head = null;
        size = 0;
    }

    public void add(int value) {
        Node newNode = new Node(value);

        if (head == null) {
            head = newNode;
        } else {
            Node current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
        size++;
    }

    public void add(int index, int value) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Invalid index");
        }

        Node newNode = new Node(value);

        if (index == 0) {
            newNode.next = head;
            head = newNode;
        } else {
            Node current = head;

            for (int i = 0; i < index - 1; i++) {
                current = current.next;
                accesses++;
            }
            newNode.next = current.next;
            current.next = newNode;
        }
        size++;
    }

    public int remove(int index) {
        checkIndex(index);
        int removedValue;
        if (index == 0) {
            removedValue = head.value;
            head = head.next;
        } else {
            Node current = head;

            for (int i = 0; i < index - 1; i++) {
                current = current.next;
                accesses++;
            }
            removedValue = current.next.value;
            current.next = current.next.next;
        }
        size--;
        return removedValue;
    }

    public int get(int index) {
        checkIndex(index);
        Node current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.value;
    }

    public boolean contains(int value) {
        Node current = head;
        while (current != null) {
            comparisons++;

            if (current.value == value) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    public void resetComparisons() {
        comparisons = 0;
    }

    public long getComparisons() {
        return comparisons;
    }

    public int size() {
        return size;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Invalid index");
        }
    }

    public void resetAccesses() {
        accesses = 0;
    }

    public long getAccesses() {
        return accesses;
    }
}