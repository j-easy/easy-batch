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
package org.easybatch.core.reader;

import org.easybatch.core.record.Record;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;

import static org.easybatch.core.util.Utils.checkNotNull;

/**
 * Template class for multi-files record readers.
 * Implementations should provide how to create the delegate reader
 * in {@link org.easybatch.core.reader.AbstractMultiFileRecordReader#createReader()} method.
 *
 * Using multi-files readers assumes <strong>all files have the same format</strong>.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public abstract class AbstractMultiFileRecordReader implements RecordReader {

    @Deprecated
    protected List<File> files;
    @Deprecated
    protected File currentFile;
    protected AbstractFileRecordReader delegate;
    @Deprecated
    protected Iterator<File> iterator;
    protected Charset charset;

    /**
     * Create a new multi-file record reader.
     *
     * @param files to read
     * @deprecated This constructor is deprecated since v5.3 and will be removed in v6.
     * It will be replaced with a constructor that accepts a {@code List<java.nio.file.Path>}
     */
    @Deprecated
    public AbstractMultiFileRecordReader(List<File> files) {
        this(files, Charset.defaultCharset());
    }

    /**
     * Create a new multi-file record reader.
     *
     * @param files to read
     * @param charset of the file
     * @deprecated This constructor is deprecated since v5.3 and will be removed in v6.
     * It will be replaced with a constructor that accepts a {@code List<java.nio.file.Path>}
     */
    @Deprecated
    public AbstractMultiFileRecordReader(List<File> files, Charset charset) {
        checkNotNull(files, "files");
        checkNotNull(charset, "charset");
        this.files = files;
        this.charset = charset;
    }

    @Override
    public void open() throws Exception {
        iterator = files.iterator();
        currentFile = iterator.next();
        if (currentFile != null) {
            delegate = createReader();
            delegate.open();
        }
    }

    @Override
    public Record readRecord() throws Exception {
        if (delegate == null) {
            return null;
        }
        Record record = delegate.readRecord();
        if (record == null) { // finished reading the current file, jump to next file
            delegate.close();
            if (iterator.hasNext()) {
                currentFile = iterator.next();
                delegate = createReader();
                delegate.open();
                return readRecord();
            }
        }
        return record;
    }

    @Override
    public void close() throws Exception {
        if (delegate != null) {
            delegate.close();
        }
    }

    protected abstract AbstractFileRecordReader createReader() throws Exception;
}
