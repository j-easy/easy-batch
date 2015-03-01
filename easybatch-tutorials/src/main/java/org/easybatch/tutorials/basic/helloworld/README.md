# Hello World Tutorial

## Description

This tutorial is a simple batch application that reads tweets from a String and prints them out to the standard output.

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
$>mvn exec:java -PrunHelloWorldTutorial
```

### From Your IDE

* Import the `easybatch-tutorials` module in your IDE
* Resolve maven dependencies
* Navigate to the `org.easybatch.tutorials.basic.helloworld` package
* Run the `org.easybatch.tutorials.basic.helloworld.Launcher` class without any argument
