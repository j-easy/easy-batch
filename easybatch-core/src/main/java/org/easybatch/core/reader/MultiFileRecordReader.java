package org.easybatch.core.reader;

import org.easybatch.core.record.Record;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Read records from multiple files <strong>having the same format</strong>.
 *
 * Returns records read by the delegate reader.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class MultiFileRecordReader implements RecordReader {

    private File directory;

    private FilenameFilter filenameFilter;

    private AbstractFileRecordReader delegate;

    private List<File> files = new ArrayList<>();

    public MultiFileRecordReader(final File directory, final FilenameFilter filenameFilter, final AbstractFileRecordReader delegate) {
        this.directory = directory;
        this.filenameFilter = filenameFilter;
        this.delegate = delegate;
    }

    @Override
    public void open() throws Exception {
        Collections.addAll(files, directory.listFiles(filenameFilter));
    }

    @Override
    public Record readRecord() throws Exception {
        // TODO implement logic
        return delegate.readRecord();
    }

    @Override
    public void close() throws Exception {
        delegate.close();
    }
}
