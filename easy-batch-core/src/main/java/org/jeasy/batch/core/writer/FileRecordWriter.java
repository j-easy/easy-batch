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
package org.jeasy.batch.core.writer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.file.Path;

import org.jeasy.batch.core.record.Batch;
import org.jeasy.batch.core.record.Record;
import org.jeasy.batch.core.util.Utils;

/**
 * A writer that writes records to a file.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class FileRecordWriter implements RecordWriter {

    private HeaderCallback headerCallback;
    private FooterCallback footerCallback;
    private Charset charset = Charset.defaultCharset();
    private String lineSeparator = Utils.LINE_SEPARATOR;
    private boolean append;
    private OutputStreamWriter outputStreamWriter;
    private Path path;

    /**
     * Create a new {@link FileRecordWriter}.
     *
     * @param path          the output file.
     */
    public FileRecordWriter(final Path path) {
        this.path = path;
    }

    /**
     * Set the charset of the input file
     * @param charset of the input file
     */
    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    /**
     * Set the line separator
     * @param lineSeparator to use
     */
    public void setLineSeparator(String lineSeparator) {
        this.lineSeparator = lineSeparator;
    }

    /**
     * Set a header callback.
     * @param headerCallback to set
     */
    public void setHeaderCallback(HeaderCallback headerCallback) {
        this.headerCallback = headerCallback;
    }

    /**
     * Set a footer callback.
     * @param footerCallback to set
     */
    public void setFooterCallback(FooterCallback footerCallback) {
        this.footerCallback = footerCallback;
    }

    /**
     * Parameter to open the writer in append mode.
     * @param append true if the writer should be opened in append mode.
     */
    public void setAppend(boolean append) {
        this.append = append;
    }

    @Override
    public void open() throws Exception {
        outputStreamWriter = new OutputStreamWriter(new FileOutputStream(path.toFile(), append), charset);
        if (headerCallback != null) {
            headerCallback.writeHeader(outputStreamWriter);
            outputStreamWriter.write(lineSeparator);
            outputStreamWriter.flush();
        }
    }

    @Override
    public void writeRecords(Batch batch) throws Exception {
        for (Record record : batch) {
            outputStreamWriter.write(record.getPayload().toString());
            outputStreamWriter.write(lineSeparator);
        }
        outputStreamWriter.flush();
    }

    @Override
    public void close() throws Exception {
        if (footerCallback != null) {
            footerCallback.writeFooter(outputStreamWriter);
            outputStreamWriter.write(lineSeparator);
            outputStreamWriter.flush();
        }
        outputStreamWriter.close();
    }

    /**
     * Callback to write a header to the output file.
     * Implementations are not required to flush the writer or write a line separator.
     */
    public interface HeaderCallback {
        void writeHeader(OutputStreamWriter writer) throws IOException;
    }

    /**
     * Callback to write a footer to the output file.
     * Implementations are not required to flush the writer or write a line separator.
     */
    public interface FooterCallback {
        void writeFooter(OutputStreamWriter writer) throws IOException;
    }
}
