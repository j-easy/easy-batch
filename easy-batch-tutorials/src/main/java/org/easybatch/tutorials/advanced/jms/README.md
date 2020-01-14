# Tutorial: Processing data asynchronously using a JMS queue

## Description

This tutorial is a show case of how to implement asynchronous jobs using a JMS queue.

## Run the tutorial

### From the command line

Open a terminal and run the following commands:

```
$>cd easybatch-tutorials
$>mvn install
$>mvn exec:java -PrunJmsTutorial
```

This will start an embedded JMS broker listening to incoming messages.

Open a second terminal and run the following command:

`mvn exec:java -PrunJmsSender`

You will be able to type in tweets in the console to post them to the JMS queue and see how the job will process them as they come.
To quit the application, type in "quit" in order to send a poison record in order to stop the job.

### From Your IDE

* Import the `easybatch-tutorials` project in your IDE
* Resolve maven dependencies
* Navigate to the `org.easybatch.tutorials.advanced.jms` package
* Run the `org.easybatch.tutorials.advanced.jms.Launcher` class without any argument to launch the job
* Run the `org.easybatch.tutorials.advanced.jms.JMSSenderLauncher` class without any argument to launch the JMS message sender application
