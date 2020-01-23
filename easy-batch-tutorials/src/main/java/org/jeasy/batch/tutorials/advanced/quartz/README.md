# Tutorial: Job scheduling

## Description

This tutorial is an application that schedules a batch job to run repeatedly every 10 seconds.
The goal is to show how to use Quartz to schedule Easy Batch jobs.

## Run the tutorial

First, make sure you have already downloaded the source code and built the tutorials
as described in the [quick start](https://github.com/j-easy/easy-batch/tree/master/easy-batch-tutorials#quick-start) section.

### From the command line

Open a terminal and run the following command:

```
$>mvn exec:java -PrunQuartzTutorial
```

If everything is ok, you will see in the console that the job will run every 10 seconds.

### From Your IDE

* Navigate to the `org.jeasy.batch.tutorials.advanced.quartz` package
* Run the `org.jeasy.batch.tutorials.advanced.quartz.Launcher` class without any argument

If everything is ok, you will see in the console that the job will run every 10 seconds.
