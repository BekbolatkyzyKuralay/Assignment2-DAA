# Assignment 2 — Algorithmic Analysis, Correctness and Performance Trade-offs

## 1. Overview

The purpose of this assignment is to implement and analyze three data structures: Dynamic Array, Linked List, and Min-Heap.

The main goal is not only to implement these structures, but also to compare their theoretical complexity with experimental performance. The project uses Java and benchmarks different operations with different input sizes.

### Implemented Data Structures

- **Dynamic Array**
    - add(x)
    - add(index, x)
    - remove(index)
    - get(index)
    - contains(x)

- **Linked List**
    - add(x)
    - add(index, x)
    - remove(index)
    - get(index)
    - contains(x)

- **Min-Heap**
    - insert(x)
    - peekMin()
    - extractMin()

The project also contains tests, benchmarking experiments, CSV results, and plots.




## 2. Complexity Analysis

### Dynamic Array

| Operation | Best Case | Average Case | Worst Case | Auxiliary Space |
|---|---|---|---|---|
| add(x) | Ω(1) | Θ(1) amortized | O(n) | O(n) when resized |
| add(index, x) | Ω(1) | Θ(n) | O(n) | O(n) when resized |
| remove(index) | Ω(1) | Θ(n) | O(n) | O(1) |
| get(index) | Θ(1) | Θ(1) | Θ(1) | O(1) |
| contains(x) | Ω(1) | Θ(n) | O(n) | O(1) |

A Dynamic Array provides fast random access because an element can be accessed directly using its index. Therefore, `get(index)` has constant time complexity.

Insertion or removal can be more expensive because elements may need to be shifted. When the internal array becomes full, a larger array is created and the existing elements are copied to it.


### Linked List

| Operation | Best Case | Average Case | Worst Case | Auxiliary Space |
|---|---|---|---|---|
| add(x) | Ω(1) | Θ(n) | O(n) | O(1) |
| add(index, x) | Ω(1) | Θ(n) | O(n) | O(1) |
| remove(index) | Ω(1) | Θ(n) | O(n) | O(1) |
| get(index) | Ω(1) | Θ(n) | O(n) | O(1) |
| contains(x) | Ω(1) | Θ(n) | O(n) | O(1) |

In this implementation, the Linked List stores a reference only to the head. Because of this, adding an element to the end requires traversal through the list.

Accessing an element by index also requires moving from the head node until the required position is reached. However, insertion and removal at the beginning can be performed in constant time.


### Min-Heap

| Operation | Best Case | Average Case | Worst Case | Auxiliary Space |
|---|---|---|---|---|
| insert(x) | Ω(1) | O(log n) | O(log n) | O(1) |
| peekMin() | Θ(1) | Θ(1) | Θ(1) | O(1) |
| extractMin() | Ω(1) | O(log n) | O(log n) | O(1) |

The Min-Heap keeps the smallest element at the root. Therefore, `peekMin()` can return the minimum value directly.

During insertion, a new element may move upward until the heap property is restored. During `extractMin()`, an element may move downward through the heap. The height of a binary heap is logarithmic, so these operations have O(log n) worst-case time.




## 3. Correctness

To show that the implementations work correctly, I selected two operations that contain loops:

1. Dynamic Array insertion at a specific index
2. Min-Heap insertion


### 3.1 Dynamic Array — add(index, value)

When a new value is inserted at a specific index, the elements from that index to the end must be shifted one position to the right.

#### Loop Invariant

Before each iteration of the loop, the elements already processed from the right side have been moved one position to the right correctly, and their original values are preserved.

#### Initialization

The loop starts from `i = size`. At this moment, no elements have been shifted yet. The position at `data[size]` is available for the shifting process, so the invariant is true before the first iteration.

#### Maintenance

During each iteration, the algorithm performs:

`data[i] = data[i - 1]`

This moves one element one position to the right. Then `i` decreases, and the next element is processed. Therefore, all elements that have already been processed remain in their correct shifted positions.

#### Termination

The loop stops when `i` is no longer greater than `index`. At this point, all elements from the original insertion index to the end have been shifted one position to the right.

The new value can then be stored at:

`data[index] = value`

Therefore, the insertion is correct because no existing element is lost and the new element is placed at the requested index.


### 3.2 Min-Heap — insert(value)

When a new element is inserted into the Min-Heap, it is first placed at the end of the heap. It is then moved upward while it is smaller than its parent.

#### Loop Invariant

Before each iteration of the loop, the heap property is valid everywhere except possibly between the newly inserted element and its parent.

#### Initialization

Before the loop begins, the new value is added at the last position of the heap. The old elements already satisfy the Min-Heap property because the heap was valid before the insertion.

The only possible violation is between the new element and its parent. Therefore, the invariant is true.

#### Maintenance

During each iteration, the new element is compared with its parent.

If the new element is smaller, the two elements are swapped. After the swap, the heap property is restored at the previous position. The possible violation moves upward to the new position of the inserted element.

Therefore, the invariant remains true after every iteration.

#### Termination

The loop terminates when either the inserted element reaches the root or it is greater than or equal to its parent.

At this point, there is no remaining parent-child violation. Since the rest of the heap was already valid, the complete Min-Heap property is restored.

Therefore, the `insert(value)` operation correctly maintains the Min-Heap property.

## 4. Experimental Setup

The experiments were created to compare the theoretical complexity of the data structures with their actual performance.

### Input Sizes

The following input sizes were used:

- n = 100
- n = 1,000
- n = 10,000
- n = 100,000

Here, `n` represents the number of elements initially stored in the data structure.

The value `m` represents the number of operations performed during a workload.

### Benchmarking Method

Each experiment was repeated 5 times and the average execution time was calculated.

Execution time was measured using:

`System.nanoTime()`

The input data was generated before the timed part of the experiment. Printing and input generation were not included in the measured execution time.

A fixed random seed was used:

`Random(42)`

This makes the experiments reproducible because the same sequence of random values can be generated again.


### Workload 1 — Random Access

Structures:

- Dynamic Array
- Linked List

For each value of `n`, 10,000 random indices were generated. The `get(index)` operation was performed for every index.

Measured values:

- average execution time;
- number of accesses.

This workload compares direct array access with Linked List traversal.


### Workload 2 — Search

Structures:

- Dynamic Array
- Linked List

For each value of `n`, 1,000 search values were generated and the `contains(value)` operation was performed.

Measured values:

- average execution time;
- number of element comparisons.

This experiment shows how search performance changes when the input size increases.


### Workload 3 — Insertion and Removal

Structures:

- Dynamic Array
- Linked List

For every input size, 1,000 insertion and removal operations were tested.

Two positions were used:

- beginning: `index = 0`;
- middle: `index = n / 2`.

Measured values:

- average execution time;
- element movements for Dynamic Array;
- node accesses for Linked List.

This workload demonstrates how the physical organization of each data structure affects insertion and removal performance.


### Workload 4 — Priority Processing

Structure:

- Min-Heap

For each input size, `n` random integers were inserted into an empty Min-Heap. After insertion, all elements were removed using `extractMin()`.

Measured values:

- total insertion time;
- total extraction time;
- number of comparisons.

The extracted elements were also checked to make sure that they were returned in non-decreasing order.




## 5. Results

The benchmark results were saved automatically during the experiments.

The complete numerical results can be found here:

`results/tables/benchmark_results.csv`

The CSV file contains:

- workload;
- data structure;
- operation;
- input size (`n`);
- average execution time in nanoseconds;
- measured metric;
- metric value.

The experiments were performed for all required input sizes: 100, 1,000, 10,000, and 100,000.

### Workload 1 — Random Access Results

| n | Dynamic Array Time (ns) | Linked List Time (ns) | Accesses | Theoretical Complexity |
|---:|---:|---:|---:|---|
| 100 | 374,900 | 1,827,940 | 10,000 | Array: Θ(1), List: O(n) |
| 1,000 | 96,200 | 9,544,360 | 10,000 | Array: Θ(1), List: O(n) |
| 10,000 | 13,180 | 107,377,840 | 10,000 | Array: Θ(1), List: O(n) |
| 100,000 | 120,920 | 1,192,261,620 | 10,000 | Array: Θ(1), List: O(n) |

The Dynamic Array remained fast because it can access an element directly by index. The Linked List became much slower as `n` increased because it must traverse nodes to reach an index.


### Workload 2 — Search Results

| n | Dynamic Array Time (ns) | Linked List Time (ns) | Comparisons | Theoretical Complexity |
|---:|---:|---:|---:|---|
| 100 | 689,740 | 604,260 | 99,932 | O(n) |
| 1,000 | 1,554,500 | 3,342,320 | 998,014 | O(n) |
| 10,000 | 6,528,140 | 26,908,040 | 9,485,303 | O(n) |
| 100,000 | 43,493,880 | 178,303,720 | 62,468,382 | O(n) |

Both structures use linear search, so the number of comparisons increases as the input size grows. Even with similar comparison counts, their actual execution times are different because their memory organization is different.


### Workload 3 — Insertion and Removal Results

#### Beginning — index = 0

| n | Structure | Insert Time (ns) | Insert Metric | Remove Time (ns) | Remove Metric |
|---:|---|---:|---:|---:|---:|
| 100 | Dynamic Array | 3,563,280 | 599,500 movements | 4,647,620 | 599,500 movements |
| 100 | Linked List | 87,800 | 0 accesses | 66,860 | 0 accesses |
| 1,000 | Dynamic Array | 247,600 | 1,499,500 movements | 216,800 | 1,499,500 movements |
| 1,000 | Linked List | 55,180 | 0 accesses | 32,080 | 0 accesses |
| 10,000 | Dynamic Array | 1,956,520 | 10,499,500 movements | 2,158,560 | 10,499,500 movements |
| 10,000 | Linked List | 23,780 | 0 accesses | 14,460 | 0 accesses |
| 100,000 | Dynamic Array | 21,199,560 | 100,499,500 movements | 17,408,180 | 100,499,500 movements |
| 100,000 | Linked List | 6,420 | 0 accesses | 3,180 | 0 accesses |

At the beginning of the Linked List, insertion and removal do not require traversal. In the Dynamic Array, many elements must be shifted.

#### Middle — index = n / 2

| n | Structure | Insert Time (ns) | Insert Metric | Remove Time (ns) | Remove Metric |
|---:|---|---:|---:|---:|---:|
| 100 | Dynamic Array | 185,620 | 549,500 movements | 142,440 | 549,500 movements |
| 100 | Linked List | 379,620 | 49,000 accesses | 358,880 | 49,000 accesses |
| 1,000 | Dynamic Array | 150,000 | 999,500 movements | 124,860 | 999,500 movements |
| 1,000 | Linked List | 878,280 | 499,000 accesses | 853,400 | 499,000 accesses |
| 10,000 | Dynamic Array | 1,265,700 | 5,499,500 movements | 805,260 | 5,499,500 movements |
| 10,000 | Linked List | 8,979,680 | 4,999,000 accesses | 10,395,120 | 4,999,000 accesses |
| 100,000 | Dynamic Array | 10,059,660 | 50,499,500 movements | 11,946,060 | 50,499,500 movements |
| 100,000 | Linked List | 113,784,360 | 49,999,000 accesses | 117,182,920 | 49,999,000 accesses |

For middle operations, the Dynamic Array must shift elements, while the Linked List must traverse nodes to reach the required position. Therefore, both operations become more expensive as `n` increases.


### Workload 4 — Priority Processing Results

| n | Insert Time (ns) | Insert Comparisons | Extract Time (ns) | Extract Comparisons | Complexity |
|---:|---:|---:|---:|---:|---|
| 100 | 33,480 | 222 | 85,300 | 852 | O(log n) per operation |
| 1,000 | 104,380 | 2,203 | 245,440 | 14,966 | O(log n) per operation |
| 10,000 | 1,079,320 | 22,602 | 2,542,280 | 216,531 | O(log n) per operation |
| 100,000 | 4,261,900 | 228,950 | 23,944,460 | 2,831,453 | O(log n) per operation |

The Min-Heap results show that insertion and extraction become more expensive as the heap grows. Extraction requires more comparisons because the root is removed and the heap property must be restored.

All extracted values were verified to be in non-decreasing order.

### Plot 1 – Execution Time vs. n

<img src="./results/plots/execution_time_vs_n.png" width="600" alt="Execution Time vs n">

### Plot 2 – Operations / Comparisons / Accesses vs. n

<img src="./results/plots/operations_vs_n.png" width="600" alt="Operations vs n">




## 6. Discussion

The experimental results generally agree with the theoretical complexity, although the measured execution times do not always increase perfectly with `n`.

### Effect of Increasing n

In the Random Access workload, the Dynamic Array remained very fast even when the input size increased. This agrees with the theoretical Θ(1) complexity of `get(index)`.

The Linked List showed a large increase in execution time. For example, its random access time increased from 1,827,940 ns for `n = 100` to 1,192,261,620 ns for `n = 100,000`. This happens because the list must traverse nodes to reach a requested index.

In the Search workload, both structures performed linear search. The number of comparisons increased significantly with the input size. This agrees with the O(n) worst-case complexity of `contains(value)`.

### Insertion and Removal

The results show an important difference between the physical organization of the Dynamic Array and Linked List.

For insertion and removal at the beginning, the Linked List performed well because the head can be changed directly. No node traversal was required in this implementation.

The Dynamic Array had to shift many elements. At `n = 100,000`, the beginning operations recorded 100,499,500 element movements.

For middle operations, the Linked List had to traverse many nodes before reaching the required position. The Dynamic Array also had a large number of movements, but array memory access is direct and contiguous.

### Min-Heap Performance

The Min-Heap results also generally agree with the theoretical analysis.

`peekMin()` is Θ(1) because the minimum value is stored at the root.

Insertion and extraction may move through the height of the heap, giving O(log n) complexity per operation. As `n` increased, the measured number of comparisons also increased.

The experiment also verified that `extractMin()` returned values in non-decreasing order, which supports the correctness of the implementation.

### Theory vs Experimental Results

The measured execution times do not always grow smoothly. For example, some Dynamic Array measurements for smaller inputs were slower than measurements for larger inputs.

This does not mean that the theoretical complexity is incorrect. Big-O, Omega, and Theta describe how the amount of work grows with input size, rather than predicting an exact execution time.

Practical Java performance can also be affected by:

- JVM warm-up;
- JIT compilation;
- CPU caching;
- garbage collection;
- system activity;
- measurement overhead.

Therefore, two algorithms with the same Big-O complexity can still have different measured running times.

Constant factors and implementation details also matter. For example, Dynamic Array elements are stored in contiguous memory, while a Linked List follows references between separate nodes.




## 7. Design Recommendations

The experiments show that the best data structure depends on the workload.

A **Dynamic Array** is suitable when the program needs frequent random access by index. Its `get(index)` operation is Θ(1), which makes it useful when fast indexed access is important.

A **Linked List** can be useful when there are frequent insertions or removals at the beginning of the structure. These operations can be performed without shifting all other elements. However, accessing an arbitrary index is slower because the nodes must be traversed.

A **Min-Heap** is suitable for priority-based processing. The minimum element can be accessed in Θ(1), while insertion and extraction take O(log n) time in the worst case.

Therefore, data structure selection should be based on the operations that are performed most frequently rather than choosing one structure for every situation.




## 8. Conclusion

In this assignment, I implemented and analyzed a Dynamic Array, Linked List, and Min-Heap in Java.

I tested the structures using four workloads: Random Access, Search, Insertion and Removal, and Priority Processing. The experiments used different input sizes and measured execution time together with comparisons, accesses, and element movements.

The results generally supported the theoretical complexity analysis. Dynamic Array was effective for random access, Linked List was useful for operations at the beginning, and Min-Heap provided efficient priority processing.

This assignment also showed that theoretical complexity and real execution time are related but are not exactly the same. JVM behavior, memory organization, caching, and other practical factors can affect measured performance.

Overall, the experiments helped me understand how the choice of a data structure depends on the workload and why both theoretical analysis and practical testing are important.
