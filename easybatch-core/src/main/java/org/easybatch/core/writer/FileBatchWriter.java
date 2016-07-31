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

package org.easybatch.core.writer;

import org.easybatch.core.record.Batch;
import org.easybatch.core.record.Record;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static org.easybatch.core.util.Utils.checkNotNull;

/**
 * Writes a batch of records to a file.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class FileBatchWriter implements RecordWriter<Batch> {

    /**
     * The delegate {@link FileRecordWriter}.
     */
    private FileRecordWriter fileRecordWriter;

    /**
     * Writes a batch of records to a file.
     *
     * @param fileWriter the file writer to write records to
     * @throws IOException thrown if the file cannot be opened
     */
    public FileBatchWriter(final FileWriter fileWriter) throws IOException {
        checkNotNull(fileWriter, "file writer");
        this.fileRecordWriter = new FileRecordWriter(fileWriter);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Batch processRecord(final Batch batch) throws RecordWritingException {
        List<Record> records = batch.getPayload();
        for (Record record : records) {
            fileRecordWriter.processRecord(record);
        }
        return batch;
    }

}
