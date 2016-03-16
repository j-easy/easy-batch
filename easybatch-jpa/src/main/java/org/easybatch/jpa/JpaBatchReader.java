/*
 *  The MIT License
 *
 *   Copyright (c) 2016, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in
 *   all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *   THE SOFTWARE.
 */

package org.easybatch.jpa;

import org.easybatch.core.reader.AbstractBatchReader;

import javax.persistence.EntityManagerFactory;

/**
 * Reads records in batches using the Java Persistence API.
 *
 * @param <T> the type of objects this reader will read.
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class JpaBatchReader<T> extends AbstractBatchReader {

    /**
     * Reads records in batches using the Java Persistence API.
     *
     * @param batchSize            the batch size
     * @param entityManagerFactory the entity manager factory
     * @param query                the JPQL query to fetch data
     * @param type                 the target type
     */
    public JpaBatchReader(final int batchSize, final EntityManagerFactory entityManagerFactory, final String query, final Class<T> type) {
        super(batchSize, new JpaRecordReader<>(entityManagerFactory, query, type));
    }

}
