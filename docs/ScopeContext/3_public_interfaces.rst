.. _technical-interfaces:

Public Interfaces
====================

Interfaces to other modules will be realized using ROS (rosjava). Currently 2 interfaces have been designed for communication with Memory Module:

- **write service**: Service called to perform a query writing data into Neo4j database.::

	# argument: String payload
	# returns: Bool success

	rosservice call /roboy/cognition/memory/data/write

- **read service:** Service called to perform a query reading data from Neo4j database.::

	# argument: String payload
	# returns: String data

	rosservice call /roboy/cognition/memory/data/read

The payload has to be defined according to :ref:`roboy-protocol`.
Currently these interfaces are under development. 
