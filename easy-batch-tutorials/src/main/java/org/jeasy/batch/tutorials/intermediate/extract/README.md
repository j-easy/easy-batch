# Tutorial: Exporting data from a database to a flat file

## Description

In this tutorial, you will see how to export tweets data from a relational database to a flat file:

<div align="center">
    <img src="db-to-file.png" alt="db-to-file" style="width:70%;height:70%;">
</div>

Let's get started!

## Reading tweets from the database

There are 3 record readers to read data from a relational database:

* The `JdbcRecordReader`: reads records from a database using the JDBC API.
* The `JpaRecordReader`: reads records from a database using the Java Persistence API.
* The `HibernateRecordReader`: reads records from a database using Hibernate.

In this tutorial, we will use the `JdbcRecordReader`:

```java
DataSource dataSource = DatabaseUtil.getDataSource();
Job job = JobBuilder.aNewJob()
    .reader(new JdbcRecordReader(dataSource, "select * from tweet"))
    .build();
```

To use the `JdbcRecordReader`, we need to provide a JDBC data source and the query to fetch data.

The `DatabaseUtil` class provides static helper methods to work with the embedded database (not shown here for the sake of simplicity).

## Mapping records to instances of the Tweet bean

The `JdbcRecordReader` create instances of `JdbcRecord` which have a `java.sql.ResultSet` as payload.

Easy Batch development is POJO-based, we would like to map Jdbc records to instances of the `Tweet` POJO and avoid dealing with the low level `java.sql.ResultSet` API:

```java
public class Tweet {
    private int id;
    private String user;
    private String message;
    // Getters, Setters and toString omitted
}
```

The `JdbcRecordMapper` can map Jdbc records to instances of the domain object `Tweet`:

```java
DataSource dataSource = DatabaseUtil.getDataSource();
Job job = JobBuilder.aNewJob()
    .reader(new JdbcRecordReader(dataSource, "select * from tweet"))
    .mapper(new JdbcRecordMapper(Tweet.class, "id", "user", "message"))
    .build();
```

You must specify the type of the target bean and an array of field names in the same order as in the database table.
Note that the mapper also converts raw data in the `java.sql.ResultSet` to typed data in the `Tweet` instance.

## Marshalling tweets to CSV format

The `DelimitedRecordMarshaller` is designed to marshal a Java object to CSV format:

```java
DataSource dataSource = DatabaseUtil.getDataSource();
Job job = JobBuilder.aNewJob()
    .reader(new JdbcRecordReader(dataSource, "select * from tweet"))
    .mapper(new JdbcRecordMapper(Tweet.class, "id", "user", "message"))
    .marshaller(new DelimitedRecordMarshaller(Tweet.class, "id", "user", "message"))
    .build();
```

By default, the `DelimitedRecordMarshaller` uses "," as delimiter and " as field qualifier.

## Writing tweets to an output file

Now that we read records from the database, mapped them to domain objects and marshalled those objects to the desired format,
 we can use the `FileRecordWriter` to write records to a file:

```java
DataSource dataSource = DatabaseUtil.getDataSource();
Job job = JobBuilder.aNewJob()
    .reader(new JdbcRecordReader(dataSource, "select * from tweet"))
    .mapper(new JdbcRecordMapper(Tweet.class, "id", "user", "message"))
    .marshaller(new DelimitedRecordMarshaller(Tweet.class, "id", "user", "message"))
    .writer(new FileRecordWriter(new FileWriter("tweets.csv"))
    .build();
```

## Putting it all together

The following listing is the complete batch application to run the tutorial:

```java
public class Launcher {

    public static void main(String[] args) throws Exception {

        // Output file
        Path tweets = Paths.get("target/tweets.csv");

        //Start embedded database server
        DatabaseUtil.startEmbeddedDatabase();
        DatabaseUtil.populateTweetTable();

        // get a JDBC data source
        DataSource dataSource = DatabaseUtil.getDataSource();

        // Build a batch job
        String[] fields = {"id", "user", "message"};
        Job job = JobBuilder.aNewJob()
                    .reader(new JdbcRecordReader(dataSource, "select * from tweet"))
                    .mapper(new JdbcRecordMapper(Tweet.class, fields))
                    .marshaller(new DelimitedRecordMarshaller(Tweet.class, fields))
                    .writer(new FileRecordWriter(tweets))
                    .build();

        // Execute the job
        JobExecutor jobExecutor = new JobExecutor();
        JobReport jobReport = jobExecutor.execute(job);
        jobExecutor.shutdown();

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
$>mvn exec:java -PrunJdbcExportDataTutorial
```

### From Your IDE

* Import the `easy-batch-tutorials` project in your IDE
* Resolve maven dependencies
* Navigate to the `org.jeasy.batch.tutorials.intermediate.extract` package
* Run the `org.jeasy.batch.tutorials.intermediate.extract.Launcher` class without any argument
