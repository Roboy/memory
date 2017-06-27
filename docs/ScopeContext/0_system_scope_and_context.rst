Context
--------

The Memory Module receives input from other Cognition modules in form of ROS messages containing RCS payload which is then parsed internally. RCS payload contains valid request, otherwise exeption would be raised and Memory Module would answer with "FALSE" or NULL value.

The main output of the Memory Module is either a single piece of data or id of facial feature representation or voice signature. 

This is illustrated in the following context overview:

.. _context_within_environment:
.. figure:: images/uml_system_context.*
  :alt: Bulding blocks overview

  UML System Context

  **UML-type context diagram** - shows the birds eye view of the system (black box) described by this architecture within the ecosystem it is to be placed in. Shows orbit level interfaces on the user interaction and component scope.
