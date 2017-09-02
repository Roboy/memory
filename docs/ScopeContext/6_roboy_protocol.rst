.. _roboy-protocol:

Roboy Communication Standard
================================

Roboy Communication Standard is a proposal on decorating standard ros messages with JSON-like payload.

Create Queries Payload Message
-----------------------------------------------

Creating a node:

.. code-block:: javascript

    {
        'label': 'some_label',
        'faceVector': [float, ..., float] // Under comsideration, OPTIONAL
        'properties': {
            'prop_a': 'value_a',
            'prop_b': 'value_b'
        }
    }

This query requests creating node with **label** - some_label, **prop_a** having **value_a** and **prop_b** - **value_b**.
The **faceVector** contains face features for the node with specified label **Person** (applicable only to nodes of this label).

There the label may be:

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
- Occupation
- Object (which Roboy can detect/interact with)

Properties other than 'name' are not required on the creation and may be omitted. Later the node's properties may be updated by an update query.
The query returns the **ID** of the created node on success. The **faceVector** is fed into Redis if present.
The named properties and allowed values may be found in :ref:`know_rep`.

Update Queries Payload Message
-----------------------------------------------

Updating a node

.. code-block:: javascript

    {
        'id': 1, //REQUIRED, contains node id

        'relations':  {
            'rel_a': [2, 3],
            'rel_b': [3]
        }

        'properties': {
            'prop_a': 'value_a',
            'prop_b': 'value_b'
        }
    }

This query requests updating node with **ID** - 1.
This query requests creating relations between two nodes, where the relations are e.g. **rel_a**, the number denotes the **ID** of the node to where the relations is following from the current node.
This query requests creating (changing) properties of the node, where the properties may be e.g. **prop_a** with value **value_a**.

.. warning::

    You should be aware of the node label.

The query returns the **OK** message on success.
The named properties and allowed values may be found in :ref:`know_rep`.

Get Queries Payload Message
-----------------------------------------------

**Get nodes IDs**

.. code-block:: javascript

    {
        'label': 'some_label',

        'relations': {
            'rel_a': [2],
            'rel_b': [3]
        },

        'properties': {
            'prop_a': 'value'
        }
    }

This query requests getting all nodes which have node label - **some_label**, have relationship **rel_a** with the node having **ID** 2 and **rel_b** with the node of **ID** 3, as well as having **prop_a** equal to **value**.
The query returns an array of node IDs on success (may be an empty array if no such nodes exist).
The allowed relation types for each pair of nodes and named properties of nodes may be found in :ref:`know_rep`.

**Get node by ID**

.. code-block:: javascript

    {
        'id': 1
    }

This query requests getting all information about a node with respective **ID**.
The query returns a JSON containing all information about the node on success (may be an empty string if no such node exist).

.. warning::

    You should be aware of the node label.

The respective information about what could be returned may be found in :ref:`know_rep`.

Remove Queries Payload Message
-----------------------------------------------

Remove properties and relations of the nodes

.. code-block:: javascript

    {
        'id': 1,

        'relations': {
            'rel_a': [2],
            'rel_b': [3]
        },

        'properties': {
            'prop_a'
        }
    }

This query requests removing all respective properties and relations with regard to the node with **ID** = 1: relationships **rel_a**
with the node having **ID** = 2 and **rel_b** with the node having **ID** = 3, as well as property **prop_a**.

.. warning::

    You should be aware of the node label.

The query returns the **OK** message on success.
The named properties and allowed values may be found in :ref:`know_rep`.
