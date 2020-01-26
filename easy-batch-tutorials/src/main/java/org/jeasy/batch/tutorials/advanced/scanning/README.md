# Tutorial: Batch scanning on write failure

## Description

The batch scanning feature allows you to scan a batch for the faulty record
when an exception occurs during batch writing. Each record will be re-written
alone as a singleton batch by the writer. Scanned records will be flagged using
the `Header#isScanned()` boolean to be able to identify them in listeners.

This feature works well with transactional writers where a failed write
operation can be re-executed without side effects. However, a known limitation
is that when used with a non-transactional writer, items might be written twice
(like in the case of a file writer where the output stream is flushed before the
exception occurs). To prevent this, a manual rollback action should be done in
a `BatchListener#onBatchWritingException(Batch, Throwable)` method.

The goal here is to read tweets from a CSV file and load them in a relational database.
Two of the tweets (records 3 and 8) have a message length bigger than the size
of the message column in the database and will cause a batch write error. Batch
scanning will kick in and re-write records one by one. Skipped records will be logged
in a file (`target/skipped-tweets.csv`).

## Run the tutorial

First, make sure you have already downloaded the source code and built the tutorials
as described in the [quick start](https://github.com/j-easy/easy-batch/tree/master/easy-batch-tutorials#quick-start) section.

### From the command line

Open a terminal and run the following command:

```
$>mvn exec:java -PrunBatchScanningTutorial
```

### From Your IDE

* Navigate to the `org.jeasy.batch.tutorials.advanced.scanning` package
* Run the `org.jeasy.batch.tutorials.advanced.scanning.Launcher` class without any argument
