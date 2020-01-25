# Tutorial: Exporting data from a database to a flat file

## Description

This tutorial is the same as the [Data export tutorial](https://github.com/j-easy/easy-batch/tree/master/easy-batch-tutorials/src/main/java/org/jeasy/batch/tutorials/intermediate/extract),
except that the output file must have:

* a header with column names
* a footer with the total number of records

The expected output file is the following:

```
id,user,message
"1","foo","easy batch rocks! #EasyBatch"
"2","bar","@foo I do confirm :-)"
Total records = 2
```

## Run the tutorial

First, make sure you have already downloaded the source code and built the tutorials
as described in the [quick start](https://github.com/j-easy/easy-batch/tree/master/easy-batch-tutorials#quick-start) section.

### From the command line

Open a terminal and run the following command:

```
$>mvn exec:java -PrunJdbcExportDataWithHeaderFooterTutorial
```

### From Your IDE

* Navigate to the `org.jeasy.batch.tutorials.intermediate.headerfooter` package
* Run the `org.jeasy.batch.tutorials.intermediate.headerfooter.Launcher` class without any argument
