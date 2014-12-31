package org.easybatch.tutorials.pipeline.unixLike;

import org.easybatch.core.api.RecordProcessor;

/**
 * A processor that mimics "cut" unix command.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class CutProcessor implements RecordProcessor<String, String> {

    private String delimiter;

    private int fieldNumber;

    public CutProcessor(String delimiter, int fieldNumber) {
        this.delimiter = delimiter;
        this.fieldNumber = fieldNumber;
    }

    @Override
    public String processRecord(String record) throws Exception {
        return record.split(delimiter)[fieldNumber];
    }

}
