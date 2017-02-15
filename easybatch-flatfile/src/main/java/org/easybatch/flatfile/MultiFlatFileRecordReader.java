package org.easybatch.flatfile;

import org.easybatch.core.reader.AbstractFileRecordReader;
import org.easybatch.core.reader.AbstractMultiFileRecordReader;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Reader for multiple flat files in one shot.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class MultiFlatFileRecordReader extends AbstractMultiFileRecordReader {

    private String charsetName;

    /**
     * Create a new {@link MultiFlatFileRecordReader}.
     *
     * @param files to read
     */
    public MultiFlatFileRecordReader(final List<File> files) {
        super(files);
        this.charsetName = Charset.defaultCharset().name();
    }

    /**
     * Create a new {@link MultiFlatFileRecordReader}.
     *
     * @param files to read
     * @param charsetName of the files
     */
    public MultiFlatFileRecordReader(final List<File> files, final String charsetName) {
        super(files);
        this.charsetName = charsetName;
    }

    @Override
    protected AbstractFileRecordReader createReader() {
        return new FlatFileRecordReader(currentFile, charsetName);
    }
}
