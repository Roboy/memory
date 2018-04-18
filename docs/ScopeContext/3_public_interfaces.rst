.. _technical-interfaces:

Public Interfaces (ROS)
=======================

Interfaces to other modules are realized through ROS (rosjava).
Currently 5 interfaces (ROS services) have been designed for communication with Memory Module.

ROS Services
--------------------------------------------------

All calls are compliant to this general form::

    rosservice call /roboy/cognition/memory/---service_name--- "\"---header---\"" "\"---payload---\""

- **create service**: Service called to perform a query writing data into Neo4j database.::

	# argument: String header String payload
	# returns: String answer

	rosservice call /roboy/cognition/memory/create

- **get service:** Service called to perform a query reading data from Neo4j database.::

	## argument: String header String payload
	# returns: String answer

	rosservice call /roboy/cognition/memory/get

- **update service:** Service called to perform a query altering data in Neo4j database.::

	## argument: String header String payload
	# returns: String answer

	rosservice call /roboy/cognition/memory/update

- **remove service:** Service called to perform a query deleting data from Neo4j database.::

	## argument: String header String payload
	# returns: String answer

	rosservice call /roboy/cognition/memory/remove

- **cypher service:** Service called to perform any Cypher query in Neo4j database.::

	## argument: String header String payload
	# returns: String answer

	rosservice call /roboy/cognition/memory/cypher

For the first 4 services the payload has to be defined according to :ref:`roboy-protocol`.

**Payload Elements:**

- 'label' specifies the class of node in the knowledge graph
- 'id' of a node is a unique number specified for each node that may be accessed be searched or modified in the knowledge graph
- 'relationships' comprise a map of relationship types with an array of node ids for each of them, providing multiple relationships tracing
- 'properties' = A map of property keys with values

Each of this element is peculiar to respective service payload.

The Cypher service uses a well-formed query in Cypher as the payload, see :ref:`cypher`.

Responses
--------------------------------------------------

**Create query** provides the following responses.

Success:::

    {
        'id': x
    }

Failure:

- some properties are not specified properly::

    {
        status:"FAIL",
        message:"no properties"
    }

- when creating a node, the name property is obligatory, name is missing::

    {
        status:"FAIL",
        message:"no name specified in properties : name required"
    }

- trying to create a node with a non-existing label, see :ref:`know_rep`::

    {
        status:"FAIL",
        message:"Label 'Xyz' doesn't exist in the DB"
    }

**Update query** provides the following responses.

Success:::

    {
        status:"OK"
    }

Failure:

- trying to create a relationship with a non-existing type, see :ref:`know_rep`::

    {
        status:"FAIL",
        message:"The relationship type 'XYZ' doesn't exist in the DB"
    }

**Get query** provides the following responses.

Success:

- getting by ID::

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

- getting IDs::

    {
        'id':[x, y]
    }

**Remove query** provides the following responses.

Success:::

    {
        status:"OK"
    }

