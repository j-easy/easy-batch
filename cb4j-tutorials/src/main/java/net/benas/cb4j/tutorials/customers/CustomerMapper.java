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

package net.benas.cb4j.tutorials.customers;

import net.benas.cb4j.core.api.RecordMapper;
import net.benas.cb4j.core.api.RecordMappingException;
import net.benas.cb4j.core.converter.TypeConversionException;
import net.benas.cb4j.core.converter.TypeConverter;
import net.benas.cb4j.core.model.Record;

import java.util.Date;

/**
 * A mapper implementation to map a CSV record to a customer bean
 * @author benas (md.benhassine@gmail.com)
 */
public class CustomerMapper implements RecordMapper<Customer> {

    public Customer mapRecord(Record record) throws RecordMappingException {

        Customer customer = new Customer();

        customer.setFirstName(record.getFieldContentByIndex(0));
        customer.setLastName(record.getFieldContentByIndex(1));
        customer.setEmail(record.getFieldContentByIndex(2));

        //convert string date to java.util.Date object
        Date birthday;
        try {
            birthday = TypeConverter.getDateTypedFieldContent(record.getFieldContentByIndex(3), "dd/MM/yyyy");
        } catch (TypeConversionException e) {
            throw new RecordMappingException(e.getMessage(),e);
        }
        customer.setBirthday(birthday);

        customer.setPhone(record.getFieldContentByIndex(4));

        //custom mapping rule for transforming encoded data into a business value
        String gender = record.getFieldContentByIndex(5);
        customer.setGender( gender.equals("0") ? Gender.MALE : Gender.FEMALE );

        //encapsulate address data into a business address object
        Address address = new Address();
        address.setStreet(record.getFieldContentByIndex(6));
        address.setZipCode(record.getFieldContentByIndex(7));
        address.setCity(record.getFieldContentByIndex(8));
        address.setCountry(record.getFieldContentByIndex(9));

        customer.setAddress(address);

        return customer;
    }
}
