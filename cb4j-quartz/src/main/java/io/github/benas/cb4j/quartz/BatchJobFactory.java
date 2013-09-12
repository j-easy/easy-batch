/*
 * The MIT License
 *
 *  Copyright (c) 2013, benas (md.benhassine@gmail.com)
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */

package io.github.benas.cb4j.quartz;

import io.github.benas.cb4j.core.api.BatchEngine;
import io.github.benas.cb4j.core.config.BatchConfiguration;
import io.github.benas.cb4j.core.config.BatchConfigurationException;
import org.quartz.Job;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;

/**
 * Quartz Job factory implementation used to create CB4J batch job instances.
 * @author benas (md.benhassine@gmail.com)
 */
public class BatchJobFactory implements JobFactory {

    /**
     * The batch configuration used to configure job instances.
     */
    private BatchConfiguration batchConfiguration;

    /**
     * The batch engine class type.
     */
    private Class<? extends BatchEngine> batchEngineType;

    public BatchJobFactory(BatchConfiguration batchConfiguration, Class<? extends BatchEngine> batchEngineType) {
        this.batchConfiguration = batchConfiguration;
        this.batchEngineType = batchEngineType;

    }

    @Override
    public Job newJob(TriggerFiredBundle bundle, Scheduler scheduler) throws SchedulerException {
        try {
            batchConfiguration.configure();
            BatchEngine batchEngine = batchEngineType.getConstructor(BatchConfiguration.class).newInstance(batchConfiguration);
            return new BatchJob(batchEngine);
        } catch (BatchConfigurationException e) {
            throw new SchedulerException(e.getMessage());
        } catch (Exception e) {
            throw new SchedulerException("An unexpected exception occurred during batch job creation, root error = ", e);
        }
    }

}
