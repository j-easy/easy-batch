# Tutorial: Word Count

## Description

This tutorial is a simple batch application that counts the number of occurrences of each word in a flat file. We will create:

* A record mapper called `LineTokenizer` that maps each line to a list of its words
* A record processor called `WordCounter` that counts the occurrences of each word

## Run the tutorial

First, make sure you have already downloaded the source code and built the tutorials
as described in the [quick start](https://github.com/j-easy/easy-batch/tree/master/easy-batch-tutorials#quick-start) section.

### From the command line

Open a terminal and run the following command:

```
$>mvn exec:java -PrunWordCountTutorial
```

### From Your IDE

* Navigate to the `org.jeasy.batch.tutorials.basic.wordcount` package
* Run the `org.jeasy.batch.tutorials.basic.wordcount.Launcher` class without any argument
