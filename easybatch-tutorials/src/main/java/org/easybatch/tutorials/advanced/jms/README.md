# Asynchronous jobs Tutorial

## Description

This tutorial is a show case of how to implement asynchronous jobs using an Easy Batch engine and a JMS queue.

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
$>mvn exec:java -PrunJmsTutorial
```

This will start an embedded JMS broker listening to incoming messages.

Open a second terminal and run the following command:

`mvn exec:java -PrunJmsSender`

You will be able to type in tweets in the console to post them to the JMS queue and see how the engine will process them as they come.

### From Your IDE

* Import the `easybatch-tutorials` module in your IDE
* Resolve maven dependencies
* Navigate to the `org.easybatch.tutorials.advanced.jms` package
* Run the `org.easybatch.tutorials.advanced.jms.Launcher` class without any argument to launch the engine
* Run the `org.easybatch.tutorials.advanced.jms.JMSSenderLauncher` class without any argument to launch the JMS message sender application
