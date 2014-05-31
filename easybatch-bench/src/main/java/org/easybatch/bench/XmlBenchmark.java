package org.easybatch.bench;

import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * JMH Class used to benchmark Easy Batch XML processing performance overhead.
 * Only record reading, parsing and mapping to domain objects are benchmarked.
 *
 * @author Mahmoud Ben Hassine (md.benhassine@gmail.com)
 */
@State(Scope.Benchmark)
public class XmlBenchmark extends Benchmark {

    @Override
    @Setup
    public void setup() throws Exception {
        super.setup();
        customersFile = System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") + "customers.xml";
        BenchmarkUtil.generateXmlCustomers(customersFile, customersCount);
        easyBatchEngine = BenchmarkUtil.buildXmlEasyBatchEngine(customersFile);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(".*" + XmlBenchmark.class.getSimpleName() + ".*")
                .forks(1)
                .warmupIterations(5)
                .measurementIterations(3)
                .build();

        new Runner(opt).run();

    }

}
