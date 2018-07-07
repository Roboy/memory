.. _initial_experience:

Using roboy_memory
================================

As part of the restructuring of dialog, the ROS connection between Dialog and Memory has been removed. Instead, one can now use memory as a static library. We still retain the ability to start the ROS services if the need arises, however it is not recommendable due to efficiency reasons. 

Availible Operations
-----------------------------------------------

.. deprecated:: 1.1

    The Roboy Memory Module offers the next services in order to work with the memory contents:

        - create - creates a node in the Neo4j DB with provided properties and face features (Redis)
        - update - adds new relationships between specified nodes or properties to the specified node
        - get - retrieves information about the specified node or returns IDs of all nodes which fall into the provided conditions
        - remove - removes properties or relationships from the specified node


Using Direct Function calls
================================

One can now simply call the functions::

    create(String request)
    update(String request)
    get(String request)
    remove(String request)

These functions are located in ``org.roboy.memory.util.MemoryOperations`` and can be called, as long as a NEO4J is running at the points specified in your environmental variables (see getting started). 

The functions take JSON-formed queries as parameters. There is no need for a header in this case, all one needs to do is to send the payload. 

Create queries
--------------------------------------------------

**Create a node of the type 'Person' with properties**::

    MemoryOperations.create("{'type':'node','label':'Person','properties':{'name':'Lucas','sex':'male'}}");

On success you will get:

**Answer:**  {'id': x } - //ID of the created node

On error you will get:

**Error:** {status:"FAIL", message:"error message"}

You can find detailed information in :ref:`technical-interfaces`

Update queries
--------------------------------------------------

**Add properties to the node with id 15**::

    MemoryOperations.update("{ 'type':'node', 'id':15, 'properties':{ 'surname':'Ki', 'xyz':'abc' } }");


**Add relationships to the node with id 15**::

    MemoryOperations.update("{'type':'node','id':15,'relationships':{'LIVE_IN':[28,23],'STUDY_AT':[16]}}"

**Add properties + relationships to the node with id 15**::

    "{'type':'node','id':15,'properties':{'surname':'Ki', 'xyz':123},'relationships':{'LIVE_IN':[28,23],'STUDY_AT':[16]}}"

On success you will get:

**Answer:** {status:"OK"}

On error you will get:

**Error:** {status:"FAIL", message:"error message"}

You can find detailed information in :ref:`technical-interfaces`

Get queries
--------------------------------------------------

**Get properties and relationships of a node by id**::

    MemoryOperations.get("{'id':15}");

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
        'relationships': {
            "from":[28],
            "friend_of":[124, 4, 26, 104, 106, 71, 96, 63],
            "member_of":[20], "study_at":[16], "is":[17],
            "has_hobby":[18],
            "live_in":[23, 28]
        }
    }

**Get ids of nodes which have all specified labels, relationships and/or properties**::

    MemoryOperations.get("{'label':'Person','relationships':{'FRIEND_OF':[15]},'properties':{'name':'Laura'}}");

On success you will get:

**Answer:** {'id':[x]}     - an array with all fitting IDs

On error you will get:

**Error:** {status:"FAIL", message:"error message"}

You can find detailed information in :ref:`technical-interfaces`

Remove queries
--------------------------------------------------

.. warning::

    Please, do not try running **remove** queries without considering significant risks. Be responsible!

**Remove properties of node 15**::

    MemoryOperations.remove("{'type':'node','id':15,'properties':['birthdate','surname']}");

**Remove relationships of node 15**::

    MemoryOperations.remove("{'type':'node','id':15,'relationships':{'LIVE_IN':[28,23],'STUDY_AT':[16]}}");

**Remove properties and relationships of node 15**::

    MemoryOperations.remove("{'type':'node','id':15,'properties':['birthdate','surname'],'relationships':{'LIVE_IN':[23]}}");

On success you will get:

**Answer:** {status:"OK"}

On error you will get:

**Error:** {status:"FAIL", message:"error message"}

Using ROS
================================

.. deprecated:: 1.1

    Using ROS is deprecated

There you can find basic examples on how to access the memory with JSON-formed queries using ROS.
For more information, please, refer to :ref:`technical-interfaces`, :ref:`know_rep` and :ref:`roboy-protocol`.

To start the ROS services, simply run the Main class' Main method.

Verifying ROS services are active
--------------------------------------------------

In order to check available services, in your catkin environment, run::

    rosservice list

You should get the next output::

    /roboy/cognition/memory/create
    /roboy/cognition/memory/cypher
    /roboy/cognition/memory/get
    /roboy/cognition/memory/remove
    /roboy/cognition/memory/update
    /rosout/get_loggers
    /rosout/set_logger_level

Calling the ROS
--------------------------------------------------

**General syntax for a ROS message**::

    rosservice call /roboy/cognition/memory/--service_name-- "\"---header---\"" "\"---payload---\""

**Sample Header:**

The header (JSON object) consists of a timestamp and the module which is sending the query ('user'):
You may try using the next header for your initial experience.

.. code-block:: javascript

    {
        'user':'test',
        'datetime':'0'
    }

**Payload Elements:**

The payload (JSON object) may comprise several elements such as:

- 'label' specifies the class of node in the knowledge graph
- 'id' of a node is a unique number specified for each node that may be accessed be searched or modified in the knowledge graph
- 'relationships' comprise a map of relationship types with an array of node IDs for each of them, providing multiple relationships tracing
- 'properties' = A map of property keys with values

Consider :ref:`roboy-protocol` for the correct use use of properties, relationships and labels.
Sample payloads as well as the whole structure of the calls are mentioned below.

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

On success you will get:

**Answer:**  {'id': x } - //ID of the created node

On error you will get:

**Error:** {status:"FAIL", message:"error message"}

You can find detailed information in :ref:`technical-interfaces`

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

**Add relationships to the node with id 15**::

    rosservice call /roboy/cognition/memory/update "\"{
        'user':'vision',
        'datetime':'1234567'
    }\"" "\"{
        'type':'node',
        'id':15,
        'relationships':{
            'LIVE_IN':[28,23],
            'STUDY_AT':[16]
        }
    }\""

**Add properties + relationships to the node with id 15**::

    rosservice call /roboy/cognition/memory/update "\"{
        'user':'vision',
        'datetime':'1234567'
    }\"" "\"{
        'type':'node',
        'id':15,
        'properties':{
            'surname':'Ki', 'xyz':123
        },
        'relationships':{
            'LIVE_IN':[28,23],
            'STUDY_AT':[16]
        }
    }\""

On success you will get:

**Answer:** {status:"OK"}

On error you will get:

**Error:** {status:"FAIL", message:"error message"}

You can find detailed information in :ref:`technical-interfaces`

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
        'relationships': {
            "from":[28],
            "friend_of":[124, 4, 26, 104, 106, 71, 96, 63],
            "member_of":[20], "study_at":[16], "is":[17],
            "has_hobby":[18],
            "live_in":[23, 28]
        }
    }

**Get ids of nodes which have all specified labels, relationships and/or properties**::

    rosservice call /roboy/cognition/memory/get "\"{
        'user':'vision',
        'datetime':'1234567'
    }\"" "\"{
        'label':'Person',
        'relationships':{
            'FRIEND_OF':[15]
        },
        'properties':{
            'name':'Laura'
        }
    }\""

On success you will get:

**Answer:** {'id':[x]}     - an array with all fitting IDs

On error you will get:

**Error:** {status:"FAIL", message:"error message"}

You can find detailed information in :ref:`technical-interfaces`

Remove queries
--------------------------------------------------

.. warning::

    Please, do not try running **remove** queries without considering significant risks. Be responsible!

**Remove properties of node 15**::

    rosservice call /roboy/cognition/memory/remove "\"{
        'user':'vision',
        'datetime':'1234567'
    }\"" "\"{
        'type':'node',
        'id':15,
        'properties':['birthdate','surname']
    }\""

**Remove relationships of node 15**::

    rosservice call /roboy/cognition/memory/remove "\"{
        'user':'vision','datetime':'1234567'
    }\"" "\"{
        'type':'node',
        'id':15,
        'relationships':{
            'LIVE_IN':[28,23],
            'STUDY_AT':[16]
        }
    }\""

**Remove properties and relationships of node 15**::

    rosservice call /roboy/cognition/memory/remove "\"{
        'user':'vision',
        'datetime':'1234567'
    }\"" "\"{
        'type':'node',
        'id':15,
        'properties':['birthdate','surname'],
        'relationships':{
            'LIVE_IN':[23]
        }
    }\""

On success you will get:

**Answer:** {status:"OK"}

On error you will get:

**Error:** {status:"FAIL", message:"error message"}

You can find detailed information in :ref:`technical-interfaces`

