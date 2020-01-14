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
$>cd easybatch-tutorials
$>mvn install
$>mvn exec:java -PrunCheckPointListenerTutorial
```

### From Your IDE

* Import the `easybatch-tutorials` project in your IDE
* Resolve maven dependencies
* Navigate to the `org.easybatch.tutorials.advanced.restart` package
* Run the `org.easybatch.tutorials.advanced.restart.Launcher` class without any argument

## Sample output

### First attempt (the job fails)

```shell
~/projects/easybatch-tutorials $ mvn exec:java -PrunCheckPointListenerTutorial
2016-10-14 17:30:39.489 INFO [org.easybatch.core.job.BatchJob setStatus] - Job 'job' starting
2016-10-14 17:30:39.492 INFO [org.easybatch.core.job.BatchJob start] - Batch size: 3
2016-10-14 17:30:39.493 INFO [org.easybatch.core.job.BatchJob start] - Error threshold: N/A
2016-10-14 17:30:39.493 INFO [org.easybatch.core.job.BatchJob start] - Jmx monitoring: false
2016-10-14 17:30:39.497 INFO [org.easybatch.core.job.BatchJob setStatus] - Job 'job' started
2016-10-14 17:30:39.505 SEVERE [org.easybatch.core.job.BatchJob fail] - Unable to write records
java.lang.Exception: Unable to write records
	at org.easybatch.tutorials.advanced.restart.BuggyWriter.writeRecords(BuggyWriter.java:28)
	at org.easybatch.core.job.BatchJob.writeBatch(BatchJob.java:198)
	at org.easybatch.core.job.BatchJob.call(BatchJob.java:79)
	at org.easybatch.core.job.BatchJob.call(BatchJob.java:22)
	at java.util.concurrent.FutureTask.run(FutureTask.java:266)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)
	at java.lang.Thread.run(Thread.java:745)
2016-10-14 17:30:39.507 INFO [org.easybatch.core.job.BatchJob teardown] - Job 'job' finished with status: FAILED
```

### Check content of journal and output file

```properties
~/projects/easybatch-tutorials $ cat target/checkpoint.oplog
#setting key 'job.status' to FAILED
#Fri Oct 14 17:30:39 CEST 2016
job.end=2016-10-14 05\:30\:39
write.last=3
job.name=job
job.status=FAILED
job.start=2016-10-14 05\:30\:39

~/projects/easybatch-tutorials $ cat target/tweets-out.csv
id,user,message
1,foo,easy batch rocks! #EasyBatch
2,bar,@foo I do confirm :-)
```

### Second attempt (the job fails again)

```shell
~/projects/easybatch-tutorials $ mvn exec:java -PrunCheckPointListenerTutorial
Oct 14, 2016 5:31:22 PM org.easybatch.tutorials.advanced.restart.CheckPointListener <init>
INFO: Last run has failed, records 1..3 will be skipped
Oct 14, 2016 5:31:22 PM org.easybatch.core.job.BatchJob setStatus
INFO: Job 'job' starting
Oct 14, 2016 5:31:22 PM org.easybatch.core.job.BatchJob start
INFO: Batch size: 3
Oct 14, 2016 5:31:22 PM org.easybatch.core.job.BatchJob start
INFO: Error threshold: N/A
Oct 14, 2016 5:31:22 PM org.easybatch.core.job.BatchJob start
INFO: Jmx monitoring: false
Oct 14, 2016 5:31:22 PM org.easybatch.core.job.BatchJob setStatus
INFO: Job 'job' started
Oct 14, 2016 5:31:22 PM org.easybatch.core.job.BatchJob fail
SEVERE: Unable to write records
java.lang.Exception: Unable to write records
	at org.easybatch.tutorials.advanced.restart.BuggyWriter.writeRecords(BuggyWriter.java:28)
	at org.easybatch.core.job.BatchJob.writeBatch(BatchJob.java:198)
	at org.easybatch.core.job.BatchJob.call(BatchJob.java:79)
	at org.easybatch.core.job.BatchJob.call(BatchJob.java:22)
	at java.util.concurrent.FutureTask.run(FutureTask.java:266)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)
	at java.lang.Thread.run(Thread.java:745)
Oct 14, 2016 5:31:22 PM org.easybatch.core.job.BatchJob teardown
INFO: Job 'job' finished with status: FAILED
```

### Third attempt (the job completes)

```shell
~/projects/easybatch-tutorials $ mvn exec:java -PrunCheckPointListenerTutorial
Oct 14, 2016 5:31:27 PM org.easybatch.tutorials.advanced.restart.CheckPointListener <init>
INFO: Last run has failed, records 1..3 will be skipped
Oct 14, 2016 5:31:27 PM org.easybatch.core.job.BatchJob setStatus
INFO: Job 'job' starting
Oct 14, 2016 5:31:27 PM org.easybatch.core.job.BatchJob start
INFO: Batch size: 3
Oct 14, 2016 5:31:27 PM org.easybatch.core.job.BatchJob start
INFO: Error threshold: N/A
Oct 14, 2016 5:31:27 PM org.easybatch.core.job.BatchJob start
INFO: Jmx monitoring: false
Oct 14, 2016 5:31:27 PM org.easybatch.core.job.BatchJob setStatus
INFO: Job 'job' started
Oct 14, 2016 5:31:27 PM org.easybatch.core.job.BatchJob setStatus
INFO: Job 'job' stopping
Oct 14, 2016 5:31:27 PM org.easybatch.core.job.BatchJob teardown
INFO: Job 'job' finished with status: COMPLETED
```

### Check content of journal and output file

```properties
~/projects/easybatch-tutorials $ cat target/checkpoint.oplog
#setting key 'job.status' to COMPLETED
#Fri Oct 14 17:31:27 CEST 2016
job.end=2016-10-14 05\:31\:27
job.name=job
write.last=5
job.start=2016-10-14 05\:31\:27
job.status=COMPLETED

~/projects/easybatch-tutorials $ cat target/tweets-out.csv
id,user,message
1,foo,easy batch rocks! #EasyBatch
2,bar,@foo I do confirm :-)
3,baz,@foo @bar Is it really easy?
4,toto,@foo @bar @baz yeah! kinda KISS framework
```
