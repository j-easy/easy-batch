package org.jeasy.batch.tutorials.intermediate.rest;

import org.jeasy.batch.core.processor.RecordProcessor;
import org.jeasy.batch.core.record.Record;
import org.jeasy.batch.core.record.StringRecord;

public class IssueFormatter implements RecordProcessor<Issue, String> {

    @Override
    public StringRecord processRecord(Record<Issue> record) {
        Issue issue = record.getPayload();
        String formattedIssue = String.format("* issue #%s : %s", issue.getNumber(), issue.getTitle());
        return new StringRecord(record.getHeader(), formattedIssue);
    }

}
