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

import org.easybatch.core.api.*;
import org.easybatch.core.api.event.EventManager;
import org.easybatch.core.api.event.job.JobEventListener;
import org.easybatch.core.api.event.step.*;
import org.easybatch.core.api.handler.ErrorRecordHandler;
import org.easybatch.core.api.handler.FilteredRecordHandler;
import org.easybatch.core.api.handler.IgnoredRecordHandler;
import org.easybatch.core.api.handler.RejectedRecordHandler;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for {@link EngineFactoryBean}.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
@RunWith(MockitoJUnitRunner.class)
public class EngineFactoryBeanTest {

    EngineFactoryBean engineFactoryBean;

    @Mock
    private RecordReader recordReader;
    @Mock
    private RecordFilter recordFilter;
    @Mock
    private RecordMapper recordMapper;
    @Mock
    private RecordValidator recordValidator;
    @Mock
    private RecordProcessor recordProcessor;
    @Mock
    private FilteredRecordHandler filteredRecordHandler;
    @Mock
    private IgnoredRecordHandler ignoredRecordHandler;
    @Mock
    private RejectedRecordHandler rejectedRecordHandler;
    @Mock
    private ErrorRecordHandler errorRecordHandler;
    @Mock
    private JobEventListener jobEventListener;
    @Mock
    private RecordReaderEventListener recordReaderEventListener;
    @Mock
    private RecordFilterEventListener recordFilterEventListener;
    @Mock
    private RecordMapperEventListener recordMapperEventListener;
    @Mock
    private RecordValidatorEventListener recordValidatorEventListener;
    @Mock
    private RecordProcessorEventListener recordProcessorEventListener;
    @Mock
    private EventManager eventManager;

    @Before
    public void setUp() throws Exception {
        engineFactoryBean = new EngineFactoryBean();

        engineFactoryBean.setRecordReader(recordReader);
        engineFactoryBean.setFilterChain(singletonList(recordFilter));
        engineFactoryBean.setRecordMapper(recordMapper);
        engineFactoryBean.setValidationPipeline(singletonList(recordValidator));
        engineFactoryBean.setProcessingPipeline(singletonList(recordProcessor));

        engineFactoryBean.setFilteredRecordHandler(filteredRecordHandler);
        engineFactoryBean.setIgnoredRecordHandler(ignoredRecordHandler);
        engineFactoryBean.setRejectedRecordHandler(rejectedRecordHandler);
        engineFactoryBean.setErrorRecordHandler(errorRecordHandler);

        engineFactoryBean.setJobEventListeners(singletonList(jobEventListener));
        engineFactoryBean.setRecordReaderEventListeners(singletonList(recordReaderEventListener));
        engineFactoryBean.setRecordFilterEventListeners(singletonList(recordFilterEventListener));
        engineFactoryBean.setRecordMapperEventListeners(singletonList(recordMapperEventListener));
        engineFactoryBean.setRecordProcessorEventListeners(singletonList(recordProcessorEventListener));
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