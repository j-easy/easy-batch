# Tutorial: Job monitoring

## Description

This tutorial is an application that reads tweets from a flat file and prints them out to the standard output.
The `TweetSlowProcessor` simulates a long running processor.

### Part 1:

In this section, the goal is to run the application and monitor the job using a standard JMX client.

* Run the tutorial with `mvn exec:java -PrunJobMonitoringTutorial` from the command line or by running the
`org.jeasy.batch.tutorials.advanced.jmx.JobMonitoringTutorial` class from your IDE

* Using a JMX client, navigate to the `org.jeasy.batch.core.monitor:name=job` MBean
 and you will be able to monitor the execution progress of the application in real time.

### Part 2:

In this section, we will implement a custom listener to monitor execution progress. The `ProgressListener` will:

* calculate the total number of records in the data source in order to calculate the progress.
* listen to notifications from the running job and print out progress to the console.

Run the tutorial:

* from the command line with `mvn exec:exec -PrunJobProgressTutorial`
* from your IDE by running the `org.jeasy.batch.tutorials.advanced.jmx.JobProgressTutorial` class with the following JMV parameters:
    - `-Dcom.sun.management.jmxremote.port=9999`
    - `-Dcom.sun.management.jmxremote.local.only=false`
    - `-Dcom.sun.management.jmxremote.authenticate=false`
    - `-Dcom.sun.management.jmxremote.ssl=false`

You should see the execution progress updated in real time in the standard output:

```
[pool-1-thread-1] INFO org.jeasy.batch.core.job.BatchJob - Job 'job' starting
[pool-1-thread-1] INFO org.jeasy.batch.core.job.BatchJob - Batch size: 100
[pool-1-thread-1] INFO org.jeasy.batch.core.job.BatchJob - Error threshold: N/A
[pool-1-thread-1] INFO org.jeasy.batch.core.job.BatchJob - Jmx monitoring: true
[pool-1-thread-1] INFO org.jeasy.batch.core.jmx.JobMonitor - Registering JMX MBean for job 'job'
[pool-1-thread-1] INFO org.jeasy.batch.core.jmx.JobMonitor - JMX MBean registered successfully as: org.jeasy.batch.core.monitor:name=job
[pool-1-thread-1] INFO org.jeasy.batch.core.job.BatchJob - Job 'job' started
[monitoring-thread] INFO org.jeasy.batch.core.jmx.JobMonitorProxy - Listening to notifications from service:jmx:rmi:///jndi/rmi://localhost:9999/jmxrmi
progress = 20.0%
progress = 40.0%
progress = 60.0%
progress = 80.0%
progress = 100.0%
[pool-1-thread-1] INFO org.jeasy.batch.core.job.BatchJob - Job 'job' stopping
[pool-1-thread-1] INFO org.jeasy.batch.core.job.BatchJob - Job 'job' finished with status: COMPLETED
```
