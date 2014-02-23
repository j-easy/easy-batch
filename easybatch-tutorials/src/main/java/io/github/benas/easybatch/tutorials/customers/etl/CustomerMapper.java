/*
 * The MIT License
 *
 *  Copyright (c) 2014, benas (md.benhassine@gmail.com)
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

package io.github.benas.easybatch.tutorials.customers.etl;

import io.github.benas.easybatch.core.api.Record;
import io.github.benas.easybatch.core.api.RecordMapper;
import io.github.benas.easybatch.jdbc.JdbcRecord;
import io.github.benas.easybatch.tutorials.customers.model.Address;
import io.github.benas.easybatch.tutorials.customers.model.Contact;
import io.github.benas.easybatch.tutorials.customers.model.Customer;

import java.sql.ResultSet;

/**
 * A mapper implementation to map a database records to a Customer beans.
 *
 * @author benas (md.benhassine@gmail.com)
 */
public class CustomerMapper implements RecordMapper<Customer> {

    public Customer mapRecord(final Record record) throws Exception {

        JdbcRecord jdbcRecord = (JdbcRecord) record;
        ResultSet resultSet = jdbcRecord.getRawContent();

        Customer customer = new Customer();
        customer.setId(resultSet.getInt("id"));
        customer.setFirstName(resultSet.getString("firstName"));
        customer.setLastName(resultSet.getString("lastName"));
        customer.setBirthDate(resultSet.getDate("birthDate"));

        Contact contact = new Contact();
        contact.setPhone(resultSet.getString("phone"));
        contact.setEmail(resultSet.getString("email"));

        Address address = new Address();
        address.setStreet(resultSet.getString("street"));
        address.setZipCode(resultSet.getString("zipCode"));
        address.setCity(resultSet.getString("city"));
        address.setCountry(resultSet.getString("country"));

        customer.setContact(contact);
        customer.setAddress(address);

        return customer;
    }

}
