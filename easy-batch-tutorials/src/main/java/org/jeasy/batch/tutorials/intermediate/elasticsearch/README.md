# Tutorial: Populating a search database

## Description

This tutorial is a batch application that reads tweets from a relational database (master) and index them in a ElasticSearch server:

<div align="center">
    <img src="db-to-db.png" alt="db-to-db" style="width:70%;height:70%;">
</div>

A similar use case is synchronizing data between an operational database and a [reporting database](http://martinfowler.com/bliki/ReportingDatabase.html).

## Run the tutorial

### From the command line

Open a terminal an run the following command:

```
$>cd easy-batch-tutorials
$>mvn install
$>mvn exec:java -PrunElasticSearchTutorial
```

### From Your IDE

* Import the `easy-batch-tutorials` project in your IDE
* Resolve maven dependencies
* Navigate to the `org.jeasy.batch.tutorials.intermediate.elasticsearch` package
* Run the `org.jeasy.batch.tutorials.intermediate.elasticsearch.Launcher` class without any argument
