# Roboy Memory Module

Documentation can be found here:

[![Documentation Status](http://roboy-memory.readthedocs.io/badge/?version=latest)](http://roboy-memory.readthedocs.io/latest/?badge=latest)

[![Documentation Status](http://roboy-memory.readthedocs.io/badge/?version=docs)](http://roboy-memory.readthedocs.io/en/docs/?badge=docs)

## What is it?

This repository contains a memory module developed to use for Roboy (roboy.org), humanoid robot.
The goal of the project is to provide Roboy with modern graph-based Knowledge Representation.

Roboy should feature ability to remember information about himself: his name, his age, his origin, his location, his friends,
etc.

The same is applicable to Roboy speaking about people who are friends with him. Roboy should tell information about a person or an object and be able to provide basic automatic inference (supported by the graph nature of KR). This way, Roboy Memory Module serves as a long-term memory repository of actionable information acquired by other Roboy modules. Persistency layer is presented by a Neo4j graph database.

## How does it work?

Upon incoming request, a Java client will pre-process the request and initiate transaction with the database. Two ways of communication between Roboy Java client and Neo4J database are supported: communication using Neo4J driver operating Cypher query language and Neo4J native Java API. Cypher query language offers more flexible querying while communications via Neo4J Java API are implemented as usage-specific routines. Interfaces are implemented on top of ros through the Java client. The input is any type of information Roboy can retrieve from environment abiding by Knowledge Representation reference in format of Roboy Communication Standard protocol, the output are pieces of data related to the requested scope in the same form.

 
