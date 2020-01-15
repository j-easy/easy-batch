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

package org.jeasy.batch.bench;

import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import io.github.benas.randombeans.api.Randomizer;
import io.github.benas.randombeans.randomizers.*;
import io.github.benas.randombeans.randomizers.range.DateRangeRandomizer;
import org.jeasy.batch.core.job.Job;
import org.jeasy.batch.core.job.JobBuilder;
import org.jeasy.batch.flatfile.DelimitedRecordMapper;
import org.jeasy.batch.flatfile.FlatFileRecordReader;
import org.jeasy.batch.xml.XmlRecordMapper;
import org.jeasy.batch.xml.XmlRecordReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Random;

import static io.github.benas.randombeans.FieldDefinitionBuilder.field;

/**
 * Utility class used to generate customer data and build a batch job.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class BenchmarkUtil {

    public static EnhancedRandom enhancedRandom;

    public static Marshaller customerMarshaller;

    static {
        enhancedRandom = buildEnhancedRandom();
        customerMarshaller = buildCustomerMarshaller();
    }

    public static Customer generateCustomer() {
        return enhancedRandom.nextObject(Customer.class);
    }

    public static void generateCsvCustomers(String customersFile, int customersCount) throws Exception {
        FileWriter fileWriter = new FileWriter(customersFile);
        for (int i = 0; i < customersCount; i++) {
            Customer customer = BenchmarkUtil.generateCustomer();
            fileWriter.write(BenchmarkUtil.toCsv(customer) + "\n");
            fileWriter.flush();
        }
        fileWriter.close();
    }

    public static void generateXmlCustomers(String customersFile, int customersCount) throws Exception {
        FileWriter fileWriter = new FileWriter(customersFile);
        fileWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
        fileWriter.write("<customers>\n");
        fileWriter.flush();
        for (int i = 0; i < customersCount; i++) {
            Customer customer = BenchmarkUtil.generateCustomer();
            fileWriter.write(BenchmarkUtil.toXml(customer) + "\n");
            fileWriter.flush();
        }
        fileWriter.write("</customers>");
        fileWriter.flush();
        fileWriter.close();
    }

    public static Job buildCsvJob(String customersFile) throws Exception {
        return new JobBuilder()
                .reader(new FlatFileRecordReader(new File(customersFile)))
                .mapper(new DelimitedRecordMapper(Customer.class,
                        "id", "firstName", "lastName", "birthDate", "email", "phone", "street", "zipCode", "city", "country"))
                .build();
    }

    public static Job buildXmlJob(String customersFile) throws Exception {
        return new JobBuilder()
                .reader(new XmlRecordReader("customer", new FileInputStream(customersFile)))
                .mapper(new XmlRecordMapper<>(Customer.class))
                .build();
    }

    public static Marshaller buildCustomerMarshaller() {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Customer.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
            return jaxbMarshaller;
        } catch (JAXBException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static EnhancedRandom buildEnhancedRandom() {
        Date today = new Date();
        Date nextYear = new Date();nextYear.setYear(today.getYear() + 1);
        return EnhancedRandomBuilder.aNewEnhancedRandomBuilder()
                .randomize(field().named("id").ofType(Integer.TYPE).inClass(Customer.class).get(), new Randomizer() {
                    Random random = new Random();
                    @Override
                    public Integer getRandomValue() {
                        return Math.abs(random.nextInt(1000000));
                    }
                })
                .randomize(field().named("firstName").ofType(String.class).inClass(Customer.class).get(), new FirstNameRandomizer())
                .randomize(field().named("lastName").ofType(String.class).inClass(Customer.class).get(), new LastNameRandomizer())
                .randomize(field().named("birthDate").ofType(Date.class).inClass(Customer.class).get(), new DateRangeRandomizer(today, nextYear))
                .randomize(field().named("email").ofType(String.class).inClass(Customer.class).get(), new EmailRandomizer())
                .randomize(field().named("phone").ofType(String.class).inClass(Customer.class).get(), new GenericStringRandomizer(
                        new String[]{"0102030405","0607080910","0504030201","0610090807"}))
                .randomize(field().named("street").ofType(String.class).inClass(Customer.class).get(), new StreetRandomizer())
                .randomize(field().named("zipCode").ofType(String.class).inClass(Customer.class).get(), new GenericStringRandomizer(
                        new String[]{"54321", "12345", "98765", "56789"}))
                .randomize(field().named("city").ofType(String.class).inClass(Customer.class).get(), new GenericStringRandomizer(new String[]{"Paris", "Brussels", "London" }))
                .randomize(field().named("country").ofType(String.class).inClass(Customer.class).get(), new GenericStringRandomizer(new String[]{"France", "Belgium", "England" }))
                .build();
    }

    public static String toCsv(Customer customer) {
        return MessageFormat.format("{0},{1},{2},{3,date,yyyy-MM-dd},{4},{5},{6},{7},{8},{9}",
                String.valueOf(customer.getId()), customer.getFirstName(), customer.getLastName(), customer.getBirthDate(),
                customer.getEmail(), customer.getPhone(), customer.getStreet(), customer.getZipCode(), customer.getCity(), customer.getCountry());
    }

    public static String toXml(Customer customer) throws Exception {
        StringWriter stringWriter = new StringWriter();
        customerMarshaller.marshal(customer, stringWriter);
        return stringWriter.toString();
    }

}
