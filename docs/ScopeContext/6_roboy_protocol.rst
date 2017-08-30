.. _roboy-protocol:

Roboy Communication Standard
================================

Roboy Communication Standard is a proposal on decorating standard ros messages with 
JSON-like payload.::

	Header header
	String payload
	—-
	String answer

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

Where node type is Person, and relation type is FRIEND_OF.

There we have to get the node (id) of a friend which name corresponds to the name given, that is performed by the query initialized with the first example of the payload.
After recieving the correspondent node (id) we can relate the node (person) with Roboy as friends, that is performed by the query initialized with the second example of the payload.




