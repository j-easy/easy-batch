# Tutorial: Job scheduling

## Description

This tutorial is an application that schedules a batch job to run repeatedly every 10 seconds.
The goal is to show how to use Quartz to schedule Easy Batch jobs.

## Run the tutorial

### From the command line

Open a terminal and run the following commands:

```
$>cd easybatch-tutorials
$>mvn install
$>mvn exec:java -PrunQuartzTutorial
```

If everything is ok, you will see in the console that the job will run every 10 seconds.

### From Your IDE

* Import the `easybatch-tutorials` project in your IDE
* Resolve maven dependencies
* Navigate to the `org.easybatch.tutorials.advanced.quartz` package
* Run the `org.easybatch.tutorials.advanced.quartz.Launcher` class without any argument

If everything is ok, you will see in the console that the job will run every 10 seconds.
