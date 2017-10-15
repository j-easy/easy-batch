# Tutorial: Contributing custom metrics

## Description

The goal of this tutorial is to write a listener that contributes a custom metric to job report.
We would like to add a metric for the processing time average of all records.

We will write a class that implements:

* The `PipelineListener` to gather record processing time and calculate the average
* The `JobListener` to get access to the job report and add the custom metric

## Run the tutorial

### From the command line

Open a terminal and run the following commands:

```shell
$>cd easybatch-tutorials
$>mvn install
$>mvn exec:java -PrunCustomMetricTutorial
```

### From Your IDE

* Import the `easybatch-tutorials` project in your IDE
* Resolve maven dependencies
* Navigate to the `org.easybatch.tutorials.advanced.metric` package
* Run the `org.easybatch.tutorials.advanced.metric.Launcher` class without any argument

## Sample output

Additional metrics are shown after core metrics in the job report. In this tutorial, it is the `Record processing time average` metric :

```shell
Job Report:
===========
Name: job
Status: COMPLETED
Parameters:
	Batch size = 100
	Error threshold = N/A
	Jmx monitoring = false
Metrics:
	Start time = 2016-10-19 01:57:50
	End time = 2016-10-19 01:57:51
	Duration = 0hr 0min 0sec 305ms
	Read count = 2
	Write count = 2
	Filter count = 0
	Error count = 0
	Record processing time average (in ms) = 147.5
```