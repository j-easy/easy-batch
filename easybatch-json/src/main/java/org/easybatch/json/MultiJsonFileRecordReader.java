package org.easybatch.json;

import org.easybatch.core.reader.AbstractFileRecordReader;
import org.easybatch.core.reader.AbstractMultiFileRecordReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * Reader for multiple json files in one shot.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class MultiJsonFileRecordReader extends AbstractMultiFileRecordReader {

    /**
     * Create a new {@link MultiJsonFileRecordReader}.
     *
     * @param files to read
     */
    public MultiJsonFileRecordReader(List<File> files) {
        super(files);
    }

    @Override
    protected AbstractFileRecordReader createReader() throws FileNotFoundException {
        return new JsonFileRecordReader(currentFile);
    }
}
