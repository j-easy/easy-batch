package org.easybatch.extensions.yaml;

import org.easybatch.core.reader.AbstractFileRecordReader;
import org.easybatch.core.reader.AbstractMultiFileRecordReader;

import java.io.File;
import java.util.List;

/**
 * Reader for multiple yaml files in one shot.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class MultiYamlFileRecordReader extends AbstractMultiFileRecordReader {

    /**
     * Create a new {@link MultiYamlFileRecordReader}.
     *
     * @param files to read
     */
    public MultiYamlFileRecordReader(List<File> files) {
        super(files);
    }

    @Override
    protected AbstractFileRecordReader createReader() throws Exception {
        return new YamlFileRecordReader(currentFile);
    }
}
