package io.github.benas.easybatch.tutorials.bench;

import io.github.benas.easybatch.core.impl.EasyBatchEngine;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Class used to benchmark Easy Batch performance.
 * Only record reading, parsing and mapping to domain objects are benchmarked.
 *
 * @author benas (md.benhassine@gmail.com)
 */
public class Benchmark {

    public static void main(String[] args) throws Exception {

        String workDir = args[0];
        String fileFormat = args[1];
        int limit = Integer.parseInt(args[2]);
        String customersFile = workDir + "customers." + fileFormat;
        Map<Integer, Long> processingTimes = new HashMap<Integer, Long>();

        for (int customersCount = 1000; customersCount < limit + 1; customersCount += (customersCount * 9)) {

            System.out.println("#####################################################");
            System.out.println("Generating " + customersCount + " random customers...");
            if ("csv".equalsIgnoreCase(fileFormat)) {
                BenchmarkUtil.generateCsvCustomers(customersFile, customersCount);
            } else {
                BenchmarkUtil.generateXmlCustomers(customersFile, customersCount);
            }

            // Build an easy batch engine
            EasyBatchEngine easyBatchEngine;
            if ("csv".equalsIgnoreCase(fileFormat)) {
                easyBatchEngine = BenchmarkUtil.buildCsvEasyBatchEngine(customersFile);
            } else {
                easyBatchEngine = BenchmarkUtil.buildXmlEasyBatchEngine(customersFile);
            }
            System.out.println("Running easy batch...");
            long startTime = System.currentTimeMillis();
            easyBatchEngine.call();
            long duration = System.currentTimeMillis() - startTime;
            System.out.println("Easy batch engine took = " + duration + "ms to process " + customersCount + " customers");
            processingTimes.put(customersCount, duration);

        }

        System.out.println("###################");
        System.out.println("Benchmark results :");
        for (Integer customersCount : processingTimes.keySet()) {
            System.out.println("Easy batch engine took = " + processingTimes.get(customersCount) + "ms to process " +
                    customersCount + " customers");
        }

        //remove generated file
        File file = new File(customersFile);
        file.delete();

    }

}
