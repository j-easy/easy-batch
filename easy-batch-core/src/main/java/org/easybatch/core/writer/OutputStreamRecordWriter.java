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
package org.easybatch.core.writer;

import org.easybatch.core.listener.JobListener;
import org.easybatch.core.record.Batch;
import org.easybatch.core.record.Record;

import java.io.OutputStreamWriter;

import static org.easybatch.core.util.Utils.LINE_SEPARATOR;
import static org.easybatch.core.util.Utils.checkNotNull;

/**
 * Record writer to write the <strong>payload</strong> of a {@link Record} to an output stream.
 * Users of this class is responsible for opening/closing the output stream, maybe using a {@link JobListener}.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 * @deprecated This class is deprecated since v5.3 and will be removed in v6.
 */
@Deprecated
public class OutputStreamRecordWriter implements RecordWriter {

    protected String lineSeparator;
    protected OutputStreamWriter outputStreamWriter;

    /**
     * Create a new {@link OutputStreamRecordWriter}.
     *
     * @param outputStreamWriter the output stream to write records to.
     */
    @Deprecated
    public OutputStreamRecordWriter(final OutputStreamWriter outputStreamWriter) {
        this(outputStreamWriter, LINE_SEPARATOR);
    }

    /**
     * Create a new {@link OutputStreamRecordWriter}.
     *
     * @param outputStreamWriter the output stream to write records to.
     * @param lineSeparator      the line separator.
     */
    @Deprecated
    public OutputStreamRecordWriter(final OutputStreamWriter outputStreamWriter, final String lineSeparator) {
        checkNotNull(outputStreamWriter, "output stream writer");
        checkNotNull(lineSeparator, "line separator");
        this.outputStreamWriter = outputStreamWriter;
        this.lineSeparator = lineSeparator;
    }

    @Override
    @Deprecated
    public void open() throws Exception {
        // no op
    }

    @Override
    @Deprecated
    public void writeRecords(Batch batch) throws Exception {
        for (Record record : batch) {
            outputStreamWriter.write(record.getPayload().toString());
            outputStreamWriter.write(lineSeparator);
        }
        outputStreamWriter.flush();

    }

    @Override
    @Deprecated
    public void close() throws Exception {
        outputStreamWriter.close();
    }
}
