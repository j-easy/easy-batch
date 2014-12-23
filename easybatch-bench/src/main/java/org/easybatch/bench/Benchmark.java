package org.easybatch.bench;

import org.easybatch.core.impl.Engine;
import org.openjdk.jmh.annotations.*;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * JMH Class used to benchmark Easy Batch performance overhead.
 * Only record reading, parsing and mapping to domain objects are benchmarked.
 *
 * @author Mahmoud Ben Hassine (md.benhassine@gmail.com)
 */
@State(Scope.Benchmark)
public class Benchmark {

    protected String customersFile;

    protected int customersCount;

    protected Engine engine;

    @GenerateMicroBenchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void runBatch() throws Exception {
        engine.call();
    }

    @Setup
    public void setup() throws Exception {
        customersCount = Integer.parseInt(System.getProperty("org.easybatch.bench.count"));
    }

    @TearDown
    public void tearDown() {
        new File(customersFile).delete();
    }

}
