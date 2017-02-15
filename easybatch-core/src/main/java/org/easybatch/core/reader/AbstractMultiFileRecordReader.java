package org.easybatch.core.reader;

import org.easybatch.core.record.Record;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

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

    protected List<File> files;
    protected File currentFile;
    protected AbstractFileRecordReader delegate;
    protected Iterator<File> iterator;

    /**
     * Create a new multi-file record reader.
     *
     * @param files to read
     */
    public AbstractMultiFileRecordReader(List<File> files) {
        Objects.requireNonNull(files, "files list should not be null");
        this.files = files;
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
