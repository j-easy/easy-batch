package io.github.benas.easybatch.tutorials.bench;

import io.github.benas.easybatch.core.impl.EasyBatchEngine;
import io.github.benas.easybatch.core.impl.EasyBatchEngineBuilder;
import io.github.benas.easybatch.flatfile.FlatFileRecordReader;
import io.github.benas.easybatch.flatfile.dsv.DsvRecordMapper;
import io.github.benas.easybatch.xml.XmlRecordMapper;
import io.github.benas.easybatch.xml.XmlRecordReader;
import io.github.benas.jpopulator.api.Populator;
import io.github.benas.jpopulator.api.Randomizer;
import io.github.benas.jpopulator.impl.PopulatorBuilder;
import io.github.benas.jpopulator.randomizers.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.FileWriter;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Random;

/**
 * Utility class used to generate customer data and build easy batch instance definition.
 *
 * @author benas (md.benhassine@gmail.com)
 */
public class BenchmarkUtil {

    public static Populator customerPopulator;

    public static Marshaller customerMarshaller;

    static {
        customerPopulator = buildCustomerPopulator();
        customerMarshaller = buildCustomerMarshaller();
    }

    public static Customer generateCustomer() {
        return (Customer) customerPopulator.populateBean(Customer.class);
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

    public static EasyBatchEngine buildCsvEasyBatchEngine(String customersFile) throws Exception {
        return new EasyBatchEngineBuilder()
                .registerRecordReader(new FlatFileRecordReader(customersFile))
                .registerRecordMapper(new DsvRecordMapper<Customer>(Customer.class,
                        new String[]{"id", "firstName", "lastName", "birthDate", "email", "phone", "street", "zipCode", "city", "country"}))
                .build();
    }

    public static EasyBatchEngine buildXmlEasyBatchEngine(String customersFile) throws Exception {
        return new EasyBatchEngineBuilder()
                .registerRecordReader(new XmlRecordReader("customer", customersFile))
                .registerRecordMapper(new XmlRecordMapper<Customer>(Customer.class))
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

    public static Populator buildCustomerPopulator() {
        Date today = new Date();
        Date nextYear = new Date();nextYear.setYear(today.getYear() + 1);
        return new PopulatorBuilder()
                .registerRandomizer(Customer.class, Integer.TYPE, "id", new Randomizer() {
                    Random random = new Random();
                    @Override
                    public Integer getRandomValue() {
                        return Math.abs(random.nextInt(1000000));
                    }
                })
                .registerRandomizer(Customer.class, String.class, "firstName", new FirstNameRandomizer())
                .registerRandomizer(Customer.class, String.class, "lastName", new LastNameRandomizer())
                .registerRandomizer(Customer.class, Date.class, "birthDate", new DateRangeRandomizer(today, nextYear))
                .registerRandomizer(Customer.class, String.class, "email", new EmailRandomizer())
                .registerRandomizer(Customer.class, String.class, "phone", new GenericStringRandomizer(
                        new String[]{"0102030405","0607080910","0504030201","0610090807"}))
                .registerRandomizer(Customer.class, String.class, "street", new StreetRandomizer())
                .registerRandomizer(Customer.class, String.class, "zipCode", new GenericStringRandomizer(
                        new String[]{"54321", "12345", "98765", "56789"}))
                .registerRandomizer(Customer.class, String.class, "city", new CityRandomizer())
                .registerRandomizer(Customer.class, String.class, "country", new CountryRandomizer())
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
