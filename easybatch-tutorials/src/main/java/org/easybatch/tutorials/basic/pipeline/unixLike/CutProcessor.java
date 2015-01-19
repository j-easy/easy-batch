package org.easybatch.tutorials.basic.pipeline.unixLike;

import org.easybatch.core.api.RecordProcessor;
import org.easybatch.core.record.StringRecord;

/**
 * A processor that mimics "cut" unix command.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class CutProcessor implements RecordProcessor<StringRecord, String> {

    private String delimiter;

    private int fieldNumber;

    public CutProcessor(String delimiter, int fieldNumber) {
        this.delimiter = delimiter;
        this.fieldNumber = fieldNumber;
    }

    @Override
    public String processRecord(StringRecord record) throws Exception {
        return record.getPayload().split(delimiter)[fieldNumber];
    }

}
