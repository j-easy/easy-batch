/*
 *  The MIT License
 *
 *   Copyright (c) 2015, Mahmoud Ben Hassine (mahmoud@benhassine.fr)
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

package org.easybatch.integration.spring;

import org.easybatch.core.job.Job;
import org.easybatch.core.listener.JobListener;
import org.easybatch.core.listener.PipelineListener;
import org.easybatch.core.listener.RecordReaderListener;
import org.easybatch.core.processor.RecordProcessor;
import org.easybatch.core.reader.RecordReader;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class JobFactoryBeanTest {

    private JobFactoryBean jobFactoryBean;

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
}
