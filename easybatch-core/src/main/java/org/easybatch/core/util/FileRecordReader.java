/*
 * The MIT License
 *
 *  Copyright (c) 2015, Mahmoud Ben Hassine (mahmoud@benhassine.fr)
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

package org.easybatch.core.util;

import org.easybatch.core.api.Record;
import org.easybatch.core.api.RecordReader;

import java.io.File;
import java.text.MessageFormat;
import java.util.*;

/**
 * Reader that reads files in a directory.
 *
 * This reader is not recursive.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class FileRecordReader implements RecordReader {

    /**
     * The directory to read files from.
     */
    private File directory;

    /**
     * The array of files in the directory.
     */
    private List<File> files;

    /**
     * The data source iterator.
     */
    private Iterator<File> iterator;

    /**
     * The current record number.
     */
    private int currentRecordNumber;

    /**
     * Construct a {@link FileRecordReader} to read files (non recursively) from a given directory.
     *
     * @param directory the directory to read files from.
     */
    public FileRecordReader(File directory) {
        this.directory = directory;
    }

    /**
     * Open the reader.
     *
     * @throws Exception thrown if an exception occurs during reader opening
     */
    @Override
    public void open() throws Exception {
        if (!directory.exists()) {
            throw new IllegalArgumentException(MessageFormat.format(
                    "Directory {0} does not exist.", directory.getAbsoluteFile()));
        }
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(MessageFormat.format(
                    "{0} is not a directory.", directory.getAbsoluteFile()));
        }
        if (!directory.canRead()) {
            throw new IllegalArgumentException(MessageFormat.format(
                    "Unable to read files from directory {0}. Permission denied.",
                    directory.getAbsoluteFile()));
        }

        files = new ArrayList<File>();
        File[] filesList = directory.listFiles();
        if( filesList != null ) {
            for (File file : filesList) {
                if (file.isFile()) {
                    files.add(file);
                }
            }
        }
        this.iterator = files.listIterator();
        currentRecordNumber = 0;
    }

    /**
     * Check if the directory has a next file.
     *
     * @return true if the reader has a next record, false else.
     */
    @Override
    public boolean hasNextRecord() {
        return iterator.hasNext();
    }

    /**
     * Read next record from the data source.
     *
     * @return the next record from the data source.
     * @throws Exception thrown if an exception occurs during reading next record
     */
    @Override
    public Record readNextRecord() throws Exception {
        currentRecordNumber++;
        return new FileRecord(currentRecordNumber, iterator.next());
    }

    /**
     * Get the total record number in the data source. This is useful to calculate execution progress.
     *
     * @return the total record number in the data source or null if the total records number cannot be
     *         calculated in advance
     */
    @Override
    public Integer getTotalRecords() {
        return files.size();
    }

    /**
     * This method returns a human readable data source name to be shown in the batch report.
     *
     * @return the data source name this reader is reading data from
     */
    @Override
    public String getDataSourceName() {
        return directory.getAbsolutePath();
    }

    /**
     * Close the reader.
     *
     * @throws Exception thrown if an exception occurs during reader closing
     */
    @Override
    public void close() throws Exception {

    }
}
