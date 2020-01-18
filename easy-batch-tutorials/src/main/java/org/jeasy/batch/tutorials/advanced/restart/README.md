# Tutorial: Writing a custom listener to restart a failed job from the last save point

## Description

The goal of this tutorial is to write a custom listener that saves the state of a job in a persistent store (a journal file for instance).

* The `BuggyWriter` will randomly fail when writing records, which will make the job to fail
* The `CheckPointListener` is responsible for writing the job status and the last successfully written record in the journal file

When the job fails, restarting it should skip the correctly processed and written records and continue where it left off.

The job in this tutorial will read records from `src/main/resources/data/tweets.csv` and copy them to `target/tweets-out.csv`.

* The input file contains 5 tweets.
* The batch size is set to 3, which means there will be 2 batches to write.
* The `BuggyWriter` will randomly fail when writing the **second** batch

## Expected results

#### First execution

* If the first execution of the job completes, then `tweets-out.csv` should contain all tweets in `src/main/resources/data/tweets.csv`.
* If the first execution of the job fails, then:
 - `tweets-out.csv` should contain the first 3 tweets from `src/main/resources/data/tweets.csv`
 - `target/checkpoint.oplog` should contain two properties: `job.status=FAILED` and `write.last=3`

#### Second execution (if the job fails during the first attempt)

* If you restart the job, you should see the following message in the console:

`INFO: Last run has failed, records 1..3 will be skipped`

* If the second run fails again, then the persisted state will not change. You can restart the job again (until success)
* If the second run completes, then the second batch of tweets should be written in the `tweets-out.csv` file.

## Run the tutorial

### From the command line

Open a terminal and run the following commands:

```shell
$>cd easy-batch-tutorials
$>mvn install
$>mvn exec:java -PrunCheckPointListenerTutorial
```

### From Your IDE

* Import the `easy-batch-tutorials` project in your IDE
* Resolve maven dependencies
* Navigate to the `org.jeasy.batch.tutorials.advanced.restart` package
* Run the `org.jeasy.batch.tutorials.advanced.restart.Launcher` class without any argument

## Sample output

### First attempt (the job fails)

```shell
~/projects/easy-batch-tutorials $ mvn exec:java -PrunCheckPointListenerTutorial
[pool-1-thread-1] INFO org.jeasy.batch.core.job.BatchJob - Job 'job' starting
[pool-1-thread-1] INFO org.jeasy.batch.core.job.BatchJob - Batch size: 3
[pool-1-thread-1] INFO org.jeasy.batch.core.job.BatchJob - Error threshold: N/A
[pool-1-thread-1] INFO org.jeasy.batch.core.job.BatchJob - Jmx monitoring: false
[pool-1-thread-1] INFO org.jeasy.batch.core.job.BatchJob - Job 'job' started
[pool-1-thread-1] ERROR org.jeasy.batch.core.job.BatchJob - Unable to write records
java.lang.Exception: Unable to write records
	at org.jeasy.batch.tutorials.advanced.restart.BuggyWriter.writeRecords(BuggyWriter.java:28)
	at org.jeasy.batch.core.job.BatchJob.writeBatch(BatchJob.java:241)
	at org.jeasy.batch.core.job.BatchJob.call(BatchJob.java:109)
	at org.jeasy.batch.core.job.BatchJob.call(BatchJob.java:51)
	at java.util.concurrent.FutureTask.run(FutureTask.java:266)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
	at java.lang.Thread.run(Thread.java:748)
[pool-1-thread-1] INFO org.jeasy.batch.core.job.BatchJob - Job 'job' finished with status: FAILED
```

### Check content of journal and output file

```properties
~/projects/easy-batch-tutorials $ cat target/checkpoint.oplog
#setting key 'job.status' to FAILED
#Sat Jan 18 22:47:32 CET 2020
job.end=2020-01-18 10\:47\:32
write.last=3
job.name=job
job.status=FAILED
job.start=2020-01-18 10\:47\:32

~/projects/easy-batch-tutorials $ cat target/tweets-out.csv
id,user,message
1,foo,easy batch rocks! #EasyBatch
2,bar,@foo I do confirm :-)
```

### Second attempt (the job completes)

```shell
~/projects/easy-batch-tutorials $ mvn exec:java -PrunCheckPointListenerTutorial
[org.jeasy.batch.tutorials.advanced.restart.Launcher.main()] INFO org.jeasy.batch.tutorials.advanced.restart.CheckPointListener - Last run has failed, records 1..3 will be skipped
[pool-1-thread-1] INFO org.jeasy.batch.core.job.BatchJob - Job 'job' starting
[pool-1-thread-1] INFO org.jeasy.batch.core.job.BatchJob - Batch size: 3
[pool-1-thread-1] INFO org.jeasy.batch.core.job.BatchJob - Error threshold: N/A
[pool-1-thread-1] INFO org.jeasy.batch.core.job.BatchJob - Jmx monitoring: false
[pool-1-thread-1] INFO org.jeasy.batch.core.job.BatchJob - Job 'job' started
[pool-1-thread-1] INFO org.jeasy.batch.core.job.BatchJob - Job 'job' stopping
[pool-1-thread-1] INFO org.jeasy.batch.core.job.BatchJob - Job 'job' finished with status: COMPLETED
```

### Check content of journal and output file

```properties
~/projects/easy-batch-tutorials $ cat target/checkpoint.oplog
#setting key 'job.status' to COMPLETED
#Sat Jan 18 22:48:00 CET 2020
job.end=2020-01-18 10\:48\:00
job.name=job
write.last=5
job.start=2020-01-18 10\:48\:00
job.status=COMPLETED

~/projects/easy-batch-tutorials $ cat target/tweets-out.csv
id,user,message
1,foo,easy batch rocks! #EasyBatch
2,bar,@foo I do confirm :-)
3,baz,@foo @bar Is it really easy?
4,toto,@foo @bar @baz yeah! kinda KISS framework
```
