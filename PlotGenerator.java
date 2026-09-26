package org.example;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class PlotGenerator {

    private static final String CSV_FILE =
            "results/tables/benchmark_results.csv";
    private static final String PLOTS_FOLDER =
            "results/plots/";

    public static void main(String[] args) {

        new File(PLOTS_FOLDER).mkdirs();

        createTimePlot();
        createMetricPlot();

        System.out.println("Plots created successfully.");
    }

    private static void createTimePlot() {

        XYSeries dynamicArray = new XYSeries("Dynamic Array");
        XYSeries linkedList = new XYSeries("Linked List");
        XYSeries minHeap = new XYSeries("Min Heap");

        try (BufferedReader reader =
                     new BufferedReader(new FileReader(CSV_FILE))) {

            String line;
            reader.readLine();

            while ((line = reader.readLine()) != null) {

                String[] parts = line.split(",");
                String workload = parts[0];
                String structure = parts[1];
                int n = Integer.parseInt(parts[3]);
                long time = Long.parseLong(parts[4]);

                if (workload.equals("Workload1")) {

                    if (structure.equals("DynamicArray")) {
                        dynamicArray.add(n, time);
                    }

                    if (structure.equals("LinkedList")) {
                        linkedList.add(n, time);
                    }
                }
                if (workload.equals("Workload4")
                        && parts[2].equals("Insert")) {

                    minHeap.add(n, time);
                }
            }

        } catch (IOException e) {
            System.out.println(
                    "Error reading CSV: " + e.getMessage()
            );
            return;
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(dynamicArray);
        dataset.addSeries(linkedList);
        dataset.addSeries(minHeap);

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Execution Time vs n",
                "Input Size (n)",
                "Average Time (ns)",
                dataset
        );

        try {
            ChartUtils.saveChartAsPNG(
                    new File(PLOTS_FOLDER + "execution_time_vs_n.png"),
                    chart,
                    1000,
                    600
            );

        } catch (IOException e) {
            System.out.println(
                    "Error saving time plot: " + e.getMessage()
            );
        }
    }

    private static void createMetricPlot() {

        XYSeries arrayComparisons =
                new XYSeries("Dynamic Array Comparisons");

        XYSeries listComparisons =
                new XYSeries("Linked List Comparisons");

        XYSeries heapComparisons =
                new XYSeries("Min Heap Comparisons");

        try (BufferedReader reader =
                     new BufferedReader(new FileReader(CSV_FILE))) {

            String line;
            reader.readLine();

            while ((line = reader.readLine()) != null) {

                String[] parts = line.split(",");
                String workload = parts[0];
                String structure = parts[1];

                int n = Integer.parseInt(parts[3]);
                long metricValue = Long.parseLong(parts[6]);

                if (workload.equals("Workload2")) {
                    if (structure.equals("DynamicArray")) {
                        arrayComparisons.add(n, metricValue);
                    }
                    if (structure.equals("LinkedList")) {
                        listComparisons.add(n, metricValue);
                    }
                }

                if (workload.equals("Workload4")
                        && parts[2].equals("ExtractMin")) {

                    heapComparisons.add(n, metricValue);
                }
            }

        } catch (IOException e) {
            System.out.println(
                    "Error reading CSV: " + e.getMessage()
            );
            return;
        }

        XYSeriesCollection dataset = new XYSeriesCollection();

        dataset.addSeries(arrayComparisons);
        dataset.addSeries(listComparisons);
        dataset.addSeries(heapComparisons);

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Operations and Comparisons vs n",
                "Input Size (n)",
                "Number of Operations",
                dataset
        );

        try {
            ChartUtils.saveChartAsPNG(
                    new File(PLOTS_FOLDER + "operations_vs_n.png"),
                    chart,
                    1000,
                    600
            );

        } catch (IOException e) {
            System.out.println(
                    "Error saving operations plot: " + e.getMessage()
            );
        }
    }
}