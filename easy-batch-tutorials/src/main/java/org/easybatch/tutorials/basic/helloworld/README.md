# Tutorial: Hello World

## Description

In this tutorial, you will learn how to build a very simple batch application to process a flat file.
We will read text records containing tweets and print them out to the console. Here are the `tweets.csv` file:

```
id,user,message
1,foo,easy batch rocks! #EasyBatch
2,bar,@foo I do confirm :-)
3,baz,@foo @bar Is it really easy?
4,toto,@foo @bar @baz yeah! kinda KISS framework
```

Each record is composed of:

* The tweet id
* The user name
* The tweet content
* The header record contains field names.

Let's get started!

## Reading tweets from the data source

To read records from a flat file, you can use the `FlatFileRecordReader`.
This reader will read the file line by line and produce `StringRecord` instances which, as you may have guessed,
 contain strings as payload, or tweets in our case.

Let's configure a job named `Hello world job` to use this reader:

```java
File dataSource = new File("tweets.csv");

Job job = JobBuilder.aNewJob()
    .named("Hello world job")
    .reader(new FlatFileRecordReader(dataSource))
    .build();
```

## Writing tweets to the console

To write records to the standard output, you can use the `StandardOutputRecordWriter`:

```java
File dataSource = new File("tweets.csv");

Job job = JobBuilder.aNewJob()
    .named("Hello world job")
    .reader(new FlatFileRecordReader(dataSource))
    .writer(new StandardOutputRecordWriter())
    .build();
```

## Putting it all together

To launch the tutorial, we will use the following class:

```java
public class Launcher {

    public static void main(String[] args) {

        // Create the data source
        File dataSource = new File("tweets.csv");

        // Build a batch job
        Job job = new JobBuilder()
            .named("hello world job")
            .reader(new FlatFileRecordReader(dataSource))
            .writer(new StandardOutputRecordWriter())
            .build();

        // Execute the job
        JobExecutor jobExecutor = new JobExecutor();
        JobReport report = jobExecutor.execute(job);
        jobExecutor.shutdown();

        // Print the job execution report
        System.out.println(report);
    }
}
```

To execute the job, you can use the `JobExecutor` class.

Easy Batch returns a report at the end of execution containing various metrics and statistics.
In this example, we print its content to the console to examine it.
Time to run the tutorial and see the result.

## Run the tutorial

### From the command line

Open a terminal and run the following commands:

```
$>cd easybatch-tutorials
$>mvn install
$>mvn exec:java -PrunHelloWorldTutorial
```

### From Your IDE

* Import the `easybatch-tutorials` project in your IDE
* Resolve maven dependencies
* Navigate to the `org.easybatch.tutorials.basic.helloworld` package
* Run the `org.easybatch.tutorials.basic.helloworld.Launcher` class without any argument
