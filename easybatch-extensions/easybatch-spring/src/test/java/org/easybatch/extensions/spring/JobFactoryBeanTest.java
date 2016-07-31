/*
 *  The MIT License
 *
 *   Copyright (c) 2016, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in
 *   all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *   THE SOFTWARE.
 */

package org.easybatch.extensions.spring;

import org.easybatch.core.job.Job;
import org.easybatch.core.job.JobExecutor;
import org.easybatch.core.job.JobReport;
import org.easybatch.core.job.JobStatus;
import org.easybatch.core.listener.JobListener;
import org.easybatch.core.listener.PipelineListener;
import org.easybatch.core.listener.RecordReaderListener;
import org.easybatch.core.processor.RecordProcessor;
import org.easybatch.core.reader.RecordReader;
import org.easybatch.core.util.Utils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class JobFactoryBeanTest {

    @Rule
    public final SystemOutRule systemOut = new SystemOutRule().enableLog();

    @Mock
    private RecordReader recordReader;
    @Mock
    private RecordProcessor recordProcessor;
    @Mock
    private JobListener jobListener;
    @Mock
    private RecordReaderListener recordReaderListener;
    @Mock
    private PipelineListener pipelineListener;

    private JobFactoryBean jobFactoryBean;

    @Before
    public void setUp() throws Exception {
        jobFactoryBean = new JobFactoryBean();

        jobFactoryBean.setRecordReader(recordReader);
        jobFactoryBean.setProcessingPipeline(singletonList(recordProcessor));

        jobFactoryBean.setJobListeners(singletonList(jobListener));
        jobFactoryBean.setRecordReaderListeners(singletonList(recordReaderListener));
        jobFactoryBean.setPipelineListeners(singletonList(pipelineListener));
    }

    @Test
    public void testGetObject() throws Exception {
        Job job = jobFactoryBean.getObject();
        assertThat(job).isNotNull();
        // TODO assert that fields are correctly set through reflection
    }

    @Test
    public void testGetObjectType() throws Exception {
        assertThat(jobFactoryBean.getObjectType()).isEqualTo(Job.class);
    }

    @Test
    public void testIsSingleton() throws Exception {
        assertThat(jobFactoryBean.isSingleton()).isTrue();
    }

    @Test
    public void integrationTest() {
        ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
        Job job = context.getBean("job", Job.class);
        JobReport report = JobExecutor.execute(job);

        assertThat(report).isNotNull();
        assertThat(report.getStatus()).isEqualTo(JobStatus.COMPLETED);
        assertThat(report.getMetrics()).isNotNull();
        assertThat(report.getMetrics().getTotalCount()).isEqualTo(2);
        assertThat(report.getMetrics().getSkippedCount()).isEqualTo(1);
        assertThat(report.getMetrics().getSuccessCount()).isEqualTo(1);
        assertThat(systemOut.getLog()).isEqualTo("world" + Utils.LINE_SEPARATOR);
    }
}
