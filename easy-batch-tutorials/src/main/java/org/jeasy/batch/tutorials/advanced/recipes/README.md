# Tutorial: Writing a custom reader to support multi-line records

## Description

This tutorial is an application that reads recipes from a flat file and prints them out to the standard output:

<div align="center">
    <img src="multi-line-records.png" alt="recipes" style="width:50%;height:50%;">
</div>

Each recipe is split on multiple records:

* R record: representing a recipe
* I record: representing an ingredient of the recipe
* END record: representing the end of the recipe data

The goal of the tutorial is to show how to write a custom record reader to read multi-line records.

## Run the tutorial

First, make sure you have already downloaded the source code and built the tutorials
as described in the [quick start](https://github.com/j-easy/easy-batch/tree/master/easy-batch-tutorials#quick-start) section.

### From the command line

Open a terminal and run the following commands:

```
$>mvn exec:java -PrunRecipesTutorial
```

### From Your IDE

* Navigate to the `org.jeasy.batch.tutorials.advanced.recipes` package
* Run the `org.jeasy.batch.tutorials.advanced.recipes.Launcher` class without any argument
