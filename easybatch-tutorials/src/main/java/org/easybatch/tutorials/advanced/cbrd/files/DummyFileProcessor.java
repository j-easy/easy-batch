package org.easybatch.tutorials.advanced.cbrd.files;

import org.easybatch.core.api.RecordProcessor;
import org.easybatch.core.record.FileRecord;

/**
 * Dummy file processor.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class DummyFileProcessor implements RecordProcessor<FileRecord, FileRecord> {

    @Override
    public FileRecord processRecord(FileRecord record) throws Exception {
        System.out.println("processed file = " + record.getPayload().getAbsolutePath());
        return record;
    }

}
