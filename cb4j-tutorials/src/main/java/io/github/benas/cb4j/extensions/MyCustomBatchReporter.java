package io.github.benas.cb4j.extensions;

import io.github.benas.cb4j.core.impl.DefaultBatchReporterImpl;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * A custom batch reporter that generates a graphic chart report
 * @author benas (md.benhassine@gmail.com)
 */
public class MyCustomBatchReporter extends DefaultBatchReporterImpl {

    /**
     * The file in which render the chart
     */
    private String reportFile;

    public MyCustomBatchReporter(String reportFile) {
        this.reportFile = reportFile;
    }

    @Override
    public void generateReport() {
        PieDataset dataset = createDataset();
        JFreeChart chart = createChart(dataset);
        try {
            ChartUtilities.saveChartAsPNG(new File(reportFile), chart, 600, 400);
        } catch (IOException e) {
            System.err.println("[ " + this.getClass().getSimpleName() + "] : an error occurred when generating chart report, using default report. ");
            super.generateReport();
        }
    }

    /**
     * Create a dataset with record metrics
     * @return populated dataset
     */
    private PieDataset createDataset() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Total ignored records", batchReport.getIgnoredRecordsNumber());
        dataset.setValue("Total rejected records", batchReport.getRejectedRecordsNumber());
        dataset.setValue("Total processed records", batchReport.getTotalInputRecordsNumber() - (batchReport.getRejectedRecordsNumber() + batchReport.getIgnoredRecordsNumber()));
        return dataset;
    }

    /**
     * Create a chart from a dataset
     * @param dataset the input data set
     * @return populated chart
     */
    private JFreeChart createChart(PieDataset dataset) {

        JFreeChart chart = ChartFactory.createPieChart(
                "My custom Batch Report", dataset, true, true, false
        );

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        plot.setCircular(false);
        return chart;

    }

}
