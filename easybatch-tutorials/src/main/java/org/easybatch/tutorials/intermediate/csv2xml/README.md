# Tutorial: transforming data from one format to another

## Description

This tutorial is a typical ETL application that reads data from a CSV file, transform it to XML format and write it to an output file:

<div align="center">
    <img src="csv-to-xml.png" alt="csv-to-xml" style="max-width:70%;">
</div>

Here is the input file called `tweets.csv` :

```
id,user,message
1,foo,easy batch rocks! #EasyBatch
2,bar,@foo I do confirm :-)
3,baz,@foo @bar Is it really easy?
4,toto,@baz yeah! kinda KISS framework
```

We would like to transform these tweets to the following XML format:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<tweets>
    <tweet><id>1</id><message>easy batch rocks! #EasyBatch</message><user>foo</user></tweet>
    <tweet><id>2</id><message>@foo I do confirm :-)</message><user>bar</user></tweet>
    <tweet><id>3</id><message>@foo @bar Is it really easy?</message><user>baZ</user></tweet>
    <tweet><id>4</id><message>@baz yeah! kinda KISS framework</message><user>toto</user></tweet>
</tweets>
```

So let's get started!

## Writing the Tweet bean

Easy Batch encourages using POJOs as a pivot model to transform data from one format to another and provides several implementations of the `RecordMarshaller` interface to marshal Java objects to a variety of output formats (CSV, XML, JSON, etc).

To follow this POJO based approach, let's first create a `Tweet` bean representing a tweet:

```java
@XmlRootElement
public class Tweet {

    private int id;
    private String user;
    private String message;

    // getters and setters omitted
}
```

This class is annotated with `@XmlRootElement` since we will use JAXB to marshal tweets to XML format.
We will see how to do that in a minute.

## Writing the ETL application

The following listing is the application that implements the requirement. We will explain each step in details:

```java
File csvTweets = new File("tweets.csv");
File xmlTweets = new File("tweets.xml");

FileRecordWriter recordWriter = new FileRecordWriter(xmlTweets);
recordWriter.setHeaderCallback(new HeaderWriter());
recordWriter.setFooterCallback(new FooterWriter());

Job job = JobBuilder.aNewJob()
    .reader(new FlatFileRecordReader(csvTweets)) // Step 1
    .filter(new HeaderRecordFilter()) // Step 2
    .mapper(new DelimitedRecordMapper(Tweet.class, "id", "user", "message")) // Step 3
    .processor(new XmlRecordMarshaller(Tweet.class)) // Step 4
    .writer(recordWriter)// Step 5
    .build();

JobExecutor jobExecutor = new JobExecutor();
jobExecutor.execute(job);
jobExecutor.shutdown();
```

What do all these components do? Here are the details:

* Step 1: The `FlatFileRecordReader` reads records form the input file
* Step 2: The header record contains only field names and has no added value to be transformed to XML, so we can filter it with the `HeaderRecordFilter`
* Step 3: To map each delimited record to an instance of the `Tweet` bean, we use the `DelimitedRecordMapper`. This mapper needs to be configured with the target object type and the list of field names.
* Step 4: At this point of the pipeline, we should have an instance of the `Tweet` bean for each record, we can marshal it to XML format using the `XmlRecordMarshaller`
* Step 5: Once tweets are marshaled to XML, we can write them to the output file with a `FileRecordWriter`. Note that we used a custom `HeaderWriter` and `FooterWriter` to add wrapper tags (`<tweets>...</tweets>`) around the file content as well as the XML declaration (`<?xml version="1.0" encoding="UTF-8" standalone="yes"?>`) to the output file.

That's it. Let's run the tutorial and see the result.

## Run the tutorial

### From the command line

Open a terminal and run the following commands:

```
$>cd easybatch-tutorials
$>mvn install
$>mvn exec:java -PrunCsv2xmlTutorial
```

### From Your IDE

* Import the `easybatch-tutorials` project in your IDE
* Resolve maven dependencies
* Navigate to the `org.easybatch.tutorials.intermediate.csv2xml` package
* Run the `org.easybatch.tutorials.intermediate.csv2xml.Launcher` class without any argument
