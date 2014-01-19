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

import io.github.benas.easybatch.core.api.AbstractRecordProcessor;
import io.github.benas.easybatch.tutorials.customers.model.Customer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

/**
 * A customer processor implementation that generates customer XML data using JAXB.
 *
 * @author benas (md.benhassine@gmail.com)
 */
public class CustomerProcessor extends AbstractRecordProcessor<Customer> {

    /**
     * The Jaxb marshaller to write customer xml data.
     */
    Marshaller jaxbMarshaller;

    /**
     * Create a CustomerProcessor.
     * @throws Exception if a problem occurs during JAXB marshaller initialization
     */
    public CustomerProcessor() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Customer.class);
        jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    }

    @Override
    public void processRecord(final Customer customer) throws Exception {
        try {
            jaxbMarshaller.marshal(customer, System.out);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

}
