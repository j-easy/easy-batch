# Distributed jobs Tutorial

## Description

This tutorial is a show case of how to implement a custom `RecordDispatcher` in order to dispatch records to
 a set of distributed worker engines.

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

This will launch an embedded broker with a JMS queue to which records will be dispatched and processed by a worker engine.

Now, you should launch the REST endpoint serving as record dispatcher. Open a separate terminal and run the following command:

`mvn exec:java -PrunDistributedTutorial`

The REST endpoint should be listening to incoming requests on `http://localhost:8000/api/orders`:

```
Record dispatcher started.
Listening for incoming records on http://localhost:8000/api/orders
Hit enter to stop the application...
```

You can send arbitrary requests to http://localhost:8000/api/orders using curl -XPUT http://localhost:8000/api/orders -d '{orderData}' for example and see that the worker engine has processed the record.

### From Your IDE

* Import the `easybatch-tutorials` module in your IDE
* Resolve maven dependencies
* Navigate to the `org.easybatch.tutorials.advanced.jms` package
* Run the `org.easybatch.tutorials.advanced.jms.Launcher` class without any argument to launch the engine.
* Navigate to the `org.easybatch.tutorials.advanced.distributed` package
* Run the `org.easybatch.tutorials.advanced.distributed.RestEndpointRecordDispatcherLauncher` class without any argument to launch the rest endpoint listener.
