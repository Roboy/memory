.. _technical-interfaces:

Public Interfaces (ROS)
=======================

Interfaces to other modules will be realized using ROS (rosjava). Currently 2 interfaces have been designed for communication with Memory Module:

- **write service**: Service called to perform a query writing data into Neo4j database.::

	# argument: String header String payload
	# returns: String answer

	rosservice call /roboy/cognition/memory/create

- **read service:** Service called to perform a query reading data from Neo4j database.::

	## argument: String header String payload
	# returns: String answer

	rosservice call /roboy/cognition/memory/update

The payload has to be defined according to :ref:`roboy-protocol`.
Currently these interfaces are under development. 

.. todo::
describe more precisely

    Header header
	String payload
	—-
	String answer


.. _initial_experience:


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

Consider :ref:`roboy_protocol` for the correct use use of properties, relationships and labels.

- 'label' specifies the class of node in the knowledge graph
- 'id' of a node is a unique number specified for each node that may be accessed be searched or modified in the knowledge graph
- 'relations' comprise a map of relationship types with an array of node ids for each of them, providing multiple relationships tracing
- 'properties' = A map of property keys with values



Using the ROS services
================================

There you can find basic examples on how to access the memory with JSON-formed queries using ROS.

Available ROS services
--------------------------------------------------

The Roboy Memory Module offers the next services in order to work with the memory contents:

- create - creates a node in the Neo4j DB with provided properties and face features (Redis)
- update - adds new relationships between specified nodes or properties to the specified node
- get - retrieves information about the specified node or returns IDs of all nodes which fall into the provided conditions
- remove - removes properties or relationships from the specified node

Calling the ROS
--------------------------------------------------

**General syntax of ROS messages**::

    rosservice call /roboy/cognition/memory/--service_name-- "\"---Header---\"" "\"---Payload---\""

**Example Header:**

The header consists of a timestamp ('datetime') in seconds since 1.1.1970 and the module which is sending the query ('user').


**Payload:**

(Consider the Memory Neo4j Architecture Wiki for right use of properties, relationships and labels)

'label' = Specifies the type of node that shall be created

'id' = The id of a node that shall be searched or modified

'properties' = A map of property keys with values

'relations' = A map of relationship types with an array of node ids

Create queries
--------------------------------------------------

**Create a node of the type 'Person' with properties**::

    rosservice call /roboy/cognition/memory/create "\"{
        'user':'vision',
        'datetime':'1234567'
    }\"" "\"{
        'type':'node',
        'label':'Person',
        'properties':{
            'name':'Lucas',
            'sex':'male'
        }
    }\""

**Answer:**  {'id': 160}        - //ID of the created node

**Errors messages:**

{status:"FAIL", message:"no properties"}

{status:"FAIL", message:"no name specified in properties : name required"}

{status:"FAIL", message:"Label 'Xyz' doesn't exist in the DB"}

Update queries
--------------------------------------------------

**Add properties to the node with id 15**::

    rosservice call /roboy/cognition/memory/update "\"{
        'user':'vision',
        'datetime':'1234567'
    }\"" "\"{
        'type':'node',
        'id':15,
        'properties':{
            'surname':'Ki',
            'xyz':'abc'
        }
    }\""

**Add relations to the node with id 15**::

    rosservice call /roboy/cognition/memory/update "\"{
        'user':'vision',
        'datetime':'1234567'
    }\"" "\"{
        'type':'node',
        'id':15,
        'relations':{
            'LIVE_IN':[28,23],
            'STUDY_AT':[16]
        }
    }\""

**Add properties + relations to the node with id 15**::

    rosservice call /roboy/cognition/memory/update "\"{
        'user':'vision',
        'datetime':'1234567'
    }\"" "\"{
        'type':'node',
        'id':15,
        'properties':{
            'surname':'Ki', 'xyz':123
        },
        'relations':{
            'LIVE_IN':[28,23],
            'STUDY_AT':[16]
        }
    }\""

**Answer:** {status:"OK"}

**Errors message:**

{status:"FAIL", message:"The relationship type 'XYZ' doesn't exist in the DB"}

Get queries
--------------------------------------------------

**Get properties and relationships of a node by id**::

    rosservice call /roboy/cognition/memory/get "\"{
        'user':'vision',
        'datetime':'1234567'
    }\"" "\"{
        'id':15
    }\""

**Answer:**::

    {
        'id': 15,
        'labels': ["person"],
        'properties': {
            "birthdate":"01.01.1970",
            "surname":"ki",
            "sex":"male",
            "name":"lucas"
        },
        'relations': {
            "from":[28],
            "friend_of":[124, 4, 26, 104, 106, 71, 96, 63],
            "member_of":[20], "study_at":[16], "is":[17],
            "has_hobby":[18],
            "live_in":[23, 28]
        }
    }


**Get ids of nodes which have all specified labels, relations and/or properties**::

    rosservice call /roboy/cognition/memory/get "\"{
        'user':'vision',
        'datetime':'1234567'
    }\"" "\"{
        'label':'Person',
        'relations':{
            'FRIEND_OF':[15]
        },
        'properties':{
            'name':'Laura'
        }
    }\""

**Answer:** {'id':[96]}     - //a vector with all fitting IDs

Remove queries
--------------------------------------------------

**Remove properties of node 15**::

    rosservice call /roboy/cognition/memory/remove "\"{
        'user':'vision',
        'datetime':'1234567'
    }\"" "\"{
        'type':'node',
        'id':15,
        'properties':['birthdate','surname']
    }\""

**Remove relations of node 15**::

    rosservice call /roboy/cognition/memory/remove "\"{
        'user':'vision','datetime':'1234567'
    }\"" "\"{
        'type':'node',
        'id':15,
        'relations':{
            'LIVE_IN':[28,23],
            'STUDY_AT':[16]
        }
    }\""

**Remove properties and relations of node 15**::

    rosservice call /roboy/cognition/memory/remove "\"{
        'user':'vision',
        'datetime':'1234567'
    }\"" "\"{
        'type':'node',
        'id':15,
        'properties':['birthdate','surname'],
        'relations':{
            'LIVE_IN':[23]
        }
    }\""

**Answer:** {status:"OK"}

