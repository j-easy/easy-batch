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

package org.easybatch.extensions.apache.common.csv;

import org.apache.commons.csv.CSVRecord;
import org.easybatch.core.mapper.AbstractRecordMapper;
import org.easybatch.core.mapper.RecordMapper;
import org.easybatch.core.mapper.RecordMappingException;
import org.easybatch.core.record.GenericRecord;

/**
 * Mapper that maps {@link ApacheCommonCsvRecord} instances to domain objects.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class ApacheCommonCsvRecordMapper extends AbstractRecordMapper implements RecordMapper<ApacheCommonCsvRecord, GenericRecord> {

    /**
     * Create a {@link ApacheCommonCsvRecordMapper}.
     *
     * @param recordClass the target type class
     */
    public ApacheCommonCsvRecordMapper(Class recordClass) {
        super(recordClass);
    }

    @Override
    public GenericRecord processRecord(final ApacheCommonCsvRecord record) throws RecordMappingException {
        CSVRecord csvRecord = record.getPayload();
        return new GenericRecord(record.getHeader(), objectMapper.mapObject(csvRecord.toMap()));
    }

}
