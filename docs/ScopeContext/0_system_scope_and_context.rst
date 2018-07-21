Context
--------

.. deprecated:: 1.1
    The Memory Module receives input from other Cognition module in form of ROS messages containing RCS payload which is
    then parsed internally.

The Memory Module receives input from other Cognition module in form of JSON string containing RCS payload which is then parsed internally.
RCS payload contains valid request, otherwise exception would be raised and Memory Module would answer with "FAIL" and error message.

The main output of the Memory Module is either a single piece of data (JSON object) or set of **IDs**.

The context of Roboy Memory Module illustrated in the following diagram:

.. _context_within_environment:
.. figure:: images/uml_system_context.*
  :alt: Building blocks overview

  UML System Context

  **UML-type context diagram** - shows the birds eye view of the system (black box) described by this architecture within the ecosystem it is to be placed in. Shows orbit level interfaces on the user interaction and component scope.
