Solution Strategy
=================

**Basic decisions for Memory Module:**

- Seperation of concern through decoupling request processing and a persistency layer.
- Iterative and incremental development is adopted.
- Highest priority is Knowledge Representation implementation to satisfy the requirements and abilities for Dialog. Roboy Communication Standard is of the second priority as it follows the KR structure. The following priority is providing other modules with actual client and interfaces for the usage. 
- For Knowledge Representation, a graph-based approach was chosen. Thus the persistency layer is presented by Neo4j graph database.
- Client for request processing is implemented on top of rosjava.

**Current implementation:**

- Graph-based Knowledge Representation ver. 1.1 on remote server.
- Roboy Communication Standard commands pool.
- Basic client software.

Motivation
----------------

The motivation to use a graph-based approach was easier (and probably more obvious) maintainance of relations and basic inference contained in graph-models by definition.

Java was the choice for development because it is Neo4j native language, thus has better support. 

Choice of rosjava was forced by both the usage of Java and ros as means of communication between Roboy parts.

Roboy Communication Standard was introduced to make querying more human-readable and graceful.
