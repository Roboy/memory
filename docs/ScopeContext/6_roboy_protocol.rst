.. _roboy-protocol:

Roboy Communication Standard
================================

Roboy Communication Standard is a proposal on decorating standard ros messages with 
JSON-like payload.::

	Header header
	string payload

**Payload**
	
A person with **someID** says: My friend **friend_name** is a friend of Roboy::

	"someID": {
	  "GET": {
		"node": {"this", "friend_name"}
	  }
	}
	
	"friendID": {
	  "POST": {
		"relation": {"this": "Roboy"}
	  }
	}

.. todo::
	Create a list of appropriate messages with regard to Knowledge Representation v.1.1
	They should implement READ and WRITE capabilities.
