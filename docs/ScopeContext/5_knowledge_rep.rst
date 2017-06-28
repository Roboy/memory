Knowledge Representation
================================

Architecture of the Neo4j database in remote.

.. figure:: images/dbvisual.*

Visualization of a DB scheme.

Node Classes
--------------------------------
- Person
- Robot
- Organization
  
  a. Company
  b. University

- Location
  
  a. City
  b. Country
  c. Continent

- Hobby
- Type

  a. Occupation

- Object (which Roboy can detect/interact with)
  
Edge Classes
--------------------------------
**(Person, Robot : Person, Robot)**

- FRIEND_OF
  
**(Person, Robot : Location)**

- LIVE_IN
- FROM

**(Person : Organization)**

- WORK_FOR
- STUDY_AT
- MEMBER_OF

**(Person, Robot : Hobby)**

- HAS_HOBBY

**(Person, Robot : Object)**

- KNOW

**(Object, Robot, Person, Organization : Type)**

- IS

**(Organization, Robot : Organization)**

- PART_OF

**(Organization, Location : Location)**

- IS_IN


Property Keys
--------------------------------

**General**

Describes non-specific prameters for any node

- name [string]
- id [int]

**Person**

Describes prameters specific to a person

- surname [string]
- birthdate [String]
- sex [string]
- face_id (facial features) [int]: reference to a face representation
- voice_id (voice signature) [int]: reference to a voice signature
- conversation_id (Topic (scope) of the last conversation) [int]: reference to a topic marker for the last conversation

**Roboy**

Describes prameters specific to Roboy

- birthdate [string]
- abilities [list of strings]
- skills [list of strings]

**Object**

Describes prameters specific to objects

- color [string]
- speed [int]
- price [int]
- temperature [int]
- usage [list of strings]