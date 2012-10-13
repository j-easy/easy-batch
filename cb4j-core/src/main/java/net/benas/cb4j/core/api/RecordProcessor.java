/*
 * The MIT License
 *
 *  Copyright (c) 2012, benas (md.benhassine@gmail.com)
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */

package net.benas.cb4j.core.api;

/**
 * Interface for record processor.<br/>
 *
 * Implementation of this interface should provide the business logic of record processing.<br/>
 *
 * Implementation of this interface should use the same type T used in {@link RecordMapper} to avoid type casting exception or any type safety warnings
 *
 * In a well designed batch, records used by the implementation should be correctly validated and mapped to domain objects of type T
 *
 * @author benas (md.benhassine@gmail.com)
 */
public interface RecordProcessor<T> {

    /**
     * PreProcess a record : this is the best place to implement any record pre processing logic.
     * @param typedRecord the record to pre process
     */
    void preProcessRecord(T typedRecord);

    /**
     * Process a record.
     * @param typedRecord the record to process.
     */
    void processRecord(T typedRecord);

    /**
     * PostProcess a record : This is the best place to implement any record post processing logic.
     * @param typedRecord the record to post process
     */
    void postProcessRecord(T typedRecord);
}
