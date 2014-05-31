package org.easybatch.bench;

import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * JMH Class used to benchmark Easy Batch CSV processing performance overhead.
 * Only record reading, parsing and mapping to domain objects are benchmarked.
 *
 * @author Mahmoud Ben Hassine (md.benhassine@gmail.com)
 */
@State(Scope.Benchmark)
public class CsvBenchmark extends Benchmark {

    @Override
    @Setup
    public void setup() throws Exception {
        super.setup();
        customersFile = System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") + "customers.csv";
        BenchmarkUtil.generateCsvCustomers(customersFile, customersCount);
        easyBatchEngine = BenchmarkUtil.buildCsvEasyBatchEngine(customersFile);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(".*" + CsvBenchmark.class.getSimpleName() + ".*")
                .forks(1)
                .warmupIterations(5)
                .measurementIterations(3)
                .build();

        new Runner(opt).run();

    }

}
