Troubleshooting
===============

**Error**: no DB connection::

    Exception in thread "pool-1-thread-16" org.neo4j.driver.v1.exceptions.ServiceUnavailableException: Unable to connect to 127.0.0.1:7687, ensure the database is running and that there is a working network connection to it.

Check if the DB is up and the Neo4j address in configuration is stated properly.

