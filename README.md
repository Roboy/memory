# Roboy Memory Module

[![Documentation Status](https://readthedocs.org/projects/roboy-memory/badge/?version=latest)](http://roboy-memory.readthedocs.io/en/latest/?badge=latest)

[![Documentation Status](https://readthedocs.org/projects/roboy-memory/badge/?version=docs)](http://roboy-memory.readthedocs.io/en/develop/?badge=develop)

Detailed Documentation can be found [here](https://readthedocs.org/projects/roboy-memory)

- [Roboy Memory Module](#roboy-memory-module)
    - [What does Roboy Memory do](#what-does-roboy-memory-do)
    - [How does Roboy Memory work](#how-does-roboy-memory-work)
    - [Neo4J Database](#neo4j-database)
        - [Remote Neo4J Database](#remote-neo4j-database)
        - [Local Neo4J Database Generator](#local-neo4j-database-generator)
            - [Removing Generated Nodes](#removing-generated-nodes)
    - [Installation Instructions](#installation-instructions)
        - [Requirements](#requirements)
        - [Optional](#optional)
        - [Installing](#installing)
            - [Installation without Neo4J Tests](#installation-without-neo4j-tests)
            - [Thorough Guide](#thorough-guide)
            - [Installing for Usage with roboy_dialog](#installing-for-usage-with-roboydialog)
            - [Installing Memory Only](#installing-memory-only)

## What does Roboy Memory do

This repository contains a memory module developed to use for [Roboy](roboy.org), humanoid robot.
The goal of the project is to provide Roboy with modern graph-based Knowledge Representation.

Roboy should feature the ability to remember information about himself: his name, his age, his origin, his location, his friends,
etc.

The same is applicable to Roboy, when speaking about people who are his friends. Roboy should be able to tell information about a person or an object and be able to provide basic automatic inference (supported by the graph nature of KR). This way, Roboy Memory Module serves as a long-term memory repository of actionable information acquired by other Roboy modules. Persistency layer is presented by a Neo4j graph database.

## How does Roboy Memory work

Upon incoming request, a Java client will pre-process the request and initiate transaction with the database. Two ways of communication between Roboy Java client and Neo4J database are supported: communication using Neo4J driver operating Cypher query language and Neo4J native Java API. Cypher query language offers more flexible querying while communications via Neo4J Java API are implemented as usage-specific routines. Interfaces are implemented on top of ros through the Java client. The input is any type of information Roboy can retrieve from environment abiding by Knowledge Representation reference in format of Roboy Communication Standard protocol, the output are pieces of data related to the requested scope in the same form.

## Neo4J Database

### Remote Neo4J Database

The roboy team runs a remote Neo4J instance. If you wish to have a copy of this for local testing purposes, you can find more info [here](https://roboy-memory.readthedocs.io/en/latest/Usage/1_getting_started.html#local-neo4j-instance).

### Local Neo4J Database Generator

As one may wish to develop offline or perform risky tests, that may compromise database integrity, it is possible to generate a sample set of data. To do this, simply run the main method of `org.roboy.memory.data.Generator`. It will also generate a copy of the Roboy nodes, that Dialog uses. Each node that is created has a `Generated` property, with the version number of the generated data.

Note: You will still be required to set up the [environmental variables](https://roboy-memory.readthedocs.io/en/latest/Usage/1_getting_started.html#configuring-the-package).

#### Removing Generated Nodes

You can remove generated data, by running the following command `MATCH (node2delete {generated: '0.0.2'}) DETACH DELETE node2delete`. If you only want to delete nodes of a specific kind, such as `Person`s, run `MATCH (node2delete:Person {generated: '0.0.2'}) DETACH DELETE node2delete`.

If you don't care and wish to remove all generated data across versions, use `MATCH (node2delete) WHERE EXISTS(node2delete.generated) DETACH DELETE node2delete`.

> Be aware, that these commands do not care about your existing data and may break things. It is advisable to read up the [Neo4J Cypher documentation](https://neo4j.com/docs/developer-manual/3.4/cypher/clauses/delete/#delete-delete-a-node-with-all-its-relationships) and making sure that **all** side effects are understood.

## Installation Instructions

### Requirements

- [Neo4J](https://roboy-memory.readthedocs.io/en/latest/Usage/0_installation.html#local-neo4j-instance)
    - Docker installation highly recommended
- Git
- Maven
- Java 8

### Optional

- ROS
    - Ros Requires Ubuntu 16.04 LTS or Mac OSX
- Redis
- IntelliJ
    - Roboy's IDE of choice

### Installing

#### Installation without Neo4J Tests

In the event that you do not require Neo4J or do not wish that the Neo4J tests execute (ie. situations with no internet connection and only remote instance setup), you can have the Neo4J tests ignored.

Simply append `-D neo4jtest=false` to your maven command.

Example: `mvn clean install -D neo4jtest=false`, `mvn test -D neo4jtest=false`

> This shall disable `org.roboy.memory.util.Neo4jTest` from running

#### Thorough Guide

A detailed guide can be found for the latest version of memory [here](https://roboy-memory.readthedocs.io/en/latest/Usage/0_installation.html).

#### Installing for Usage with roboy_dialog

Simply run `mvn clean install` in the `roboy_dialog` folder, it will build memory and all dependencies for you.

> Note that `Neo4J` must be running while using roboy_dialog. Do not forget to set your environmental variables.

#### Installing Memory Only

> There is little reason to do this anymore. Roboy Dialog automatically builds and installs Memory and there is little reason to run memory without dialog. Only do this if you are absolutly sure that you have to.

1. Set [Environmental Variables](https://roboy-memory.readthedocs.io/en/latest/Usage/1_getting_started.html#configuring-the-package)
2. Install all neccessary dependencies `sudo apt-get install docker.io git maven openjdk-8-jdk`
3. Download and run the Neo4J Docker Image `sudo docker run --publish=7474:7474 --publish=7687:7687 --volume=$HOME/neo4j/data:/data --volume=$HOME/neo4j/logs:/logs neo4j:3.4`
4. Clone the Git Repo `git clone https://github.com/Roboy/roboy_memory -b master`
5. Change directory into roboy memory `cd roboy_memory`
6. Use maven to install roboy memory `mvn clean install`
7. Run memory `java -jar target/roboy_memory-1.0.0-jar-with-dependencies.jar`
