package org.easybatch.core.api.event.record;


import org.easybatch.core.api.ValidationError;

import java.util.Set;

public interface RecordValidatorEventListener extends RecordProcessEventListener {

    public void beforeValidateRecord(Object mappedRecord);

    public void afterValidateRecord(Object validatedRecord, Set<ValidationError> validationErrors);
}
