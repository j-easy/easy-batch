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

import java.io.*;

/**
 * Convenient processor that writes records to a file.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class FileRecordWriter extends OutputStreamRecordWriter {

    /**
     * Convenient processor that writes records to a file.
     *
     * @param fileName the output file name.
     */
    public FileRecordWriter(final String fileName) throws IOException {
        super(new FileWriter(fileName));
    }

    /**
     * Convenient processor that writes records to a file.
     *
     * @param fileName      the output file name.
     * @param lineSeparator the line separator.
     */
    public FileRecordWriter(final String fileName, final String lineSeparator) throws IOException {
        super(new FileWriter(fileName), lineSeparator);
    }

    /**
     * Convenient processor that writes records to a file.
     *
     * @param fileName      the output file name.
     * @param lineSeparator the line separator.
     * @param charsetName   the charest name
     */
    public FileRecordWriter(final String fileName, final String lineSeparator, final String charsetName) throws IOException {
        super(new OutputStreamWriter(new FileOutputStream(fileName), charsetName), lineSeparator);
    }

    /**
     * Convenient processor that writes records to a file.
     *
     * @param file the output file.
     */
    public FileRecordWriter(final File file) throws IOException {
        super(new FileWriter(file));
    }

    /**
     * Convenient processor that writes records to a file.
     *
     * @param file          the output file.
     * @param lineSeparator the line separator.
     */
    public FileRecordWriter(final File file, final String lineSeparator) throws IOException {
        super(new FileWriter(file), lineSeparator);
    }

    /**
     * Convenient processor that writes records to a file.
     *
     * @param file          the output file.
     * @param lineSeparator the line separator.
     * @param charsetName   the charest name
     */
    public FileRecordWriter(final File file, final String lineSeparator, final String charsetName) throws IOException {
        super(new OutputStreamWriter(new FileOutputStream(file), charsetName), lineSeparator);
    }

    /**
     * Convenient processor that writes records to a file.
     *
     * @param fileWriter the output file writer.
     */
    public FileRecordWriter(final FileWriter fileWriter) throws IOException {
        super(fileWriter);
    }

    /**
     * Convenient processor that writes records to a file.
     *
     * @param fileWriter    the output file writer.
     * @param lineSeparator the line separator.
     */
    public FileRecordWriter(final FileWriter fileWriter, final String lineSeparator) throws IOException {
        super(fileWriter, lineSeparator);
    }
}
