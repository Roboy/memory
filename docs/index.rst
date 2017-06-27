.. Software Documentation template master file, created by
   sphinx-quickstart on Fri Jul 29 19:44:29 2016.
   You can adapt this file completely to your liking, but it should at least
   contain the root `toctree` directive.

Welcome to Roboy Memory Module documentation!
===========================================================

The goal of the project is to provide Roboy with modern graph-based Knowledge Representation. 

Roboy should feature ability to remember information about himself:

- his name
- his age
- his origin
- his location
- his friends

etc.

The same is applicable to Roboy speaking about people who are friends with him. Roboy should tell information about a person or an object and be able to provide basic automatic inference (supported by the graph nature of KR). 
This way, Roboy Memory Module serves as a long-term memory repository of actionable information acquired by other Roboy modules. Persistency layer is presented by a Neo4j graph database.

Upon incoming request, a Java client will pre-process the request and initiate transaction with the database. Two ways of communication between Roboy Java client and Neo4J database are supported: communication using Neo4J driver operating Cypher query language and Neo4J native Java API. Cypher query language offers more flexible querying while communications via Neo4J Java API are implemented as usage-specific routines. 
Interfaces are implemented on top of ros through the Java client.  The input is any type of information Roboy can retrieve from environment abiding by Knowledge Representation reference in format of Roboy Communication Standard protocol, the output are pieces of data related to the requested scope in the same form.

The current main tasks of this project are:

- Fill the memory in with all possible information about Roboy team
- Ensure KR retention (through a population script)
- Finish and evaluate the rosjava service
- Improve KR (more powerful inference)

.. _background_prerequisits:

Relevant Background Information and Pre-Requisits
--------------------------------------------------

A User should be familiar with:

- Knowledge Representation theory
- graph-based KRs
- Roboy Communication Protocol
- Roboy Knowledge Representation Architecture

A Developer should be familiar with:

- graph-based DBs (preferably Neo4j)
- Knowledge Representation theory
- Roboy Communication Protocol
- Roboy Knowledge Representation Architecture
- Java programming language
- Maven automation tool
- rosjava

Reading list for a User:

- Graph Structures for Knowledge Representation and Reasoning proceedings
- `rosjava Documentation <http://wiki.ros.org/rosjava>`_
- :ref:`roboy-protocol`

Reading list for a Developer:

- `OReilys Graph Databases <https://goo.gl/C18NpT>`_
- `Neo4j Getting Started <https://neo4j.com/developer/get-started/>`_
- `Cypher RefCard <https://neo4j.com/docs/cypher-refcard/current/>`_
- `Java Documentation <http://www.oracle.com/technetwork/topics/newtojava/new2java-141543.html>`_
- `Maven Documentation <https://maven.apache.org/index.html>`_
- :ref:`roboy-protocol`
- `rosjava Documentation <http://wiki.ros.org/rosjava>`_


.. _requirements_overview:

Requirements Overview
---------------------

The **software requirements** define the system from a blackbox/interfaces perspective. They are split into the following sections:

- **User Interfaces** - :ref:`user-interfaces`
- **Technical Interfaces** - :ref:`technical-interfaces`
- **Runtime Interfaces and Constraints** - :ref:`runtime_interfaces`



Contents:
---------------------

.. _usage:
.. toctree::
  :maxdepth: 1
  :glob:
  :caption: Usage and Installation

  Usage/*

.. _ScopeContext:
.. toctree::
  :maxdepth: 1
  :glob:
  :caption: Interfaces and Scope

  ScopeContext/*

.. _development:
.. toctree::
  :maxdepth: 1
  :glob:
  :caption: Development

  development/*

.. toctree::
   :maxdepth: 1

   about-arc42
