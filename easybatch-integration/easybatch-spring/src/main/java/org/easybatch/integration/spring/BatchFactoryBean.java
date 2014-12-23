package org.easybatch.integration.spring;

import org.easybatch.core.api.*;
import org.easybatch.core.impl.Engine;
import org.easybatch.core.impl.EngineBuilder;
import org.springframework.beans.factory.FactoryBean;

/**
 * Spring Factory Bean that creates batch instances.
 *
 * @author Mahmoud Ben Hassine (md.benhassine@gmail.com)
 */
public class BatchFactoryBean implements FactoryBean {

    private RecordReader recordReader;

    private RecordFilter recordFilter;

    private RecordMapper recordMapper;

    private RecordValidator recordValidator;

    private RecordProcessor recordProcessor;

    @Override
    public Object getObject() throws Exception {
        EngineBuilder engineBuilder = new EngineBuilder();
        if (recordReader != null) {
            engineBuilder.registerRecordReader(recordReader);
        }
        if (recordFilter != null) {
            engineBuilder.registerRecordFilter(recordFilter);
        }
        if (recordMapper != null) {
            engineBuilder.registerRecordMapper(recordMapper);
        }
        if (recordValidator != null) {
            engineBuilder.registerRecordValidator(recordValidator);
        }
        if (recordProcessor != null) {
            engineBuilder.registerRecordProcessor(recordProcessor);
        }
        return engineBuilder.build();
    }

    @Override
    public Class<?> getObjectType() {
        return Engine.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    /* Setters for dependency injection */

    public void setRecordReader(RecordReader recordReader) {
        this.recordReader = recordReader;
    }

    public void setRecordFilter(RecordFilter recordFilter) {
        this.recordFilter = recordFilter;
    }

    public void setRecordMapper(RecordMapper recordMapper) {
        this.recordMapper = recordMapper;
    }

    public void setRecordValidator(RecordValidator recordValidator) {
        this.recordValidator = recordValidator;
    }

    public void setRecordProcessor(RecordProcessor recordProcessor) {
        this.recordProcessor = recordProcessor;
    }


}
