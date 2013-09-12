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

package io.github.benas.cb4j.core.test;

import io.github.benas.cb4j.core.api.RecordMapper;
import io.github.benas.cb4j.core.api.RecordMappingException;
import io.github.benas.cb4j.core.config.BatchConfiguration;
import io.github.benas.cb4j.core.config.BatchConfigurationException;
import io.github.benas.cb4j.core.model.Record;
import io.github.benas.cb4j.core.util.BatchConstants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;

/**
 * Test class for CB4J configuration
 * @author benas (md.benhassine@gmail.com)
 */
public class BatchConfigurationTest {

    private Properties configurationProperties;

    private BatchConfiguration batchConfiguration;

    private String filePath;

    @Before
    public void setUp() throws Exception {
        configurationProperties = new Properties();
        filePath = this.getClass().getClassLoader().getResource("test.csv").getPath();
    }

    @Test(expected = BatchConfigurationException.class)
    public void testNotSpecifiedInputDataFile() throws Exception {
        batchConfiguration = new BatchConfiguration(configurationProperties);
        batchConfiguration.configure();
    }

    @Test(expected = BatchConfigurationException.class)
    public void testInvalidInputDataFile() throws Exception {
        configurationProperties.setProperty(BatchConstants.INPUT_DATA_PATH, "Blah!");
        batchConfiguration = new BatchConfiguration(configurationProperties);
        batchConfiguration.configure();
    }

    @Test(expected = BatchConfigurationException.class)
    public void testNotSpecifiedRecordSize() throws Exception {
        configurationProperties.setProperty(BatchConstants.INPUT_DATA_PATH, filePath);
        batchConfiguration = new BatchConfiguration(configurationProperties);
        batchConfiguration.configure();
    }

    @Test(expected = BatchConfigurationException.class)
    public void testNoRecordMapperRegistered() throws Exception {
        configurationProperties.setProperty(BatchConstants.INPUT_DATA_PATH, filePath);
        configurationProperties.setProperty(BatchConstants.INPUT_RECORD_SIZE, "2");
        batchConfiguration = new BatchConfiguration(configurationProperties);
        batchConfiguration.configure();
    }

    @Test(expected = BatchConfigurationException.class)
    public void testNoRecordProcessorRegistered() throws Exception {
        configurationProperties.setProperty(BatchConstants.INPUT_DATA_PATH, filePath);
        configurationProperties.setProperty(BatchConstants.INPUT_RECORD_SIZE, "2");
        batchConfiguration = new BatchConfiguration(configurationProperties);
        batchConfiguration.registerRecordMapper(new RecordMapper() {
            public Object mapRecord(Record record) throws RecordMappingException {
                return null;
            }
        });
        batchConfiguration.configure();
    }

    @Test(expected = BatchConfigurationException.class)
    public void testNotSpecifiedFieldsLength() throws Exception {
        configurationProperties.setProperty(BatchConstants.INPUT_DATA_PATH, filePath);
        configurationProperties.setProperty(BatchConstants.INPUT_RECORD_TYPE, "flr");
        batchConfiguration = new BatchConfiguration(configurationProperties);
        batchConfiguration.configure();
    }

    @Test(expected = BatchConfigurationException.class)
    public void testInvalidFieldsLength() throws Exception {
        configurationProperties.setProperty(BatchConstants.INPUT_DATA_PATH, filePath);
        configurationProperties.setProperty(BatchConstants.INPUT_RECORD_TYPE, "flr");
        configurationProperties.setProperty(BatchConstants.INPUT_FIELD_LENGTHS, "5,3,x");
        batchConfiguration = new BatchConfiguration(configurationProperties);
        batchConfiguration.configure();
    }

    @After
    public void tearDown() throws Exception {
        configurationProperties = null;
        batchConfiguration = null;
        System.gc();
    }
}
