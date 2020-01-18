# Tutorial: Importing data from a flat file into a database

## Description

In this tutorial, you will learn how to read tweets from a flat file and store them in a relational database:

<div align="center">
    <img src="file-to-db.png" alt="file-to-db" style="width:70%;height:70%;">
</div>

Here is the `tweets.csv` file:

```
id,user,message
1,foo,easy batch rocks! #EasyBatch
2,bar,@foo I do confirm :-)
3,baz,@foo @bar Is it really easy?
4,toto,@foo @bar @baz yeah! kinda KISS framework
```

So let's get started!

## Reading tweets from a flat file

The `FlatFileRecordReader` is used to read records from a flat file. So let's use this reader:

```java
Job job = new JobBuilder()
    .reader(new FlatFileRecordReader("tweets.csv"))
    .filter(new HeaderRecordFilter())
    .build();
```

We will filter the first record with the `HeaderRecordFilter` since it has no business value and we don't like to save it in the database.

## Mapping tweets to the Tweet bean

Easy Batch development is POJO-centric, you would like to map each tweet to an instance of the `Tweet` bean.
Each field in the text record should be mapped to the corresponding field in the `Tweet` bean.
You can provide object mapping logic by implementing the `RecordMapper` interface or use one of the built-in mappers.

The `DelimitedRecordMapper` is able to map delimited values to a Java bean.
It sounds like this is what you want since you will be reading data from a CSV file.

```java
public class Tweet {

    private int id;

    private String user;

    private String message;

    // Getters, Setters and toString omitted
}
```

Let's register the `DelimitedRecordMapper`:

```java
Job job = new JobBuilder()
    .reader(new FlatFileRecordReader("tweets.csv"))
    .filter(new HeaderRecordFilter())
    .mapper(new DelimitedRecordMapper(Tweet.class, "id", "name", "message"))
    .build();
```

You must specify the type of the target bean and an array of field names in the same order as in the CSV file.
By default, this mapper uses the comma "," as delimiter which is fine in our case.
You can also configure a custom delimiter if you want.

Note that the mapper also converts raw textual data to typed data in the `Tweet` instance.

## Validating tweets

We all know that tweets can have at most 140 characters, and we would like to save only valid tweets in the database.

How to declare this constraint on our `Tweet` bean? The Bean Validation API is the way to go since it provides
 an elegant solution to declare validation constraints on domain objects using annotations.
 Easy Batch provides support for Bean Validation API so you can just annotate your `Tweet` class with constraint annotations:

```java
public class Tweet {

 private int id;

 @NotNull
 private String user;

 @Size(min=0, max=140)
 private String message;

 // Getters, Setters and toString omitted
 }
```

Now that you placed validation constraints on the `Tweet` bean, let's configure a job to validate records according to these constraints:

```java
Job job = new JobBuilder()
    .reader(new FlatFileRecordReader("tweets.csv"))
    .filter(new HeaderRecordFilter())
    .mapper(new DelimitedRecordMapper(Tweet.class, "id", "name", "message"))
    .validator(new BeanValidationRecordValidator<Tweet>())
    .build();
```

The `BeanValidationRecordValidator` is responsible of validating records.
When a record is not valid, the job will reject it and move to the next one.

## Writing tweets to the database

There are 3 record writers to write data in a relational database:

* The `JdbcRecordWriter`: writes records in a database using the JDBC API.
* The `JpaRecordWriter`: writes records in a database using the Java Persistence API.
* The `HibernateRecordWriter`: writes records in a database using Hibernate.

In this tutorial, we will use the `JdbcRecordWriter`.

To keep the tutorial simple, a in-memory database will be used.

Tweets will be inserted in the tweet table:

| field   | type         |
|---------|--------------|
| id      | integer      |
| user    | varchar(32)  |
| message | varchar(140) |

To use the `JdbcRecordWriter`, we need to supply 3 artifacts:

* a JDBC DataSource
* the SQL query to insert data
* and a `PreparedStatementProvider`

First things first, let's create a JDBC connection:

```java
DataSource ds = DatabaseUtil.getDataSource()
```

The `DatabaseUtil` class provides static helper methods to work with the embedded database (not shown here for the sake of simplicity).
Next, we need to provide the SQL query to insert tweets in the `Tweet` table:

```java
String query = "INSERT INTO TWEET values (?, ?, ?)"
```

Finally, we must supply a `PreparedStatementProvider` to provide values to the SQL query.
The `BeanPropertiesPreparedStatementProvider` can be used to provide values based on fields of the domain object, `Tweet` in our case.

That's all for the record writer, let's put all components together.

## Putting it all together

The following listing is the batch application to run the tutorial:

```java
public class Launcher {

    public static void main(String[] args) throws Exception {

        // Load tweets from tweets.csv
        Path tweets = Paths.get("tweets.csv");

        // Start embedded database server
        DatabaseUtil.startEmbeddedDatabase();

        DataSource dataSource = DatabaseUtil.getDataSource();
        String query = "INSERT INTO tweet VALUES (?,?,?);";
        String[] fields = {"id", "user", "message"};
        PreparedStatementProvider psp =
                    new BeanPropertiesPreparedStatementProvider(Tweet.class, fields);

        // Build a batch job
        Job job = JobBuilder.aNewJob()
                    .batchSize(2)
                    .filter(new HeaderRecordFilter())
                    .reader(new FlatFileRecordReader(tweets))
                    .mapper(new DelimitedRecordMapper(Tweet.class, fields))
                    .validator(new BeanValidationRecordValidator())
                    .writer(new JdbcRecordWriter(dataSource, query, psp))
                    .build();

        // Execute the job
        JobExecutor jobExecutor = new JobExecutor();
        JobReport jobReport = jobExecutor.execute(job);
        jobExecutor.shutdown();

        System.out.println(jobReport);

        // Dump tweet table to check inserted data
        DatabaseUtil.dumpTweetTable();

        // Shutdown embedded database server and delete temporary files
        DatabaseUtil.cleanUpWorkingDirectory();
    }
}
```

## Run the tutorial

### From the command line

Open a terminal and run the following commands:

```
$>cd easy-batch-tutorials
$>mvn install
$>mvn exec:java -PrunJdbcImportDataTutorial
```

### From Your IDE

* Import the `easy-batch-tutorials` project in your IDE
* Resolve maven dependencies
* Navigate to the `org.jeasy.batch.tutorials.intermediate.load` package
* Run the `org.jeasy.batch.tutorials.intermediate.load.Launcher` class without any argument
