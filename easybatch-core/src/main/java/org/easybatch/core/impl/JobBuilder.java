/*
 * The MIT License
 *
 *  Copyright (c) 2015, Mahmoud Ben Hassine (mahmoud@benhassine.fr)
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

package org.easybatch.core.impl;

import org.easybatch.core.api.*;
import org.easybatch.core.api.listener.JobListener;
import org.easybatch.core.api.listener.PipelineListener;
import org.easybatch.core.api.listener.RecordReaderListener;

import static org.easybatch.core.util.Utils.checkArgument;
import static org.easybatch.core.util.Utils.checkNotNull;

/**
 * Job instance builder.
 * This is the main entry point to configure a job.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public final class JobBuilder {

    /**
     * The job to build.
     */
    private JobImpl job;

    public JobBuilder() {
        job = new JobImpl();
    }

    /**
     * Create a new {@link JobBuilder}.
     *
     * @return a new job builder.
     */
    public static JobBuilder aNewJob() {
        return new JobBuilder();
    }

    /**
     * Set the job name.
     *
     * @param name the job name
     * @return the job builder
     */
    public JobBuilder named(final String name) {
        checkNotNull(name, "job name");
        job.getJobReport().getParameters().setName(name);
        return this;
    }

    /**
     * Set the number of records to skip.
     *
     * @param number the number of records to skip
     * @return the job builder
     */
    public JobBuilder skip(final long number) {
        checkArgument(number >= 1, "The number of records to skip should be >= 1");
        job.getJobReport().getParameters().setSkip(number);
        return this;
    }

    /**
     * Set the limit number of records to process.
     *
     * @param number the limit number of records to process
     * @return the job builder
     */
    public JobBuilder limit(final long number) {
        checkArgument(number >= 1, "The limit number of records should be >= 1");
        job.getJobReport().getParameters().setLimit(number);
        return this;
    }

    /**
     * Register a record reader.
     *
     * @param recordReader the record reader to register
     * @return the job builder
     */
    public JobBuilder reader(final RecordReader recordReader) {
        return reader(recordReader, false);
    }

    /**
     * Register a record reader.
     *
     * @param recordReader the record reader to register
     * @param keepAlive    true if the reader should <strong>NOT</strong> be closed
     * @return the job builder
     */
    public JobBuilder reader(final RecordReader recordReader, final boolean keepAlive) {
        checkNotNull(recordReader, "record reader");
        job.setRecordReader(recordReader);
        job.getJobReport().getParameters().setKeepAlive(keepAlive);
        return this;
    }

    /**
     * Register a record filter.
     *
     * @param recordFilter the record filter to register
     * @return the job builder
     */
    public JobBuilder filter(final RecordFilter recordFilter) {
        checkNotNull(recordFilter, "record filter");
        job.addRecordProcessor(recordFilter);
        return this;
    }

    /**
     * Register a record mapper.
     *
     * @param recordMapper the record mapper to register
     * @return the job builder
     */
    public JobBuilder mapper(final RecordMapper recordMapper) {
        checkNotNull(recordMapper, "record mapper");
        job.addRecordProcessor(recordMapper);
        return this;
    }

    /**
     * Register a record validator.
     *
     * @param recordValidator the record validator to register
     * @return the job builder
     */
    public JobBuilder validator(final RecordValidator recordValidator) {
        checkNotNull(recordValidator, "record validator");
        job.addRecordProcessor(recordValidator);
        return this;
    }

    /**
     * Register a record processor.
     *
     * @param recordProcessor the record processor to register
     * @return the job builder
     */
    public JobBuilder processor(final RecordProcessor recordProcessor) {
        checkNotNull(recordProcessor, "record processor");
        job.addRecordProcessor(recordProcessor);
        return this;
    }

    /**
     * Register a record marshaller.
     *
     * @param recordMarshaller the record marshaller to register
     * @return the job builder
     */
    public JobBuilder marshaller(final RecordMarshaller recordMarshaller) {
        checkNotNull(recordMarshaller, "record marshaller");
        job.addRecordProcessor(recordMarshaller);
        return this;
    }

    /**
     * Register a record writer.
     *
     * @param recordWriter the record writer to register
     * @return the job builder
     */
    public JobBuilder writer(final RecordWriter recordWriter) {
        checkNotNull(recordWriter, "record writer");
        job.addRecordProcessor(recordWriter);
        return this;
    }

    /**
     * Enable strict mode : if true, then the execution will be aborted on first processing error.
     *
     * @param strictMode true if strict mode should be enabled
     * @return the job builder
     */
    public JobBuilder strictMode(final boolean strictMode) {
        job.getJobReport().getParameters().setStrictMode(strictMode);
        return this;
    }

    /**
     * Parameter to mute all loggers.
     *
     * @param silentMode true to enable silent mode
     * @return the job builder
     */
    public JobBuilder silentMode(final boolean silentMode) {
        job.getJobReport().getParameters().setSilentMode(silentMode);
        return this;
    }

    /**
     * Activate JMX monitoring.
     *
     * @param jmx true to enable jmx monitoring
     * @return the job builder
     */
    public JobBuilder jmxMode(final boolean jmx) {
        job.getJobReport().getParameters().setJmxMode(jmx);
        return this;
    }

    /**
     * Register a job listener.
     * See {@link JobListener} for available callback methods.
     *
     * @param jobListener The job listener to add.
     * @return the job builder
     */
    public JobBuilder jobEventListener(final JobListener jobListener) {
        checkNotNull(jobListener, "job listener");
        job.addJobListener(jobListener);
        return this;
    }

    /**
     * Register a record reader listener.
     * See {@link RecordReaderListener} for available callback methods.
     *
     * @param recordReaderListener The record reader listener to add.
     * @return the job builder
     */
    public JobBuilder readerEventListener(final RecordReaderListener recordReaderListener) {
        checkNotNull(recordReaderListener, "record reader listener");
        job.addRecordReaderListener(recordReaderListener);
        return this;
    }

    /**
     * Register a pipeline listener.
     * See {@link PipelineListener} for available callback methods.
     *
     * @param pipelineListener The pipeline listener to add.
     * @return the job builder
     */
    public JobBuilder pipelineEventListener(final PipelineListener pipelineListener) {
        checkNotNull(pipelineListener, "pipeline listener");
        job.addPipelineListener(pipelineListener);
        return this;
    }

    /**
     * Build an Easy Batch job instance.
     *
     * @return an Easy Batch job instance
     */
    public Job build() {
        return job;
    }

    /**
     * Build and call the job.
     *
     * @return job execution report
     */
    public JobReport call() {
        return job.call();
    }

}
