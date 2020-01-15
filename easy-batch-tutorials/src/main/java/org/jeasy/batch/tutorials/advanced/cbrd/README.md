# Tutorial: Processing files in parallel based on their content

## Description

This tutorial is a show case of the `ContentBasedBlockingQueueRecordWriter`.
The goal is to process a directory containing multiple files and to dispatch these files to worker jobs based on their content type:

<div align="center">
    <img src="cbrd.png" alt="cbrd">
</div>

Input files are processed in parallel using multiple queues.

## Run the tutorial

### From the command line

Open a terminal and run the following commands:

```
$>cd easy-batch-tutorials
$>mvn install
$>mvn exec:java -PrunFilesParallelProcessingTutorial
```

### From Your IDE

* Import the `easy-batch-tutorials` project in your IDE
* Resolve maven dependencies
* Navigate to the `org.jeasy.batch.tutorials.advanced.cbrd.files` package
* Run the `org.jeasy.batch.tutorials.advanced.cbrd.files.Launcher` class without any argument
