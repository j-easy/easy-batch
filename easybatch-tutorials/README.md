# Welcome to Easy Batch tutorials

## Introduction

Tutorials are organised in 3 levels (Basic, Intermediate and Advanced).
Each tutorial is contained in a separate package containing a `README.md` file that describes the tutorial and how to run it.
The `org.easybatch.tutorials.common` package contains classes that are common to all tutorials.
All examples use tweets data. Tweets are represented by the `org.easybatch.tutorials.common.Tweet` bean.

:warning: Tutorials described in this page use the current stable version of Easy Batch: [![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.easybatch/easybatch-core/badge.svg?style=flat)](http://search.maven.org/#artifactdetails|org.easybatch|easybatch-core|5.2.0|)

## Quick start

| :information_source: Prerequisites | :arrow_down: Download   | :hash: Build | :arrow_forward: Run |
|------------------------------------|-------------------------|--------------|---------------------|
|Java 7+ && maven 3+ |[Get source code](https://github.com/j-easy/easy-batch/archive/master.zip)|`$>mvn install`|`$>mvn exec:java -P run[tutorial name]`|

:bulb: The `src/main/resources/logging.properties` can be used to show detailed logs and help you better understand how jobs work

## Tutorials index

| :scroll: Tutorial  | :star: Level  |  :clock9: Duration  |  :link: Link  |
|:----------|:------:|:----------:|:------:|
|Hello world|basic|:two: minutes read|[:arrow_forward: View](https://github.com/j-easy/easy-batch/tree/master/easybatch-tutorials/src/main/java/org/easybatch/tutorials/basic/helloworld)|
|Word Count|basic|:two: minutes read|[:arrow_forward: View ](https://github.com/j-easy/easy-batch/tree/master/easybatch-tutorials/src/main/java/org/easybatch/tutorials/basic/wordcount)|
|Transforming data from one format to another|intermediate|:five: minutes read|[:arrow_forward: View ](https://github.com/j-easy/easy-batch/tree/master/easybatch-tutorials/src/main/java/org/easybatch/tutorials/intermediate/csv2xml)|
|Populating a search database|intermediate|:five: minutes read|[:arrow_forward: View ](https://github.com/j-easy/easy-batch/tree/master/easybatch-tutorials/src/main/java/org/easybatch/tutorials/intermediate/elasticsearch)|
|Importing data from a flat file into a database|intermediate|:five: minutes read|[:arrow_forward: View ](https://github.com/j-easy/easy-batch/tree/master/easybatch-tutorials/src/main/java/org/easybatch/tutorials/intermediate/load)|
|Exporting data from a database to a flat file|intermediate|:five: minutes read|[:arrow_forward: View ](https://github.com/j-easy/easy-batch/tree/master/easybatch-tutorials/src/main/java/org/easybatch/tutorials/intermediate/extract)|
|Exporting data from a database to a flat file with a header and a footer|intermediate|:five: minutes read|[:arrow_forward: View ](https://github.com/j-easy/easy-batch/tree/master/easybatch-tutorials/src/main/java/org/easybatch/tutorials/intermediate/headerfooter)|
|Monitoring jobs with JMX|advanced|:five: minutes read|[:arrow_forward: View ](https://github.com/j-easy/easy-batch/tree/master/easybatch-tutorials/src/main/java/org/easybatch/tutorials/advanced/jmx)|
|Scheduling jobs with Quartz|advanced|:five: minutes read|[:arrow_forward: View ](https://github.com/j-easy/easy-batch/tree/master/easybatch-tutorials/src/main/java/org/easybatch/tutorials/advanced/quartz)|
|Configuring jobs with Spring|advanced|:five: minutes read|[:arrow_forward: View ](https://github.com/j-easy/easy-batch/tree/master/easybatch-tutorials/src/main/java/org/easybatch/tutorials/advanced/spring)|
|Writing a custom listener to contribute custom metrics|advanced|:keycap_ten: minutes read|[:arrow_forward: View ](https://github.com/j-easy/easy-batch/tree/master/easybatch-tutorials/src/main/java/org/easybatch/tutorials/advanced/metric)|
|Writing a custom listener to restart a failed job|advanced|:keycap_ten: minutes read|[:arrow_forward: View ](https://github.com/j-easy/easy-batch/tree/master/easybatch-tutorials/src/main/java/org/easybatch/tutorials/advanced/restart)|
|Writing a custom reader to support multi-line records|advanced|:keycap_ten: minutes read|[:arrow_forward: View ](https://github.com/j-easy/easy-batch/tree/master/easybatch-tutorials/src/main/java/org/easybatch/tutorials/advanced/recipes)|
|Processing data in parallel with multiple worker jobs|advanced|:keycap_ten: minutes read|[:arrow_forward: View ](https://github.com/j-easy/easy-batch/tree/master/easybatch-tutorials/src/main/java/org/easybatch/tutorials/advanced/parallel)|
|Processing data asynchronously using a JMS queue|advanced|:keycap_ten: minutes read|[:arrow_forward: View ](https://github.com/j-easy/easy-batch/tree/master/easybatch-tutorials/src/main/java/org/easybatch/tutorials/advanced/jms)|
|Processing files in parallel based on their content|advanced|:keycap_ten: minutes read|[:arrow_forward: View ](https://github.com/j-easy/easy-batch/tree/master/easybatch-tutorials/src/main/java/org/easybatch/tutorials/advanced/cbrd)|
