/*
 *  The MIT License
 *
 *   Copyright (c) 2015, Mahmoud Ben Hassine (mahmoud@benhassine.fr)
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

package org.easybatch.integration.spring;

import org.easybatch.core.api.RecordMapper;
import org.easybatch.core.api.RecordMappingException;
import org.easybatch.jdbc.JdbcRecord;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A record mapper that uses
 * <a href="http://docs.spring.io/spring/docs/current/spring-framework-reference/html/jdbc.html">Spring JDBC</a>
 * to map result set to domain object.
 *
 * @param <T> Target domain object type
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class SpringJdbcRecordMapper<T> implements RecordMapper<JdbcRecord, T> {

    private final Class<T> type;

    public SpringJdbcRecordMapper(Class<T> type) {
        this.type = type;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public T processRecord(JdbcRecord record) throws RecordMappingException {
        ResultSet resultSet = record.getPayload();
        BeanPropertyRowMapper beanPropertyRowMapper = new BeanPropertyRowMapper(type);
        try {
            return (T) beanPropertyRowMapper.mapRow(resultSet, record.getHeader().getNumber().intValue());
        } catch (SQLException e) {
            throw new RecordMappingException("Unable to map record " + record + " to target type", e);
        }
    }

}
