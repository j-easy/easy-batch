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

import org.easybatch.core.api.Engine;
import org.easybatch.core.api.RecordProcessor;
import org.easybatch.core.api.RecordReader;
import org.easybatch.core.api.event.EventManager;
import org.easybatch.core.api.event.JobEventListener;
import org.easybatch.core.api.event.PipelineEventListener;
import org.easybatch.core.api.event.RecordReaderEventListener;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class EngineFactoryBeanTest {

    private EngineFactoryBean engineFactoryBean;

    @Mock
    private RecordReader recordReader;
    @Mock
    private RecordProcessor recordProcessor;
    @Mock
    private JobEventListener jobEventListener;
    @Mock
    private RecordReaderEventListener recordReaderEventListener;
    @Mock
    private PipelineEventListener pipelineEventListener;
    @Mock
    private EventManager eventManager;

    @Before
    public void setUp() throws Exception {
        engineFactoryBean = new EngineFactoryBean();

        engineFactoryBean.setRecordReader(recordReader);
        engineFactoryBean.setProcessingPipeline(singletonList(recordProcessor));

        engineFactoryBean.setJobEventListeners(singletonList(jobEventListener));
        engineFactoryBean.setRecordReaderEventListeners(singletonList(recordReaderEventListener));
        engineFactoryBean.setPipelineEventListeners(singletonList(pipelineEventListener));
        engineFactoryBean.setEventManager(eventManager);

    }

    @Test
    public void testGetObject() throws Exception {
        Engine engine = engineFactoryBean.getObject();
        assertThat(engine).isNotNull();
        // TODO assert that fields are correctly set through reflection
    }

    @Test
    public void testGetObjectType() throws Exception {
        assertThat(engineFactoryBean.getObjectType()).isEqualTo(Engine.class);
    }

    @Test
    public void testIsSingleton() throws Exception {
        assertThat(engineFactoryBean.isSingleton()).isTrue();
    }
}
