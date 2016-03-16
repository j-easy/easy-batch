/*
 * The MIT License
 *
 *  Copyright (c) 2016, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
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

package org.easybatch.xml;

import org.easybatch.core.mapper.RecordMapper;
import org.easybatch.core.mapper.RecordMappingException;
import org.easybatch.core.record.GenericRecord;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.ByteArrayInputStream;
import java.io.File;

/**
 * A record mapper that maps xml records to domain objects annotated with JAXB annotations.
 *
 * @param <P> the target domain object type
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class XmlRecordMapper<P> implements RecordMapper<XmlRecord, GenericRecord<P>> {

    /**
     * JAXB context.
     */
    private JAXBContext jaxbContext;

    /**
     * The JAXB Unmarshaller used to map xml records to domain objects.
     */
    private Unmarshaller jaxbUnmarshaller;

    /**
     * Create a {@link XmlRecordMapper}. Using this constructor, no validation against an xsd will be applied.
     *
     * @param type the target domain object type.
     * @throws JAXBException thrown if an error occurs during the creation of Jaxb context.
     */
    public XmlRecordMapper(final Class<? extends P> type) throws JAXBException {
        jaxbContext = JAXBContext.newInstance(type);
        jaxbUnmarshaller = jaxbContext.createUnmarshaller();
    }

    /**
     * Create a {@link XmlRecordMapper}.
     *
     * @param type the target domain object type.
     * @param xsd  the xsd file against which xml records will be validated
     * @throws JAXBException thrown if an error occurs during the creation of Jaxb context.
     * @throws SAXException  thrown if an error occurs during the schema parsing.
     */
    public XmlRecordMapper(final Class<? extends P> type, final File xsd) throws JAXBException, SAXException {
        jaxbContext = JAXBContext.newInstance(type);
        jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = schemaFactory.newSchema(xsd);
        jaxbUnmarshaller.setSchema(schema);
    }

    /**
     * Create a {@link XmlRecordMapper} with a pre-configured Unmarshaller (with custom adapter, custom listener, etc).
     *
     * @param unmarshaller the unmarshaller to use
     */
    public XmlRecordMapper(final Unmarshaller unmarshaller) {
        this.jaxbUnmarshaller = unmarshaller;
    }

    @Override
    public GenericRecord<P> processRecord(final XmlRecord record) throws RecordMappingException {
        try {
            P unmarshalledObject = (P) jaxbUnmarshaller.unmarshal(new ByteArrayInputStream(record.getPayload().getBytes()));
            return new GenericRecord<>(record.getHeader(), unmarshalledObject);
        } catch (JAXBException e) {
            throw new RecordMappingException("Unable to map record " + record + " to target type", e);
        }
    }

}
