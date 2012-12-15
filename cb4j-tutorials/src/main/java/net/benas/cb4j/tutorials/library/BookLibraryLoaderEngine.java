package net.benas.cb4j.tutorials.library;

import net.benas.cb4j.core.config.BatchConfiguration;
import net.benas.cb4j.core.impl.DefaultBatchEngineImpl;

/**
 * Book library engine that overrides init/shutdown methods to initialize/close database connection.
 * @author benas (md.benhassine@gmail.com)
 */
public class BookLibraryLoaderEngine extends DefaultBatchEngineImpl {

    public BookLibraryLoaderEngine(BatchConfiguration batchConfiguration) {
        super(batchConfiguration);
    }

    @Override
    public void init() {
        super.init();
        DatabaseUtil.initializeSessionFactory();
    }

    @Override
    public void shutdown() {
        super.shutdown();
        DatabaseUtil.closeSessionFactory();
    }

}
