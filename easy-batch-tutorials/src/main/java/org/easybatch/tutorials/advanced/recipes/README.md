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

### From the command line

Open a terminal and run the following commands:

```
$>cd easybatch-tutorials
$>mvn install
$>mvn exec:java -PrunRecipesTutorial
```

### From Your IDE

* Import the `easybatch-tutorials` project in your IDE
* Resolve maven dependencies
* Navigate to the `org.easybatch.tutorials.advanced.recipes` package
* Run the `org.easybatch.tutorials.advanced.recipes.Launcher` class without any argument
