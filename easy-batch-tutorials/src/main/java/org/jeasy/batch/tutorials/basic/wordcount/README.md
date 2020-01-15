# Tutorial: Word Count

## Description

This tutorial is a simple batch application that counts the number of occurrences of each word in a flat file. We will create:

* A record mapper called `LineTokenizer` that maps each line to a list of its words
* A record processor called `WordCounter` that counts the occurrences of each word

## Run the tutorial

### From the command line

Open a terminal and run the following commands:

```
$>cd easy-batch-tutorials
$>mvn install
$>mvn exec:java -PrunWordCountTutorial
```

### From Your IDE

* Import the `easy-batch-tutorials` project in your IDE
* Resolve maven dependencies
* Navigate to the `org.jeasy.batch.tutorials.basic.wordcount` package
* Run the `org.jeasy.batch.tutorials.basic.wordcount.Launcher` class without any argument
