# Tutorial: Processing data in parallel

## Description

Easy Batch jobs implement the `java.util.concurrent.Callable` interface.
This turns them into units of work that can be submitted to a `java.util.concurrent.ExecutorService`.
Using the `java.util.concurrent.ExecutorService` with a pool of threads, you can run multiple Easy Batch jobs in parallel.

There are at least 3 ways to process data in parallel:

* Processing data in parallel by creating physical partitions
* Processing data in parallel by creating logical partitions
* Processing data in parallel with fork/join model

We will implement each technique on the same application developed in the Hello world tutorial but with a huge tweets data source:

```
id,user,message
1,foo,easy batch rocks! #EasyBatch
2,bar,@foo I do confirm :-)
...
10000000,baz,@foo @bar what are you talking about? Am I in trouble?
```

## Processing data in parallel by creating physical partitions

Sometimes, it is possible to split the data source into multiple partitions:

![physical-partitions](physical-partitions.jpg)

Easy Batch **does not** provide a feature to partition input data.
If you decide to split your data source into multiples parts, you can still use Easy Batch jobs to process them in parallel.

You can run multiple jobs within the same JVM (one thread per job) or using multiple JVMs (one process per job).

| Advantages   | Drawbacks    |
|--------------|--------------|
| Each job reads only its own part and not the entire data source      | Need to split the data source into multiple parts      |

This example is implemented in the `PhysicalPartitions` class.

## Processing data in parallel by creating logical partitions

Sometimes it is just impossible to split the data source into multiple physical parts.
A common technique is to use a single data source but to instruct worker jobs to process separate parts and
 filter the rest of the data source.

Let's see an example:

![logical-partitions](logical-partitions.jpg)

* The first job will read data from `tweets.csv` file, process records 1-2 and filter records 3-4.
* The second job will also read data from `tweets.csv` file, process records 3-4 and filter records 1-2.

| Advantages   | Drawbacks    |
|--------------|--------------|
| No need to split the input data source into multiple parts      | Each job will read the entire data source      |

This example is implemented in the `LogicalPartitions` class.

## Processing data in parallel with fork/join model

In most cases, it is the processing step that is most expensive and should be done in parallel.
Reading and writing data could be done in serial.
The fork join model is a perfect choice to implement this use case:

![fork-join](fork-join.png)

In this tutorial, we will create:

* a "fork job" that will read records in sequence from the data source and distribute them to workers jobs
* 2 (or more) "worker jobs" to process records in parallel. Each worker job has its own work queue
* a "join job" that will read record from the joining queue and write them in sequence to a data sink

Each job will send a poison record to signal that it has finished its task.

| Advantages   | Drawbacks    |
|--------------|--------------|
| Best way to distribute work across multiple workers      | Need to setup an additional queues      |

This example is implemented in the `ForkJoin` class.

Sometimes, there is no need to write data to a data sink, only processing records in parallel is required:

![record-dispatching](record-dispatching.jpg)

You can find an example of this use case in the `RecordDispatching` class.

## Run the tutorials

### From the command line

Open a terminal and run the following commands:

```
$>cd easy-batch-tutorials
$>mvn install
$>mvn exec:java -PrunParallelProcessingWithLogicalPartitions
$>mvn exec:java -PrunParallelProcessingWithPhysicalPartitions
$>mvn exec:java -PrunParallelProcessingWithForkJoin
```

### From Your IDE

* Import the `easy-batch-tutorials` project in your IDE
* Resolve maven dependencies
* Navigate to the `org.jeasy.batch.tutorials.advanced.parallel` package
* Run the `org.jeasy.batch.tutorials.advanced.parallel.LogicalPartitions` class without any argument
* Run the `org.jeasy.batch.tutorials.advanced.parallel.PhysicalPartitions` class without any argument
* Run the `org.jeasy.batch.tutorials.advanced.parallel.ForkJoin` class without any argument
