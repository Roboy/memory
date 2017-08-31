.. _initial_experience:

ROS Messages For Memory
================================

This Wiki gives examples on how to query the Neo4J-DB over ROS with JSON.


**Syntax of every query:**

rosservice call /roboy/cognition/memory/---Service--- "\"{---Header---}\"" "\"{---Payload---}\""


**Available ROS services:**

create           //create a node
update           //add relationships or properties to a node
get              //get infos about nodes or find a node
remove           //remove properties or relationships of a node


**Header:**

The header consists of a timestamp ('datetime') in seconds since 1.1.1970 and the module which is sending the query ('user').

**Payload:**

Consider :ref:`roboy_protocol` for the correct use use of properties, relationships and labels.

- 'label' specifies the class of node in the knowledge graph
- 'id' of a node is a unique number specified for each node that may be accessed be searched or modified in the knowledge graph
- 'relations' comprise a map of relationship types with an array of node ids for each of them, providing multiple relationships tracing
- 'properties' = A map of property keys with values
