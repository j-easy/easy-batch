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

import java.io.*;
import java.nio.file.Path;

/**
 * Record writer that writes records to a file.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class FileRecordWriter extends OutputStreamRecordWriter {

    private HeaderCallback headerCallback;
    private FooterCallback footerCallback;

    /*
     * Constructors with a String
     */

    /**
     * Create a new {@link FileRecordWriter}.
     *
     * @param fileName the output file name.
     * @deprecated This constructor is deprecated since v5.3 and will be removed in v6.
     * Use {@link FileRecordWriter#FileRecordWriter(java.nio.file.Path)} instead
     */
    @Deprecated
    public FileRecordWriter(final String fileName) throws IOException {
        super(new FileWriter(fileName));
    }

    /**
     * Create a new {@link FileRecordWriter}.
     *
     * @param fileName      the output file name.
     * @param lineSeparator the line separator.
     * @deprecated This constructor is deprecated since v5.3 and will be removed in v6.
     * Use {@link FileRecordWriter#FileRecordWriter(java.nio.file.Path)} instead. Starting from v6,
     * there will be new setters for lineSeparator and charset parameters
     */
    @Deprecated
    public FileRecordWriter(final String fileName, final String lineSeparator) throws IOException {
        super(new FileWriter(fileName), lineSeparator);
    }

    /**
     * Create a new {@link FileRecordWriter}.
     *
     * @param fileName      the output file name.
     * @param lineSeparator the line separator.
     * @param charsetName   the charset name
     * @deprecated This constructor is deprecated since v5.3 and will be removed in v6.
     * Use {@link FileRecordWriter#FileRecordWriter(java.nio.file.Path)} instead. Starting from v6,
     * there will be new setters for lineSeparator and charset parameters
     */
    @Deprecated
    public FileRecordWriter(final String fileName, final String lineSeparator, final String charsetName) throws IOException {
        super(new OutputStreamWriter(new FileOutputStream(fileName), charsetName), lineSeparator);
    }

    /*
     * Constructors with a File
     */

    /**
     * Create a new {@link FileRecordWriter}.
     *
     * @param file the output file.
     * @deprecated This constructor is deprecated since v5.3 and will be removed in v6.
     * Use {@link FileRecordWriter#FileRecordWriter(java.nio.file.Path)} instead.
     */
    @Deprecated
    public FileRecordWriter(final File file) throws IOException {
        super(new FileWriter(file));
    }

    /**
     * Create a new {@link FileRecordWriter}.
     *
     * @param file          the output file.
     * @param lineSeparator the line separator.
     * @deprecated This constructor is deprecated since v5.3 and will be removed in v6.
     * Use {@link FileRecordWriter#FileRecordWriter(java.nio.file.Path)} instead. Starting from v6,
     * there will be new setters for lineSeparator and charset parameters
     */
    @Deprecated
    public FileRecordWriter(final File file, final String lineSeparator) throws IOException {
        super(new FileWriter(file), lineSeparator);
    }

    /**
     * Create a new {@link FileRecordWriter}.
     *
     * @param file          the output file.
     * @param lineSeparator the line separator.
     * @param charsetName   the charset name
     * @deprecated This constructor is deprecated since v5.3 and will be removed in v6.
     * Use {@link FileRecordWriter#FileRecordWriter(java.nio.file.Path)} instead. Starting from v6,
     * there will be new setters for lineSeparator and charset parameters
     */
    @Deprecated
    public FileRecordWriter(final File file, final String lineSeparator, final String charsetName) throws IOException {
        super(new OutputStreamWriter(new FileOutputStream(file), charsetName), lineSeparator);
    }

    /*
     * Constructors with a Path
     */

    /**
     * Create a new {@link FileRecordWriter}.
     *
     * @param path the output file.
     */
    public FileRecordWriter(final Path path) throws IOException {
        super(new FileWriter(path.toFile()));
    }

    /**
     * Create a new {@link FileRecordWriter}.
     *
     * @param path          the output file.
     * @param lineSeparator the line separator.
     * @deprecated This constructor is deprecated since v5.3 and will be removed in v6.
     * Use {@link FileRecordWriter#FileRecordWriter(java.nio.file.Path)} instead. Starting from v6,
     * there will be new setters for lineSeparator and charset parameters
     */
    @Deprecated
    public FileRecordWriter(final Path path, final String lineSeparator) throws IOException {
        this(path.toFile(), lineSeparator);
    }

    /**
     * Create a new {@link FileRecordWriter}.
     *
     * @param path          the output file.
     * @param lineSeparator the line separator.
     * @param charsetName   the charset name
     * @deprecated This constructor is deprecated since v5.3 and will be removed in v6.
     * Use {@link FileRecordWriter#FileRecordWriter(java.nio.file.Path)} instead. Starting from v6,
     * there will be new setters for lineSeparator and charset parameters
     */
    @Deprecated
    public FileRecordWriter(final Path path, final String lineSeparator, final String charsetName) throws IOException {
        this(path.toFile(), lineSeparator, charsetName);
    }

    /*
     * Constructors with a FileWriter
     */

    /**
     * Create a new {@link FileRecordWriter}.
     *
     * @param fileWriter the output file writer.
     * @deprecated This constructor is deprecated since v5.3 and will be removed in v6.
     * Use {@link FileRecordWriter#FileRecordWriter(java.nio.file.Path)} instead
     */
    @Deprecated
    public FileRecordWriter(final FileWriter fileWriter) throws IOException {
        super(fileWriter);
    }

    /**
     * Create a new {@link FileRecordWriter}.
     *
     * @param fileWriter    the output file writer.
     * @param lineSeparator the line separator.
     * @deprecated This constructor is deprecated since v5.3 and will be removed in v6.
     * Use {@link FileRecordWriter#FileRecordWriter(java.nio.file.Path)} instead. Starting from v6,
     * there will be new setters for lineSeparator and charset parameters
     */
    @Deprecated
    public FileRecordWriter(final FileWriter fileWriter, final String lineSeparator) throws IOException {
        super(fileWriter, lineSeparator);
    }

    /* No constructor with a charsetName parameter because it is not applicable to FileWriter */

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

    @Override
    public void open() throws Exception {
        if (headerCallback != null) {
            headerCallback.writeHeader(outputStreamWriter);
            outputStreamWriter.write(lineSeparator);
            outputStreamWriter.flush();
        }
    }

    @Override
    public void close() throws Exception {
        if (footerCallback != null) {
            footerCallback.writeFooter(outputStreamWriter);
            outputStreamWriter.write(lineSeparator);
            outputStreamWriter.flush();
        }
        super.close();
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
