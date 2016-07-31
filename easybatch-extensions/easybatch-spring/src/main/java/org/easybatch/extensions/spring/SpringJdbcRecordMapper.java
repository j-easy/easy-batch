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

package org.easybatch.extensions.spring;

import org.easybatch.core.mapper.RecordMapper;
import org.easybatch.core.mapper.RecordMappingException;
import org.easybatch.core.record.GenericRecord;
import org.easybatch.jdbc.JdbcRecord;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A record mapper that uses
 * <a href="http://docs.spring.io/spring/docs/current/spring-framework-reference/html/jdbc.html">Spring JDBC</a>
 * to map result set to domain object.
 *
 * @param <P> Target domain object type
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class SpringJdbcRecordMapper<P> implements RecordMapper<JdbcRecord, GenericRecord<P>> {

    private final Class<P> type;

    /**
     * Create a {@link SpringJdbcRecordMapper}.
     *
     * @param type the type of target object
     */
    public SpringJdbcRecordMapper(Class<P> type) {
        this.type = type;
    }

    @Override
    public GenericRecord<P> processRecord(JdbcRecord record) throws RecordMappingException {
        ResultSet resultSet = record.getPayload();
        BeanPropertyRowMapper<P> beanPropertyRowMapper = new BeanPropertyRowMapper<>(type);
        try {
            return new GenericRecord<>(record.getHeader(), beanPropertyRowMapper.mapRow(resultSet, record.getHeader().getNumber().intValue()));
        } catch (SQLException e) {
            throw new RecordMappingException("Unable to map record " + record + " to target type", e);
        }
    }

}
