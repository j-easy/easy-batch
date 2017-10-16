/**
 * The MIT License
 *
 *   Copyright (c) 2017, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
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

    /*
     * Constructors with a String
     */

    /**
     * Create a new {@link FileRecordWriter}.
     *
     * @param fileName the output file name.
     */
    public FileRecordWriter(final String fileName) throws IOException {
        super(new FileWriter(fileName));
    }

    /**
     * Create a new {@link FileRecordWriter}.
     *
     * @param fileName      the output file name.
     * @param lineSeparator the line separator.
     */
    public FileRecordWriter(final String fileName, final String lineSeparator) throws IOException {
        super(new FileWriter(fileName), lineSeparator);
    }

    /**
     * Create a new {@link FileRecordWriter}.
     *
     * @param fileName      the output file name.
     * @param lineSeparator the line separator.
     * @param charsetName   the charset name
     */
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
     */
    public FileRecordWriter(final File file) throws IOException {
        super(new FileWriter(file));
    }

    /**
     * Create a new {@link FileRecordWriter}.
     *
     * @param file          the output file.
     * @param lineSeparator the line separator.
     */
    public FileRecordWriter(final File file, final String lineSeparator) throws IOException {
        super(new FileWriter(file), lineSeparator);
    }

    /**
     * Create a new {@link FileRecordWriter}.
     *
     * @param file          the output file.
     * @param lineSeparator the line separator.
     * @param charsetName   the charset name
     */
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
     */
    public FileRecordWriter(final Path path, final String lineSeparator) throws IOException {
        this(path.toFile(), lineSeparator);
    }

    /**
     * Create a new {@link FileRecordWriter}.
     *
     * @param path          the output file.
     * @param lineSeparator the line separator.
     * @param charsetName   the charset name
     */
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
     */
    public FileRecordWriter(final FileWriter fileWriter) throws IOException {
        super(fileWriter);
    }

    /**
     * Create a new {@link FileRecordWriter}.
     *
     * @param fileWriter    the output file writer.
     * @param lineSeparator the line separator.
     */
    public FileRecordWriter(final FileWriter fileWriter, final String lineSeparator) throws IOException {
        super(fileWriter, lineSeparator);
    }

    /* No constructor with a charsetName parameter because it is not applicable to FileWriter */
}
