# Welcome to Easy Batch tutorials

## Introduction

Tutorials are organised in 3 levels (Basic, Intermediate and Advanced).
Each tutorial is contained in a separate package containing a `README.md` file that describes the tutorial and how to run it.
The `org.jeasy.batch.tutorials.common` package contains classes that are common to all tutorials.
All examples use tweets data. Tweets are represented by the `org.jeasy.batch.tutorials.common.Tweet` bean.

## Quick start

:information_source: Prerequisites: Java 8+ && maven 3+

:arrow_down: Get the source code: `$>git clone https://github.com/j-easy/easy-batch.git`

:hash: Build: `$>cd easy-batch && mvn install && cd easy-batch-tutorials`
 
:arrow_forward: Run: `$>mvn exec:java -P run[tutorial name]`

If you want to run tutorials from your IDE, you need to import the `easy-batch` directory as a maven project in your IDE.

## Tutorials index

| :scroll: Tutorial  | :star: Level  |  :clock9: Duration  |  :link: Link  |
|:----------|:------:|:----------:|:------:|
|Hello World|basic|:two: minutes read|[:arrow_forward: View](https://github.com/j-easy/easy-batch/tree/master/easy-batch-tutorials/src/main/java/org/jeasy/batch/tutorials/basic/helloworld)|
|Word Count|basic|:two: minutes read|[:arrow_forward: View ](https://github.com/j-easy/easy-batch/tree/master/easy-batch-tutorials/src/main/java/org/jeasy/batch/tutorials/basic/wordcount)|
|Transforming data from one format to another|intermediate|:five: minutes read|[:arrow_forward: View ](https://github.com/j-easy/easy-batch/tree/master/easy-batch-tutorials/src/main/java/org/jeasy/batch/tutorials/intermediate/csv2xml)|
|Populating a search database|intermediate|:five: minutes read|[:arrow_forward: View ](https://github.com/j-easy/easy-batch/tree/master/easy-batch-tutorials/src/main/java/org/jeasy/batch/tutorials/intermediate/elasticsearch)|
|Importing data from a flat file into a database|intermediate|:five: minutes read|[:arrow_forward: View ](https://github.com/j-easy/easy-batch/tree/master/easy-batch-tutorials/src/main/java/org/jeasy/batch/tutorials/intermediate/load)|
|Exporting data from a database to a flat file|intermediate|:five: minutes read|[:arrow_forward: View ](https://github.com/j-easy/easy-batch/tree/master/easy-batch-tutorials/src/main/java/org/jeasy/batch/tutorials/intermediate/extract)|
|Writing a dynamic header/footer to an output file|intermediate|:five: minutes read|[:arrow_forward: View ](https://github.com/j-easy/easy-batch/tree/master/easy-batch-tutorials/src/main/java/org/jeasy/batch/tutorials/intermediate/headerfooter)|
|Reading data from a REST endpoint|intermediate|:five: minutes read|[:arrow_forward: View ](https://github.com/j-easy/easy-batch/tree/master/easy-batch-tutorials/src/main/java/org/jeasy/batch/tutorials/intermediate/rest)|
|Monitoring jobs with JMX|advanced|:five: minutes read|[:arrow_forward: View ](https://github.com/j-easy/easy-batch/tree/master/easy-batch-tutorials/src/main/java/org/jeasy/batch/tutorials/advanced/jmx)|
|Scheduling jobs with Quartz|advanced|:five: minutes read|[:arrow_forward: View ](https://github.com/j-easy/easy-batch/tree/master/easy-batch-tutorials/src/main/java/org/jeasy/batch/tutorials/advanced/quartz)|
|Configuring jobs with Spring|advanced|:five: minutes read|[:arrow_forward: View ](https://github.com/j-easy/easy-batch/tree/master/easy-batch-tutorials/src/main/java/org/jeasy/batch/tutorials/advanced/spring)|
|Writing a custom listener to contribute custom metrics|advanced|:keycap_ten: minutes read|[:arrow_forward: View ](https://github.com/j-easy/easy-batch/tree/master/easy-batch-tutorials/src/main/java/org/jeasy/batch/tutorials/advanced/metric)|
|Writing a custom listener to restart a failed job|advanced|:keycap_ten: minutes read|[:arrow_forward: View ](https://github.com/j-easy/easy-batch/tree/master/easy-batch-tutorials/src/main/java/org/jeasy/batch/tutorials/advanced/restart)|
|Writing a custom reader to support multi-line records|advanced|:keycap_ten: minutes read|[:arrow_forward: View ](https://github.com/j-easy/easy-batch/tree/master/easy-batch-tutorials/src/main/java/org/jeasy/batch/tutorials/advanced/recipes)|
|Skipping bad records with batch scanning on write failure|advanced|:keycap_ten: minutes read|[:arrow_forward: View ](https://github.com/j-easy/easy-batch/tree/master/easy-batch-tutorials/src/main/java/org/jeasy/batch/tutorials/advanced/scanning)|
|Processing data in parallel with multiple worker jobs|advanced|:keycap_ten: minutes read|[:arrow_forward: View ](https://github.com/j-easy/easy-batch/tree/master/easy-batch-tutorials/src/main/java/org/jeasy/batch/tutorials/advanced/parallel)|
|Processing data asynchronously using a JMS queue|advanced|:keycap_ten: minutes read|[:arrow_forward: View ](https://github.com/j-easy/easy-batch/tree/master/easy-batch-tutorials/src/main/java/org/jeasy/batch/tutorials/advanced/jms)|
|Processing files in parallel based on their content|advanced|:keycap_ten: minutes read|[:arrow_forward: View ](https://github.com/j-easy/easy-batch/tree/master/easy-batch-tutorials/src/main/java/org/jeasy/batch/tutorials/advanced/cbrd)|
