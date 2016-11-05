package org.easybatch.core.reader;

import org.easybatch.core.record.Record;

import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Read records from multiple files <strong>having the same format</strong>.
 *
 * Records produced by this reader will have continuous numbers as if they were in the <strong>same file</strong>.
 *
 * Returns records read by the delegate reader.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class MultiFileRecordReader implements RecordReader {

    private File directory;

    private FilenameFilter filenameFilter;

    private AbstractFileRecordReader delegate;

    private Class<? extends AbstractFileRecordReader> delegateType;

    private List<File> files = new ArrayList<>();

    private File currentFile;

    private Iterator<File> iterator;

    /**
     * Create a new {@link MultiFileRecordReader}.
     *
     * @param directory to read records from
     * @param filenameFilter to filter files
     * @param delegateType the type of the delegate reader to use
     */
    public MultiFileRecordReader(final File directory, final FilenameFilter filenameFilter, final Class<? extends AbstractFileRecordReader> delegateType) {
        this.directory = directory;
        this.filenameFilter = filenameFilter;
        this.delegateType = delegateType;
    }

    /**
     * Create a new {@link MultiFileRecordReader}.
     *
     * @param directory to read records from
     * @param extension files extension. Example: csv, xml or json (without the '.')
     * @param delegateType the type of the delegate reader to use
     */
    public MultiFileRecordReader(final File directory, final String extension, final Class<? extends AbstractFileRecordReader> delegateType) {
        this(directory, new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(extension);
            }
        }, delegateType);
    }

    @Override
    public void open() throws Exception {
        File[] filesList = directory.listFiles(filenameFilter);
        if (filesList != null) {
            Collections.addAll(files, filesList);
            iterator = files.iterator();
            currentFile = iterator.next();
            delegate = instantiateDelegateWith(currentFile);
            delegate.open();
        }
    }

    @Override
    public Record readRecord() throws Exception {
        if (delegate == null) {
            return null;
        }
        Record record = delegate.readRecord();
        if (record == null) {
            delegate.close();
            if (iterator.hasNext()) {
                currentFile = iterator.next();
                delegate = instantiateDelegateWith(currentFile);
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

    private AbstractFileRecordReader instantiateDelegateWith(final File currentFile)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<? extends AbstractFileRecordReader> constructorWithFile = delegateType.getConstructor(File.class);
        return constructorWithFile.newInstance(currentFile);
    }

}
