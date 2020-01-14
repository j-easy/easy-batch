/**
 * The MIT License
 *
 *   Copyright (c) 2020, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
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
package org.easybatch.xml;

import org.easybatch.core.record.Record;
import org.easybatch.core.validator.RecordValidator;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.util.JAXBSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;

/**
 * This class validates the payload of records against an xml schema.
 *
 * @param <P> the type of record's payload
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class XmlRecordValidator<P> implements RecordValidator<Record<P>> {

    private Validator validator;

    /**
     * Create a new xml record validator.
     *
     * @param xsd schema file against which records will be validated
     * @throws SAXException if the schema cannot be parsed
     */
    public XmlRecordValidator(final File xsd) throws SAXException {
        this(xsd, new NoOpErrorHandler());
    }

    /**
     * Create a new xml record validator.
     *
     * @param xsd          schema file against which records will be validated
     * @param errorHandler to invoke when a validation error occurs
     * @throws SAXException if the schema cannot be parsed
     */
    public XmlRecordValidator(final File xsd, final ErrorHandler errorHandler) throws SAXException {
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = schemaFactory.newSchema(xsd);
        validator = schema.newValidator();
        validator.setErrorHandler(errorHandler);
    }

    @Override
    public Record<P> processRecord(Record<P> record) throws Exception {
        P payload = record.getPayload();
        JAXBContext context = JAXBContext.newInstance(payload.getClass());
        JAXBSource source = new JAXBSource(context, payload);
        validator.validate(source);
        return record;
    }

    private static class NoOpErrorHandler implements ErrorHandler {

        @Override
        public void warning(SAXParseException exception) throws SAXException {
            throw exception;
        }

        @Override
        public void error(SAXParseException exception) throws SAXException {
            throw exception;
        }

        @Override
        public void fatalError(SAXParseException exception) throws SAXException {
            throw exception;
        }
    }
}
