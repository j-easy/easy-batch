package org.easybatch.core.job;

import org.junit.Test;

public class JobBuilderTest {

    @Test(expected = IllegalArgumentException.class)
    public void whenBatchSizeIsLessThanOne_thenShouldThrowAnIllegalArgumentException() throws Exception {
        JobBuilder.aNewJob().batchSize(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenErrorThresholdIsLessThanOne_thenShouldThrowAnIllegalArgumentException() throws Exception {
        JobBuilder.aNewJob().errorThreshold(0);
    }
}