# Parallel jobs Tutorial

## Description

This tutorial is an application that reads tweets from a flat file and process them in parallel.

## Pre-requisite

* JDK 1.6+
* Maven
* Git (optional)
* Your favorite IDE (optional)

## Get source code

### Using git

`git clone https://github.com/benas/easy-batch.git`

### Downloading a zip file

Download the [zip file](https://github.com/benas/easy-batch/archive/easybatch-3.0.0.zip) containing the source code and extract it.

## Run the tutorial

### From the command line

Open a terminal in the directory where you have extracted the source code of the project, then proceed as follows:

```
$>cd easy-batch
$>mvn install
$>cd easybatch-tutorials
$> # Launch the record dispatching tutorial
$>mvn exec:java -PrunParallelTutorialWithRecordDispatching
$> # Launch the data source splitting tutorial
$>mvn exec:java -PrunParallelTutorialWithDataSplitting
$> # Launch the data source filtering tutorial
$>mvn exec:java -PrunParallelTutorialWithDataFiltering
```

### From Your IDE

* Import the `easybatch-tutorials` module in your IDE
* Resolve maven dependencies
* Navigate to the `org.easybatch.tutorials.advanced.parallel` package
* Run the `org.easybatch.tutorials.advanced.parallel.ParallelTutorialWithDataFiltering` class without any argument
* Run the `org.easybatch.tutorials.advanced.parallel.ParallelTutorialWithDataSplitting` class without any argument
* Run the `org.easybatch.tutorials.advanced.parallel.ParallelTutorialWithRecordDispatching` class without any argument
