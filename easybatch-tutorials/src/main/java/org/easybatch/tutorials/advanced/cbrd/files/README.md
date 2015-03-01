# Content-Based record dispatcher Tutorial 1

## Description

This tutorial is a show case of the `ContentBasedRecordDispatcher`.
The goal is to process a directory containing multiple files and to dispatch these files based on their content type.
Input files are processed in parallel using multiple queues.

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
$>mvn exec:java -PrunFilesParallelProcessingTutorial
```

### From Your IDE

* Import the `easybatch-tutorials` module in your IDE
* Resolve maven dependencies
* Navigate to the `org.easybatch.tutorials.advanced.cbrd.files` package
* Run the `org.easybatch.tutorials.advanced.cbrd.files.FilesParallelProcessingTutorial` class without any argument
