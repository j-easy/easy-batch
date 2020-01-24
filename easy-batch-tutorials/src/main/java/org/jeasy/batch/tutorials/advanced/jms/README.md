# Tutorial: Processing data asynchronously using a JMS queue

## Description

This tutorial shows how to implement asynchronous jobs using a JMS queue.

## Run the tutorial

First, make sure you have already downloaded the source code and built the tutorials
as described in the [quick start](https://github.com/j-easy/easy-batch/tree/master/easy-batch-tutorials#quick-start) section.

### From the command line

Open a terminal and run the following command:

```
$>mvn exec:java -PrunJmsBroker
```

This will start an embedded JMS broker listening to incoming messages. You can stop
the broker by pressing any key at the end of the tutorial.

Open a second terminal and run the following command:

```
$>mvn exec:java -PrunJmsProducerJob
```

This will launch a batch job that reads tweets from a flat file and send them to
a JMS queue.

Open a third terminal and run the following command:

```
$>mvn exec:java -PrunJmsConsumerJob
```

This will launch a batch job that reads tweets from the queue and writes them in
uppercase to the standard output. After a timeout of 10 seconds, the job will be completed.

### From Your IDE

* Navigate to the `org.jeasy.batch.tutorials.advanced.jms` package
* Run the `org.jeasy.batch.tutorials.advanced.jms.JmsBrokerLauncher` class without any argument to start the JMS broker
* Run the `org.jeasy.batch.tutorials.advanced.jms.JmsProducerJobLauncher` class without any argument to launch the producer job
* Run the `org.jeasy.batch.tutorials.advanced.jms.JmsConsumerJobLauncher` class without any argument to launch the consumer job
