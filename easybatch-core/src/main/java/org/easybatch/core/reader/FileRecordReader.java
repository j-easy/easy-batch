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

package org.easybatch.core.reader;

import org.easybatch.core.record.FileRecord;
import org.easybatch.core.record.Header;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static java.lang.String.format;
import static org.easybatch.core.util.Utils.checkArgument;

/**
 * A convenient {@link RecordReader} that recursively reads files in a directory.
 * <p/>
 * This reader produces {@link FileRecord} instances.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
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
    private long currentRecordNumber;

    /**
     * Create a {@link FileRecordReader} to read files recursively from a given directory.
     *
     * @param directory the directory to read files from.
     */
    public FileRecordReader(File directory) {
        this.directory = directory;
    }

    /**
     * Open the reader.
     */
    @Override
    public void open() {
        checkDirectory();
        files = getFiles(directory);
        iterator = files.listIterator();
        currentRecordNumber = 0;
    }

    private List<File> getFiles(final File directory) {
        List<File> files = new ArrayList<>();
        File[] filesList = directory.listFiles();
        if (filesList != null) {
            for (File file : filesList) {
                if (file.isFile()) {
                    files.add(file);
                } else {
                    files.addAll(getFiles(file));
                }
            }
        }
        return files;
    }

    private void checkDirectory() {
        checkArgument(directory.exists(), format("Directory %s does not exist.", directory.getAbsolutePath()));
        checkArgument(directory.isDirectory(), format("%s is not a directory.", directory.getAbsolutePath()));
        checkArgument(directory.canRead(), format("Unable to read files from directory %s. Permission denied.", directory.getAbsolutePath()));
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
     */
    @Override
    public FileRecord readNextRecord() {
        Header header = new Header(++currentRecordNumber, getDataSourceName(), new Date());
        return new FileRecord(header, iterator.next());
    }

    /**
     * Get the total record number in the data source. This is useful to calculate execution progress.
     *
     * @return the total record number in the data source or null if the total records number cannot be
     * calculated in advance
     */
    @Override
    public Long getTotalRecords() {
        return (long) files.size();
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
     */
    @Override
    public void close() {
        // no op
    }
}
