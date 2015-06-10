/*
 * The MIT License
 *
 *  Copyright (c) 2015, Mahmoud Ben Hassine (mahmoud@benhassine.fr)
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

package org.easybatch.integration.gson;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.easybatch.core.api.Record;
import org.easybatch.core.api.RecordMapper;
import org.easybatch.core.api.RecordMappingException;
import org.easybatch.json.JsonRecord;

/**
 * Mapper that uses <a href="https://code.google.com/p/google-gson/">Google Gson</a>
 * to map json records to domain objects.
 *
 * @param <T> Target domain object class.
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class GsonRecordMapper<T> implements RecordMapper<T> {

    private Gson mapper;

    private Class<T> type;

    public GsonRecordMapper(Gson mapper, Class<T> type) {
        this.mapper = mapper;
        this.type = type;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public T mapRecord(Record record) throws RecordMappingException {
        JsonRecord jsonRecord = (JsonRecord) record;
        try {
            return mapper.fromJson(jsonRecord.getPayload(), type);
        } catch (JsonSyntaxException e) {
            throw new RecordMappingException("Unable to map record " + record + " to target type", e);
        }
    }

}
