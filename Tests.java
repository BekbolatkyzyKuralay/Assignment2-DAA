package org.example;
public class Tests {

    public static void main(String[] args) {
        testDynamicArray();
        testLinkedList();
        testMinHeap();

        System.out.println("\nAll tests finished.");
    }

    private static void testDynamicArray() {
        System.out.println("=== Dynamic Array Test ===");
        DynamicArray array = new DynamicArray();

        array.add(10);
        array.add(20);
        array.add(30);

        System.out.println("Size: " + array.size());
        System.out.println("Element at index 1: " + array.get(1));
        System.out.println("Contains 20: " + array.contains(20));
        System.out.println("Contains 50: " + array.contains(50));

        array.add(1, 15);
        System.out.println("After adding 15 at index 1: " + array.get(1));

        int removed = array.remove(1);
        System.out.println("Removed: " + removed);
        System.out.println("Size after removal: " + array.size());
        System.out.println();
    }

    private static void testLinkedList() {
        System.out.println("=== Linked List Test ===");
        LinkedList list = new LinkedList();

        list.add(10);
        list.add(20);
        list.add(30);

        System.out.println("Size: " + list.size());
        System.out.println("Element at index 1: " + list.get(1));
        System.out.println("Contains 20: " + list.contains(20));
        System.out.println("Contains 50: " + list.contains(50));

        list.add(1, 15);
        System.out.println("After adding 15 at index 1: " + list.get(1));

        int removed = list.remove(1);
        System.out.println("Removed: " + removed);
        System.out.println("Size after removal: " + list.size());
        System.out.println();
    }

    private static void testMinHeap() {
        System.out.println("=== Min Heap Test ===");
        MinHeap heap = new MinHeap();

        heap.insert(30);
        heap.insert(10);
        heap.insert(20);
        heap.insert(5);
        heap.insert(40);

        System.out.println("Size: " + heap.size());
        System.out.println("Minimum: " + heap.peekMin());
        System.out.print("Extraction order: ");

        while (!heap.isEmpty()) {
            System.out.print(heap.extractMin() + " ");
        }
        System.out.println();
    }
}