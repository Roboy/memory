Troubleshooting
===============

Possible Common Exceptions
--------------------------------------------------

**No ROS master connection**::

    org.ros.internal.node.client.Registrar callMaster
    SEVERE: Exception caught while communicating with master.
    java.lang.RuntimeException: java.net.ConnectException: Host is down

Check if the roscore master PC is connected to the network or master URI in configuration is stated properly.

**No roscore running on ROS master**::

    org.ros.internal.node.client.Registrar callMaster
    SEVERE: Exception caught while communicating with master.
    java.lang.RuntimeException: java.net.ConnectException: Connection refused

Check if roscore is up on the master PC or master URI in configuration is stated properly.

**Host PC is not reachable from ROS master**::

    ERROR: Unable to communicate with service [/roboy/cognition/memory/get],
    address [rosrpc://127.0.0.1:51734/]

Check if hostname for ROS publisher (current PC) in configuration is stated properly.

**No service is running on host from ROS master**::

    ERROR: transport error completing service call:
    unable to receive data from sender, check sender's logs for details.

Check if the package is running and services were successfully published (on current PC).

**No Neo4j connection**::

    Exception in thread "pool-1-thread-16" org.neo4j.driver.v1.exceptions.ServiceUnavailableException:
    Unable to connect to 127.0.0.1:7687, ensure the database is running and that there is a working network connection to it.

Check if Neo4j is up and the Neo4j address in configuration is stated properly.

**Neo4j credentials are incorrect**::

    Exception in thread "pool-1-thread-16" org.neo4j.driver.v1.exceptions.AuthenticationException:
    The client is unauthorized due to authentication failure.

Check if Neo4j credentials in configuration are stated properly.

**No Redis connection**::

    Exception in thread "pool-1-thread-33" redis.clients.jedis.exceptions.JedisConnectionException:
    java.net.UnknownHostException: 127.0.0.1

Check if Redis is up and the Redis address in configuration is stated properly.

**Redis credentials are incorrect**::

    Exception in thread "pool-1-thread-16" redis.clients.jedis.exceptions.JedisDataException:
    ERR invalid password

Check if Redis credentials in configuration are stated properly.

**Missing parenthesis**::

    Exception in thread "pool-1-thread-13" com.google.gson.JsonSyntaxException:
    java.io.EOFException: End of input at line 1 column 38 path $.datetime

Check JSON "{}" parenthesis in query.

**JSON index is present, but value is not**::

    Exception in thread "pool-1-thread-24" com.google.gson.JsonSyntaxException:
    com.google.gson.stream.MalformedJsonException: Expected value at line 1 column 33 path $.properties

Check if any value in JSON query is missing.

**JSON query is formed incorrectly**::

    Exception in thread "pool-1-thread-18" com.google.gson.JsonSyntaxException:
    com.google.gson.stream.MalformedJsonException: Unterminated string at line 1 column 9 path $.

Check if JSON is formed properly: quotes, parenthesis. Refer to :ref:`roboy-protocol:`

**Primitives are initialized with complex types in JSON query**::

    Exception in thread "pool-1-thread-14" com.google.gson.JsonSyntaxException:
    java.lang.IllegalStateException: Expected an int but was BEGIN_ARRAY at line 1 column 8 path $.id

    Exception in thread "pool-1-thread-22" com.google.gson.JsonSyntaxException:
    java.lang.IllegalStateException: Expected a string but was BEGIN_ARRAY at line 1 column 11 path $.label

    Exception in thread "pool-1-thread-22" com.google.gson.JsonSyntaxException:
    java.lang.IllegalStateException: Expected a string but was BEGIN_OBJECT at line 1 column 11 path $.label

Check if the JSON query is type valid: JSON array instead of object is recieved. Change the respective values. Refer to :ref:`roboy-protocol:`.

**Complex types are initialized with primitive types in JSON query**::

    Exception in thread "pool-1-thread-21" com.google.gson.JsonSyntaxException:
    java.lang.IllegalStateException: Expected BEGIN_ARRAY but was STRING at line 1 column 35 path $.properties[0]

Check if the JSON query is type valid: primitive objects instead of JSON arrays are recieved. Change the respective values. Refer to :ref:`roboy-protocol:`.

**Wrong complex type is applied on initialization in JSON query**::

    Exception in thread "pool-1-thread-22" com.google.gson.JsonSyntaxException:
    java.lang.IllegalStateException: Expected BEGIN_ARRAY but was BEGIN_OBJECT at line 1 column 11 path $.label

    Exception in thread "pool-1-thread-22" com.google.gson.JsonSyntaxException:
    java.lang.IllegalStateException: Expected BEGIN_OBJECT but was BEGIN_ARRAY at line 1 column 11 path $.label

Check if the JSON query is type valid: JSON object instead of JSON array and vice versa are recieved. Change the respective values. Refer to :ref:`roboy-protocol:`.



