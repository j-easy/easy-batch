package org.easybatch.core.reader;

import java.io.File;

/**
 * Abstract class for all file readers.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public abstract class AbstractFileRecordReader implements RecordReader {

    protected File file;

    protected AbstractFileRecordReader(File file) {
        this.file = file;
    }

}
