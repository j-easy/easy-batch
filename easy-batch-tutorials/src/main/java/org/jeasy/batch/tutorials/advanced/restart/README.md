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

First, make sure you have already downloaded the source code and built the tutorials
as described in the [quick start](https://github.com/j-easy/easy-batch/tree/master/easy-batch-tutorials#quick-start) section.

### From the command line

Open a terminal and run the following command:

```shell
$>mvn exec:java -PrunCheckPointListenerTutorial
```

### From Your IDE

* Navigate to the `org.jeasy.batch.tutorials.advanced.restart` package
* Run the `org.jeasy.batch.tutorials.advanced.restart.Launcher` class without any argument

## Sample output

### First attempt (the job fails)

```shell
$ mvn exec:java -PrunCheckPointListenerTutorial
[pool-1-thread-1] INFO org.jeasy.batch.core.job.BatchJob - Job 'job' starting
[pool-1-thread-1] INFO org.jeasy.batch.core.job.BatchJob - Job 'job' started
[pool-1-thread-1] ERROR org.jeasy.batch.core.job.BatchJob - Unable to write records
java.lang.Exception: Unable to write records
	at org.jeasy.batch.tutorials.advanced.restart.BuggyWriter.writeRecords(BuggyWriter.java:28)
	at org.jeasy.batch.core.job.BatchJob.writeBatch(BatchJob.java:244)
	at org.jeasy.batch.core.job.BatchJob.call(BatchJob.java:112)
	at org.jeasy.batch.core.job.BatchJob.call(BatchJob.java:54)
	at java.util.concurrent.FutureTask.run(FutureTask.java:266)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
	at java.lang.Thread.run(Thread.java:748)
[pool-1-thread-1] INFO org.jeasy.batch.core.job.BatchJob - Job 'job' finished with status FAILED in 19ms
```

### Check content of journal and output file

```properties
$> cat target/checkpoint.oplog
#setting key 'job.status' to FAILED
#Thu Jan 23 09:45:34 CET 2020
job.end=2020-01-23 09\:45\:34
write.last=3
job.name=job
job.status=FAILED
job.start=2020-01-23 09\:45\:34

$> cat target/tweets-out.csv
id,user,message
1,foo,easy batch rocks! #EasyBatch
2,bar,@foo I do confirm :-)
```

### Second attempt (the job completes)

```shell
$> mvn exec:java -PrunCheckPointListenerTutorial
[org.jeasy.batch.tutorials.advanced.restart.Launcher.main()] INFO org.jeasy.batch.tutorials.advanced.restart.CheckPointListener - Last run has failed, records 1..3 will be skipped
[pool-1-thread-1] INFO org.jeasy.batch.core.job.BatchJob - Job 'job' starting
[pool-1-thread-1] INFO org.jeasy.batch.core.job.BatchJob - Job 'job' started
[pool-1-thread-1] INFO org.jeasy.batch.core.job.BatchJob - Job 'job' stopping
[pool-1-thread-1] INFO org.jeasy.batch.core.job.BatchJob - Job 'job' finished with status COMPLETED in 14ms
```

### Check content of journal and output file

```properties
$> cat target/checkpoint.oplog
#setting key 'job.status' to COMPLETED
#Thu Jan 23 09:49:10 CET 2020
job.end=2020-01-23 09\:49\:10
job.name=job
write.last=5
job.start=2020-01-23 09\:49\:10
job.status=COMPLETED

$> cat target/tweets-out.csv
id,user,message
1,foo,easy batch rocks! #EasyBatch
2,bar,@foo I do confirm :-)
3,baz,@foo @bar Is it really easy?
4,toto,@foo @bar @baz yeah! kinda KISS framework
```
