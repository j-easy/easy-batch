/**
 * The MIT License
 *
 *   Copyright (c) 2020, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
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
package org.easybatch.extensions.apache.common.csv;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.easybatch.core.mapper.AbstractRecordMapper;
import org.easybatch.core.mapper.RecordMapper;
import org.easybatch.core.record.GenericRecord;
import org.easybatch.core.record.Record;
import org.easybatch.core.record.StringRecord;

import java.io.StringReader;

/**
 * Mapper that maps {@link StringRecord} instances to domain objects using <a href="http://commons.apache.org/proper/commons-csv/">Apache Common CSV</a>.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class ApacheCommonCsvRecordMapper<P> extends AbstractRecordMapper<P> implements RecordMapper<StringRecord, Record<P>> {

    private CSVFormat csvFormat = CSVFormat.newFormat(',');

    /**
     * Create a new {@link ApacheCommonCsvRecordMapper}.
     *
     * @param recordClass the target type class
     * @param columns     field names in order
     */
    public ApacheCommonCsvRecordMapper(Class<P> recordClass, String... columns) {
        super(recordClass);
        csvFormat = csvFormat.withHeader(columns);
    }

    @Override
    public Record<P> processRecord(final StringRecord record) throws Exception {
        String payload = record.getPayload();
        CSVParser csvParser = csvFormat.parse(new StringReader(payload));
        CSVRecord csvRecord = csvParser.iterator().next();
        csvParser.close();
        return new GenericRecord<>(record.getHeader(), objectMapper.mapObject(csvRecord.toMap()));
    }

    /*
     * Setter for parameters
     */

    /**
     * Set the trim parameter.
     *
     * @param trim parameter
     */
    public void setTrim(boolean trim) {
        csvFormat = csvFormat.withTrim(trim);
    }

    /**
     * Set the delimiter parameter.
     *
     * @param delimiter parameter
     */
    public void setDelimiter(char delimiter) {
        csvFormat = csvFormat.withDelimiter(delimiter);
    }

    /**
     * Set the quote parameter.
     *
     * @param quote parameter
     */
    public void setQuote(char quote) {
        csvFormat = csvFormat.withQuote(quote);
    }
}
