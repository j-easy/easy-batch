# Benchmark description

This benchmark uses [JMH](http://openjdk.java.net/projects/code-tools/jmh/) to measure the potential overhead of Easy Batch.

The goal of the benchmark is to measure Easy Batch performance for reading, parsing and mapping data to domain objects.
Record processing logic is not taken into account since it heavily depends on the use case.

Two benchmarks will be performed:

* Process a CSV flat file containing customer data
* Process an XML file containing customer data

Data will be generated randomly using [jPopulator](https://github.com/benas/jPopulator/wiki).

It will be possible to specify the number of input records.

The input Csv file has the following format:

```
#id,firstName,lastName,birthDate,email,phone,street,zipCode,city,country
1,foo,bar,1990-12-13,foo@bar.org,0123456789,street1,zipCode1,london,uk
2,bar,foo,1990-10-12,bar@foo.org,9876543210,street2,zipCode2,newyork,usa
```

The input XML file has the following format:

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<customers>
    <customer>
        <id>1</id>
        <firstName>foo</firstName>
        <lastName>bar</lastName>
        <birthDate>1990-12-13</birthDate>
        <email>foo@bar.org</email>
        <phone>0123456789</phone>
        <street>street1</street>
        <zipCode>zipCode1</zipCode>
        <city>london</city>
        <country>uk</country>
    </customer>
<customers>
```

The domain object is the following:

```java
@XmlRootElement
public class Customer {

    private int id;
    private String firstName;
    private String lastName;
    private Date birthDate;
    private String email;
    private String phone;
    private String street;
    private String zipCode;
    private String city;
    private String country;

    //Getters and setters omitted

}
```

# Run the CSV benchmark

```
$>cd easybatch-bench
$>cd mvn clean install
$>cd target
$>java -Dorg.easybatch.bench.count=10000 -cp "easybatch-bench-3.0.0.jar:dependency/*" org.easybatch.bench.CsvBenchmark
```

# Run the XML benchmark

```
$>cd easybatch-bench
$>cd mvn clean install
$>cd target
$>java -Dorg.easybatch.bench.count=10000 -cp "easybatch-bench-3.0.0.jar:dependency/*" org.easybatch.bench.XmlBenchmark
```

# Notes

If you are using MS Windows, please use the `;` classpath separator in the previous commands as follows:

`-cp "easybatch-bench-3.0.0.jar;dependency/*"`

Use the JVM property `-Dorg.easybatch.bench.count` to specify the number of records to generate.

By default, the directory `java.io.tmpdir` is used to generate the input files (which will be deleted at the end of benchmark execution).
 You can change this default directory by setting the JVM property `-Djava.io.tmpdir=path_to_tmp_directory`.
