# Java-Class-Updater

## Requirements
- Maven ^3.0.0
- Java ^1.8

## How to run from cmd
- Run `mvn verify` to generate shaded jar
- Locate jar, it should be `repo_base/target/eUpdater.jar`
- Run `java -jar path/to/jar/eUpdater.jar`

# Notes

`https://villavu.com/forum/showthread.php?t=116829`

## General Overview
Much of this updater is written quite neat and organized, while other parts of it are very inefficient and bloated. In my last rewrite of it, I decided to keep the deob bloated, as it works very well and doesn't need updating often (if ever). I spent the majority of my time ensuring that the class and field finders were very easy to modify and use. This updater is strongly based on deobfuscation of the gamepack, much of which is specifically done for an individual pattern. This allows each pattern to be incredible strong, and allows the finding of patterns simple, due to the fact that they rarely change since the deob removes the changes to the bytecode. Many of the patterns used in the updater are your standard regex patterns, i.e ALOAD, GETFIELD, ACONST_NULL. For other patterns, those that can not be easily found with a standard regex pattern, I follow unique conditional statements or opaque predicates. This seems to be a unique approach as compared to the majority of updaters used. It allows the use of very small patterns that don't break, yet are still incredibly unique within the gamepack.

## Deobfuscation
The deob is located within the deob/ package and that is where all the deobfuscation occurs. The DeobFrame.java is simply an abstract class which we overwrite with the specific deob we are using. Deobsfucate.java contains the running portion of the code in which we add all the deob methods to a list, and run each entry. Method.java removes the unused methods within the pack (this was heavily taken from an old jh thread). Multipliers.java standardizes the way in which the multipliers are written in the pack. EqualSwap.java was originally done in order to standardize the way in which variables were assigned in the pack, but is now just a catch all for any type of deob that I add to the updater (very messy and unorganized). RemoveExceptions.java does just that, removes all exceptions that are added to the pack as a means of obfuscation (much of this was done together with Brandon). OpaquePredicates.java standardizes the comparators within all opaque predicates. MethodName.java removes dummy parameters from method names. Note: this is not the "proper" way in which to do this, and it renders the pack completely unable to run. This is because I don't remove the parameter from references to the method. Since I only use the updater to update and not for a bot, it doesn't affect me.

## Analyzers
Both the class finders and field finders are located within analysers/package.

## Class Analyzers
Within analysers/classes is where all the classes and methods live in which all the class names within the gamepack are located and identified. The classAnalyserFrame.java class is the framework that is extended for all of the individual class analyzers to use specifically. Most of the methods and fields that are contained within are self explanatory and easy to use. Each class finder extends the classAnalyserFrame and implements the abstract method "identify()". This is the actual working part of the code that is used to find the specific classes within the client. All of the classAnalsyers are run from a class mainAnalyser.java which is located within analysers/methods. This class loads all the class finders into a list and runs each one individually. If a class is unable to be found, it will prompt you to enter in the class name (if you have it).

## Field Analyzers
All of the field analyzers are located inside analysers/methods. This is where the majority of the time is spent in maintaining an updater, as these are the most apt to break. Similar to the class analyzers, there is a methodAnalyserFrame.java which is extended to every specific class of finders. Also similar to the class analyzers, the method "identify()" is implemented in every class and contains all the code to find all wanted fields within the class. Objects for the classes are created in the analysers/methods.java class. This allows us to access any of the field values from any location in the updater. This makes it easy to find fields that depend on the location of another. These analyzers are also run from the methods/mainAnalyser.java class.

## Updater Running
The main class that is run upon execution is the main/eUpdater.java class. There are several useful variables in which you can change depending on what you want: 
- Revision is the current revision of the client, if it is higher than the current gamepack downloaded, it will download a new one.
- simbaPrint if true will save the hooks file to a simba file located at: "C:/Simba/Includes/Reflection/lib/internal/Hooks.simba"
- logPrint will print the readable hooks file to the debug
- forceDeob/Download are self explanatory
- findMultis will find multipliers if true
- doRefactor will do a very crappy and basic refactor of the client if set true

## Using the Searchers
As much as possible, I tried to keep the bulk of the code used in finding fields done within other methods to allow the writing of each pattern to be as short as possible. I didn't want to do this to such an extreme that it made it difficult to make complex patterns either. As such, the searcher/ package contains a multitude of useful methods in which allow important bytecode determinations, with very little code needed.

## FieldSearcher
As the name implies, the FieldSearcher class contains methods that involve the fields within the gamepack. As such, all of these methods are used on a specific class within the gamepack, never a method (as a method doesn't own a class). 
A FieldSearcher object is created like so: 

`FieldSearcher Fs = new FieldSearcher(cF); //Where cF is a classFrame`

Most of the FieldSearcher methods or pretty self explanatory, but i'll write a bit about here anyway:
- findDesc(String desc) -> Returns first field found with matching desc
- countDesc(String desc) -> Returns the number of fields that have the specified desc
- findContainsDesc(String desc) -> Same as findDesc, except not exact
- findAccess(int Acc) -> Returns first field found with matching access
- findValue(int Acc)-> Returns true if a field is found with the matching value
- findDescInstance(String desc, int Instance) -> Same as findDesc, but will return the specified instance. This is useful for looping through fields

The FieldSearcher's are pretty limited and are mainly used for identifying classes, or very basic fields

# multiplierSearcher
This class is used to find the multiplier for a given integer field. Feel free to look through it to see how it works, but since it's used at the end on all fields, you really won't ever touch it.

# Searcher
This is the most used class within the updater, and almost every field within the updater is found by using it. It contains the majority of all the finders within the updater.
