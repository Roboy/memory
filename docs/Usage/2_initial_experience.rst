.. _initial_experience:

Usage of the ROS services
================================

This Wiki gives examples on how to query the Neo4J-DB over ROS with JSON.


Available ROS services
--------------------------------------------------


- create - create a node

- update - add relationships or properties to a node

- get - get infos about nodes or find a node

- remove - remove properties or relationships of a node



Comosition of Memory-ROS messages
--------------------------------------------------

**Syntax of every query:**

rosservice call /roboy/cognition/memory/---Service--- "\"{---Header---}\"" "\"{---Payload---}\""


**Header:**

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

**Answer:**

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

