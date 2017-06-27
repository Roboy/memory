Cypher Examples
================================

Useful Cypher queries related to actual Knowledge Representation

**Create a „location“-node**::

	CREATE (n:Location {name: "Munich"})

**Add a 2nd Lable (Organization) to a Node**::

	match (n:Company)
	set n:Organization
	return n

**Create a relationship**

if relationship type is not existing yet::

	MATCH (a:Person),(b:City) WHERE a.name = 'Lucas' AND b.name = 'Frankfurt' CREATE (a)-[r:FROM]->(b) RETURN r

if relationship type is existing:::

	MATCH (a:Country),(b:Continent) WHERE a.name = 'Germany' AND b.name = 'Europe' Merge (a)-[r:IS_IN]->(b) RETURN r


**Delete**

all „location“-Nodes::
	
	MATCH (n:Location) DETACH Delete n

a specific Node by ID::

	MATCH (n:Person) where ID(n)=13 DELETE n

all relationships from Roboy::
	
	MATCH (n:Robot { name: 'Roboy' })-[r:FRIEND_OF]->() DELETE r


**Add Properties**::

	Match (n:Object {name: 'Ball'}) 
	Set n.color = 'red' 
	Set n.price_euro = 15 
	Set n.usage = ["playing", "trowing", "rolling"]
	Return n

**Show**

all nodes with relationships::
	
	MATCH (n) RETURN n;

the database scheme::

	CALL db.schema()
