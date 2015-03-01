# Welcome to Easy Batch tutorials

You will find here several tutorials of different levels that will introduce you to all APIs of the framework.

The tutorials are organised in 3 levels:

* Basic
* Intermediate
* Advanced

Each tutorial is contained in a separate package. In each package, there is `readme.md` file that describes the tutorial and how to run it.

Navigate to the package you are interested in and you will be able to get started quickly. Each tutorial is shipped with a
main class to launch it. To run a tutorial, just import the `easybatch-tutorials` module in your favorite IDE and launch the main class.

The `org.easybatch.tutorials.common` package contains main classes that are common to all tutorials.

Here is the tutorials list:

### Basic tutorials

* [Hello world][]: The simplest batch application that you can write with Easy Batch
* [Key APIs][]: A walk through Key APIs to show you how to use them the right way
* [Unix-like pipeline][]: A sample application to process data in pipeline the unix-way
* [Filter-Map-Reduce][]: A filter-map-reduce example using Easy Batch

### Intermediate tutorials

* [Loading data in a relational database][]: A batch application that imports data from a flat file into a relational database
* [Loading data in a NoSQL database][]: A batch application that loads data from a flat file into a MongoDB server
* [Extracting data from a NoSQL database][]: A batch application that extracts data from a MongoDB collection and export it in a XML file
* [Elastic Search][]: A sample application that extracts data from a relational database and index it in a ElasticSearch server
* [Recipes][]: A showcase of how to write a custom reader to read data in a non standard format

### Advanced tutorials

* [Spring][]: A tutorial to show how to use Easy Batch as a Spring bean
* [Quartz][]: Learn how to schedule Easy Batch jobs with Quartz
* [JMX][]: Learn how to monitor Easy Batch jobs with JMX
* [JMS][]: Learn how to create asynchronous batch applications using Easy Batch and JMS
* [Parallel processing][]: An example of how to use Easy Batch to process data in parallel
* [Distributed jobs][]: An example of how to use Easy Batch to process data in a distributed environment
* [ContentBasedRecordDispatcher sample 1][]: An example of how to process a collection of objects based on their type
* [ContentBasedRecordDispatcher sample 2][]: An example of how to process multiple files in parallel based on their content

[Hello world]: https://github.com/benas/easy-batch/tree/master/easybatch-tutorials/src/main/java/org/easybatch/tutorials/basic/helloworld
[Key APIs]: https://github.com/benas/easy-batch/tree/master/easybatch-tutorials/src/main/java/org/easybatch/tutorials/basic/keyapis
[Unix-like pipeline]: https://github.com/benas/easy-batch/tree/master/easybatch-tutorials/src/main/java/org/easybatch/tutorials/basic/pipeline
[Filter-Map-Reduce]: https://github.com/benas/easy-batch/tree/master/easybatch-tutorials/src/main/java/org/easybatch/tutorials/basic/filterMapReduce
[Loading data in a relational database]: https://github.com/benas/easy-batch/tree/master/easybatch-tutorials/src/main/java/org/easybatch/tutorials/intermediate/load
[Loading data in a NoSQL database]: https://github.com/benas/easy-batch/tree/master/easybatch-tutorials/src/main/java/org/easybatch/tutorials/intermediate/mongodb/load
[Extracting data from a NoSQL database]: https://github.com/benas/easy-batch/tree/master/easybatch-tutorials/src/main/java/org/easybatch/tutorials/intermediate/mongodb/extract
[Elastic Search]: https://github.com/benas/easy-batch/tree/master/easybatch-tutorials/src/main/java/org/easybatch/tutorials/intermediate/elasticsearch
[Recipes]: https://github.com/benas/easy-batch/tree/master/easybatch-tutorials/src/main/java/org/easybatch/tutorials/intermediate/recipes
[Spring]: https://github.com/benas/easy-batch/tree/master/easybatch-tutorials/src/main/java/org/easybatch/tutorials/advanced/spring
[Quartz]: https://github.com/benas/easy-batch/tree/master/easybatch-tutorials/src/main/java/org/easybatch/tutorials/advanced/quartz
[JMX]: https://github.com/benas/easy-batch/tree/master/easybatch-tutorials/src/main/java/org/easybatch/tutorials/advanced/jmx
[JMS]: https://github.com/benas/easy-batch/tree/master/easybatch-tutorials/src/main/java/org/easybatch/tutorials/advanced/jms
[Parallel processing]: https://github.com/benas/easy-batch/tree/master/easybatch-tutorials/src/main/java/org/easybatch/tutorials/advanced/parallel
[Distributed jobs]: https://github.com/benas/easy-batch/tree/master/easybatch-tutorials/src/main/java/org/easybatch/tutorials/advanced/distributed
[ContentBasedRecordDispatcher sample 1]: https://github.com/benas/easy-batch/tree/master/easybatch-tutorials/src/main/java/org/easybatch/tutorials/advanced/cbrd/fruits
[ContentBasedRecordDispatcher sample 2]: https://github.com/benas/easy-batch/tree/master/easybatch-tutorials/src/main/java/org/easybatch/tutorials/advanced/cbrd/files
