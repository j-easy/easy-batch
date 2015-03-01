# Content-Based record dispatcher Tutorial 2

## Description

This tutorial is a show case of the `ContentBasedRecordDispatcher` to dispatch fruits based on their type.
Fruits are processed in parallel using multiple queues.

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
$>mvn exec:java -PrunFruitsParallelProcessingTutorial
```

### From Your IDE

* Import the `easybatch-tutorials` module in your IDE
* Resolve maven dependencies
* Navigate to the `org.easybatch.tutorials.advanced.cbrd.fruits` package
* Run the `org.easybatch.tutorials.advanced.cbrd.fruits.FruitsParallelProcessingTutorial` class without any argument
