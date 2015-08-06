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

package org.easybatch.core.writer;

import org.easybatch.core.api.Record;
import org.easybatch.core.api.RecordProcessingException;

import java.io.IOException;
import java.io.OutputStreamWriter;

import static java.lang.String.format;
import static org.easybatch.core.util.Utils.*;

/**
 * Convenient processor to write the <strong>payload</strong> of a {@link Record} to an output stream.
 * <p/>
 * Users of this class are responsible for opening/closing the output stream, maybe using
 * a {@link org.easybatch.core.api.event.job.JobEventListener}.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class OutputStreamRecordWriter extends AbstractRecordWriter {

    private String lineSeparator;

    private OutputStreamWriter outputStreamWriter;

    /**
     * Convenient processor to write the <strong>payload</strong> of a {@link Record} to an output stream.
     * <p/>
     * The user of this class is responsible for opening/closing the output stream, maybe using
     * a {@link org.easybatch.core.api.event.job.JobEventListener}.
     *
     * @param outputStreamWriter the output stream to write records to.
     */
    public OutputStreamRecordWriter(OutputStreamWriter outputStreamWriter) {
        this(outputStreamWriter, LINE_SEPARATOR);
    }

    /**
     * Convenient processor to write the <strong>payload</strong> of a {@link Record} to an output stream.
     * <p/>
     * The user of this class is responsible for opening/closing the output stream, maybe using
     * a {@link org.easybatch.core.api.event.job.JobEventListener}.
     *
     * @param outputStreamWriter the output stream to write records to.
     * @param lineSeparator the line separator.
     */
    public OutputStreamRecordWriter(OutputStreamWriter outputStreamWriter, String lineSeparator) {
        checkNotNull(outputStreamWriter, "output stream writer");
        this.outputStreamWriter = outputStreamWriter;
        this.lineSeparator = lineSeparator;
    }

    @Override
    public void writeRecord(final Object record) throws RecordProcessingException {
        Object payload = isRecord(record) ? ((Record) record).getPayload() : record;
        try {
            outputStreamWriter.write(payload.toString());
            outputStreamWriter.write(lineSeparator);
            outputStreamWriter.flush();
        } catch (IOException exception) {
            String message = format("Unable to write record %s to the output stream writer", record);
            throw new RecordProcessingException(message, exception);
        }
    }

}
