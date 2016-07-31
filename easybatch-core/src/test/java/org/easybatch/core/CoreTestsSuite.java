/*
 * The MIT License
 *
 *  Copyright (c) 2016, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
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

package org.easybatch.core;

import org.easybatch.core.converter.*;
import org.easybatch.core.dispatcher.*;
import org.easybatch.core.filter.*;
import org.easybatch.core.job.*;
import org.easybatch.core.mapper.BatchMapperTest;
import org.easybatch.core.mapper.ObjectMapperTest;
import org.easybatch.core.marshaller.BatchMarshallerTest;
import org.easybatch.core.processor.BatchProcessorTest;
import org.easybatch.core.processor.RecordCollectorTest;
import org.easybatch.core.reader.*;
import org.easybatch.core.validator.BatchValidatorTest;
import org.easybatch.core.writer.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Test suite for easy batch core module.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        // reader
        FileRecordReaderTest.class,
        FileBatchReaderTest.class,
        BlockingQueueRecordReaderTest.class,
        StandardInputRecordReaderTest.class,
        IterableRecordReaderTest.class,
        IterableBatchReaderTest.class,
        StringRecordReaderTest.class,
        StringBatchReaderTest.class,

        // mapper
        BatchMapperTest.class,
        ObjectMapperTest.class,

        // marshaller
        BatchMarshallerTest.class,

        // converter
        AtomicIntegerTypeConverterTest.class,
        AtomicLongTypeConverterTest.class,
        BigDecimalTypeConverterTest.class,
        BigIntegerTypeConverterTest.class,
        BooleanTypeConverterTest.class,
        ByteTypeConverterTest.class,
        CharacterTypeConverterTest.class,
        DateTypeConverterTest.class,
        GregorianCalendarTypeConverterTest.class,
        DoubleTypeConverterTest.class,
        FloatTypeConverterTest.class,
        IntegerTypeConverterTest.class,
        LongTypeConverterTest.class,
        ShortTypeConverterTest.class,

        // filter
        StartWithStringRecordFilterTest.class,
        EndWithStringRecordFilterTest.class,
        FileExtensionFilterTest.class,
        GrepFilterTest.class,
        HeaderRecordFilterTest.class,
        PoisonRecordFilterTest.class,
        RecordNumberBetweenFilterTest.class,
        RecordNumberEqualToFilterTest.class,
        RecordNumberGreaterThanFilterTest.class,
        RecordNumberLowerThanFilterTest.class,
        EmptyRecordFilterTest.class,
        BatchFilterTest.class,

        // dispatcher
        BroadcastRecordDispatcherTest.class,
        ContentBasedRecordDispatcherTest.class,
        ContentBasedRecordDispatcherBuilderTest.class,
        RandomRecordDispatcherTest.class,
        RoundRobinRecordDispatcherTest.class,

        //writer
        BlockingQueueRecordWriterTest.class,
        CollectionBatchWriterTest.class,
        CollectionRecordWriterTest.class,
        FileRecordWriterTest.class,
        FileBatchWriterTest.class,
        OutputStreamRecordWriterTest.class,
        OutputStreamBatchWriterTest.class,
        StandardOutputRecordWriterTest.class,
        StandardOutputBatchWriterTest.class,
        StringRecordWriterTest.class,
        StringBatchWriterTest.class,

        // processor
        BatchProcessorTest.class,
        RecordCollectorTest.class,

        // validator
        BatchValidatorTest.class,

        // job
        JobReportTest.class,
        JobImplTest.class,
        JobExecutorTest.class,
        ProcessingPipelineTest.class,
        EventManagerTest.class
})
public class CoreTestsSuite {
}
