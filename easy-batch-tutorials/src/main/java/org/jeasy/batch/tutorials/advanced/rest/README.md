# Tutorial: Reading data from a REST endpoint

## Description

Easy Batch does not provide a REST API reader or writer (See [issue 286](https://github.com/j-easy/easy-batch/issues/286))
because REST APIs differs significantly in terms of how they manage pagination, authentication, content type, etc.

That said, it is always possible to get/post data from/to a REST endpoint using the input/output stream of a `java.net.URL`
and use Easy Batch components to read/write data.

This tutorial shows how to use Easy Batch to generate a release notes for an Easy Batch milestone (eating our own dog food :-)).
We will create a job that reads data from a REST endpoint (Github API in this case), transform it and write it to a file.

## Run the tutorial

First, make sure you have already downloaded the source code and built the tutorials
as described in the [quick start](https://github.com/j-easy/easy-batch/tree/master/easy-batch-tutorials#quick-start) section.

Since the job in this tutorial will read data from Github REST API, you need to ensure 
that you are connected to the internet before running the tutorial.

### From the command line

Open a terminal and run the following command:

```
$>mvn exec:java -PrunRestTutorial
```

### From Your IDE

* Navigate to the `org.jeasy.batch.tutorials.advanced.rest` package
* Run the `org.jeasy.batch.tutorials.advanced.rest.Launcher` class without any argument

## Check the result

The job should generate a file named `release-notes.md` with all issues of milestone `v6.0.0`:

```
$>cat target/release-notes.md
```
