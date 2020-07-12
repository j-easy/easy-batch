# Tutorial: Populating a search database

## Description

This tutorial is a batch application that reads tweets from a relational database (master) and index them in a ElasticSearch server:

<div align="center">
    <img src="db-to-db.png" alt="db-to-db" style="width:70%;height:70%;">
</div>

A similar use case is synchronizing data between an operational database and a [reporting database](http://martinfowler.com/bliki/ReportingDatabase.html).

## Run the tutorial

First, make sure you have already downloaded the source code and built the tutorials
as described in the [quick start](https://github.com/j-easy/easy-batch/tree/master/easy-batch-tutorials#quick-start) section.

### From the command line

Open a terminal and run the following command:

```
$>mvn exec:java -PrunElasticSearchTutorial
```

### From Your IDE

* Navigate to the `org.jeasy.batch.tutorials.intermediate.elasticsearch` package
* Run the `org.jeasy.batch.tutorials.intermediate.elasticsearch.Launcher` class without any argument
