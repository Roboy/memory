.. _initial_experience:

ROS messages for memory
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
(Consider the Memory Neo4j Architecture Wiki for right use of properties, relationships and labels)

'label' = Specifies the type of node that shall be created
'id' = The id of a node that shall be searched or modified
'properties' = A map of property keys with values
'relations' = A map of relationship types with an array of node ids