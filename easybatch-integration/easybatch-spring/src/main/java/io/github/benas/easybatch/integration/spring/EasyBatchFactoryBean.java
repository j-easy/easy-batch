package io.github.benas.easybatch.integration.spring;

import io.github.benas.easybatch.core.api.*;
import io.github.benas.easybatch.core.impl.EasyBatchEngine;
import io.github.benas.easybatch.core.impl.EasyBatchEngineBuilder;
import org.springframework.beans.factory.FactoryBean;

/**
 * Spring Factory Bean that creates Easy Batch instances.
 *
 * @author benas (md.benhassine@gmail.com)
 */
public class EasyBatchFactoryBean implements FactoryBean {

    private RecordReader recordReader;

    private RecordFilter recordFilter;

    private RecordMapper recordMapper;

    private RecordValidator recordValidator;

    private RecordProcessor recordProcessor;

    @Override
    public Object getObject() throws Exception {
        EasyBatchEngineBuilder easyBatchEngineBuilder = new EasyBatchEngineBuilder();
        if (recordReader != null) {
            easyBatchEngineBuilder.registerRecordReader(recordReader);
        }
        if (recordFilter != null) {
            easyBatchEngineBuilder.registerRecordFilter(recordFilter);
        }
        if (recordMapper != null) {
            easyBatchEngineBuilder.registerRecordMapper(recordMapper);
        }
        if (recordValidator != null) {
            easyBatchEngineBuilder.registerRecordValidator(recordValidator);
        }
        if (recordProcessor != null) {
            easyBatchEngineBuilder.registerRecordProcessor(recordProcessor);
        }
        return easyBatchEngineBuilder.build();
    }

    @Override
    public Class<?> getObjectType() {
        return EasyBatchEngine.class;
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
