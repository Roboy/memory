.. _technical-interfaces:

Public Interfaces
====================

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