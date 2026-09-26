package org.example;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class Benchmark {

    private static final int[] SIZES = {
            100, 1000, 10000, 100000
    };
    private static final int UPDATE_OPERATIONS = 1000;
    private static final int REPEATS = 5;
    private static final int RANDOM_ACCESSES = 10000;
    private static final int SEARCHES = 1000;
    private static final String CSV_FILE =
            "results/tables/benchmark_results.csv";

    public static void main(String[] args) {
        createCsvFile();

        System.out.println("=== Workload 1: Random Access ===");
        for (int n : SIZES) {
            testRandomAccess(n);
        }

        System.out.println("\n=== Workload 2: Search ===");
        for (int n : SIZES) {
            testSearch(n);
        }

        System.out.println("\n=== Workload 3: Insertion and Removal ===");

        for (int n : SIZES) {
            testInsertionRemoval(n);
        }

        System.out.println("\n=== Workload 4: Priority Processing ===");

        for (int n : SIZES) {
            testMinHeap(n);
        }
    }

    private static void testRandomAccess(int n) {
        long arrayTotalTime = 0;
        long listTotalTime = 0;

        for (int repeat = 0; repeat < REPEATS; repeat++) {
            Random random = new Random(42);
            DynamicArray array = new DynamicArray();
            LinkedList list = new LinkedList();

            for (int i = 0; i < n; i++) {
                int value = random.nextInt(100000);
                array.add(value);
                list.add(value);
            }

            int[] indices = new int[RANDOM_ACCESSES];

            for (int i = 0; i < RANDOM_ACCESSES; i++) {
                indices[i] = random.nextInt(n);
            }
            long start = System.nanoTime();
            for (int index : indices) {
                array.get(index);
            }
            long end = System.nanoTime();
            arrayTotalTime += end - start;
            start = System.nanoTime();

            for (int index : indices) {
                list.get(index);
            }
            end = System.nanoTime();
            listTotalTime += end - start;
        }

        long arrayAverage = arrayTotalTime / REPEATS;
        long listAverage = listTotalTime / REPEATS;
        System.out.println("\nn = " + n);

        System.out.println(
                "Dynamic Array average time: "
                        + arrayAverage + " ns"
        );

        System.out.println(
                "Linked List average time: "
                        + listAverage + " ns"
        );

        System.out.println(
                "Random accesses: " + RANDOM_ACCESSES
        );
        saveResult(
                "Workload1",
                "DynamicArray",
                "RandomAccess",
                n,
                arrayAverage,
                "Accesses",
                RANDOM_ACCESSES
        );

        saveResult(
                "Workload1",
                "LinkedList",
                "RandomAccess",
                n,
                listAverage,
                "Accesses",
                RANDOM_ACCESSES
        );
    }

    private static void testSearch(int n) {

        long arrayTotalTime = 0;
        long listTotalTime = 0;
        long arrayTotalComparisons = 0;
        long listTotalComparisons = 0;

        for (int repeat = 0; repeat < REPEATS; repeat++) {
            Random random = new Random(42);
            DynamicArray array = new DynamicArray();
            LinkedList list = new LinkedList();
            for (int i = 0; i < n; i++) {
                int value = random.nextInt(100000);

                array.add(value);
                list.add(value);
            }

            int[] searchValues = new int[SEARCHES];

            for (int i = 0; i < SEARCHES; i++) {
                searchValues[i] = random.nextInt(100000);
            }

            array.resetComparisons();
            long start = System.nanoTime();
            for (int value : searchValues) {
                array.contains(value);
            }
            long end = System.nanoTime();

            arrayTotalTime += end - start;
            arrayTotalComparisons += array.getComparisons();
            list.resetComparisons();
            start = System.nanoTime();

            for (int value : searchValues) {
                list.contains(value);
            }
            end = System.nanoTime();
            listTotalTime += end - start;
            listTotalComparisons += list.getComparisons();
        }

        long arrayAverageTime = arrayTotalTime / REPEATS;
        long listAverageTime = listTotalTime / REPEATS;

        long arrayAverageComparisons =
                arrayTotalComparisons / REPEATS;

        long listAverageComparisons =
                listTotalComparisons / REPEATS;

        System.out.println("\nn = " + n);
        System.out.println(
                "Dynamic Array average time: "
                        + arrayAverageTime + " ns"
        );

        System.out.println(
                "Dynamic Array comparisons: "
                        + arrayAverageComparisons
        );

        System.out.println(
                "Linked List average time: "
                        + listAverageTime + " ns"
        );

        System.out.println(
                "Linked List comparisons: "
                        + listAverageComparisons
        );
        saveResult(
                "Workload2",
                "DynamicArray",
                "Search",
                n,
                arrayAverageTime,
                "Comparisons",
                arrayAverageComparisons
        );

        saveResult(
                "Workload2",
                "LinkedList",
                "Search",
                n,
                listAverageTime,
                "Comparisons",
                listAverageComparisons
        );
    }

    private static void testInsertionRemoval(int n) {
        System.out.println("\nn = " + n);
        testPosition(n, 0, "Beginning");
        testPosition(n, n / 2, "Middle");
    }

    private static void testPosition(int n, int index, String position) {

        long arrayInsertTime = 0;
        long arrayRemoveTime = 0;
        long listInsertTime = 0;
        long listRemoveTime = 0;

        long arrayInsertMovements = 0;
        long arrayRemoveMovements = 0;
        long listInsertAccesses = 0;
        long listRemoveAccesses = 0;

        for (int repeat = 0; repeat < REPEATS; repeat++) {
            DynamicArray array = new DynamicArray();

            for (int i = 0; i < n; i++) {
                array.add(i);
            }

            array.resetMovements();
            long start = System.nanoTime();

            for (int i = 0; i < UPDATE_OPERATIONS; i++) {
                array.add(index, i);
            }

            long end = System.nanoTime();

            arrayInsertTime += end - start;
            arrayInsertMovements += array.getMovements();
            array = new DynamicArray();

            for (int i = 0; i < n + UPDATE_OPERATIONS; i++) {
                array.add(i);
            }

            array.resetMovements();
            start = System.nanoTime();

            for (int i = 0; i < UPDATE_OPERATIONS; i++) {
                array.remove(index);
            }

            end = System.nanoTime();
            arrayRemoveTime += end - start;
            arrayRemoveMovements += array.getMovements();
            LinkedList list = new LinkedList();

            for (int i = 0; i < n; i++) {
                list.add(i);
            }

            list.resetAccesses();
            start = System.nanoTime();

            for (int i = 0; i < UPDATE_OPERATIONS; i++) {
                list.add(index, i);
            }

            end = System.nanoTime();
            listInsertTime += end - start;
            listInsertAccesses += list.getAccesses();
            list = new LinkedList();

            for (int i = 0; i < n + UPDATE_OPERATIONS; i++) {
                list.add(i);
            }

            list.resetAccesses();
            start = System.nanoTime();

            for (int i = 0; i < UPDATE_OPERATIONS; i++) {
                list.remove(index);
            }

            end = System.nanoTime();
            listRemoveTime += end - start;
            listRemoveAccesses += list.getAccesses();
        }

        System.out.println("\nPosition: " + position);

        System.out.println(
                "Dynamic Array insertion time: "
                        + arrayInsertTime / REPEATS + " ns"
        );

        System.out.println(
                "Dynamic Array insertion movements: "
                        + arrayInsertMovements / REPEATS
        );

        System.out.println(
                "Dynamic Array removal time: "
                        + arrayRemoveTime / REPEATS + " ns"
        );

        System.out.println(
                "Dynamic Array removal movements: "
                        + arrayRemoveMovements / REPEATS
        );

        System.out.println(
                "Linked List insertion time: "
                        + listInsertTime / REPEATS + " ns"
        );

        System.out.println(
                "Linked List insertion accesses: "
                        + listInsertAccesses / REPEATS
        );

        System.out.println(
                "Linked List removal time: "
                        + listRemoveTime / REPEATS + " ns"
        );

        System.out.println(
                "Linked List removal accesses: "
                        + listRemoveAccesses / REPEATS
        );
        saveResult(
                "Workload3",
                "DynamicArray",
                "Insert-" + position,
                n,
                arrayInsertTime / REPEATS,
                "Movements",
                arrayInsertMovements / REPEATS
        );

        saveResult(
                "Workload3",
                "DynamicArray",
                "Remove-" + position,
                n,
                arrayRemoveTime / REPEATS,
                "Movements",
                arrayRemoveMovements / REPEATS
        );

        saveResult(
                "Workload3",
                "LinkedList",
                "Insert-" + position,
                n,
                listInsertTime / REPEATS,
                "Accesses",
                listInsertAccesses / REPEATS
        );

        saveResult(
                "Workload3",
                "LinkedList",
                "Remove-" + position,
                n,
                listRemoveTime / REPEATS,
                "Accesses",
                listRemoveAccesses / REPEATS
        );
    }

    private static void testMinHeap(int n) {

        long totalInsertTime = 0;
        long totalExtractTime = 0;

        long totalInsertComparisons = 0;
        long totalExtractComparisons = 0;

        boolean allSorted = true;
        for (int repeat = 0; repeat < REPEATS; repeat++) {

            Random random = new Random(42);
            int[] values = new int[n];
            for (int i = 0; i < n; i++) {
                values[i] = random.nextInt(100000);
            }

            MinHeap heap = new MinHeap();
            heap.resetComparisons();
            long start = System.nanoTime();

            for (int value : values) {
                heap.insert(value);
            }

            long end = System.nanoTime();

            totalInsertTime += end - start;
            totalInsertComparisons += heap.getComparisons();
            heap.resetComparisons();

            int previous = Integer.MIN_VALUE;
            start = System.nanoTime();
            while (!heap.isEmpty()) {

                int current = heap.extractMin();
                if (current < previous) {
                    allSorted = false;
                }
                previous = current;
            }
            end = System.nanoTime();
            totalExtractTime += end - start;
            totalExtractComparisons += heap.getComparisons();
        }

        System.out.println("\nn = " + n);

        System.out.println(
                "Average insertion time: "
                        + totalInsertTime / REPEATS + " ns"
        );

        System.out.println(
                "Average insertion comparisons: "
                        + totalInsertComparisons / REPEATS
        );

        System.out.println(
                "Average extraction time: "
                        + totalExtractTime / REPEATS + " ns"
        );

        System.out.println(
                "Average extraction comparisons: "
                        + totalExtractComparisons / REPEATS
        );

        System.out.println(
                "Non-decreasing order: " + allSorted
        );
        saveResult(
                "Workload4",
                "MinHeap",
                "Insert",
                n,
                totalInsertTime / REPEATS,
                "Comparisons",
                totalInsertComparisons / REPEATS
        );

        saveResult(
                "Workload4",
                "MinHeap",
                "ExtractMin",
                n,
                totalExtractTime / REPEATS,
                "Comparisons",
                totalExtractComparisons / REPEATS
        );
    }

    private static void createCsvFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CSV_FILE))) {

            writer.println(
                    "Workload,Structure,Operation,N,AverageTimeNs,Metric,MetricValue"
            );

        } catch (IOException e) {
            System.out.println("Error creating CSV file: " + e.getMessage());
        }
    }

    private static void saveResult(
            String workload,
            String structure,
            String operation,
            int n,
            long averageTime,
            String metric,
            long metricValue) {

        try (PrintWriter writer =
                     new PrintWriter(new FileWriter(CSV_FILE, true))) {

            writer.println(
                    workload + "," +
                            structure + "," +
                            operation + "," +
                            n + "," +
                            averageTime + "," +
                            metric + "," +
                            metricValue
            );
        } catch (IOException e) {
            System.out.println(
                    "Error saving result: " + e.getMessage()
            );
        }
    }
}