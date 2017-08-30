.. _getting-started:

Getting started
===============


Local Neo4j Instance
--------------------------------------------------

Before proceeding further, please commence a user configuration step:

- please navigate inside the package folder **$ROBOY_MEMORY** to::
	
	cd scripts

- run::
	
	./user_conf.sh -u your_username -p your_password

- wait the script to execute.

To start using your local Neo4j instance:

- please navigate inside the package folder **$ROBOY_MEMORY** to::
	
	cd scripts

- run (username and password should be the same as in the previous step)::
	
	./fetch_db.sh -u your_username -p your_password

- wait the script to execute.

This would populate the database with the initial Knowledge Representation data.


Remote Neo4j Instance
--------------------------------------------------

To use a remote intance of Neo4j containing the most recent Knowledge Representation, ensure your connectivity to the Roboy server.
If the server is up, use the roboy_memory package in the remote mode (default).
For this, please use a password related to your specific user:

- user, a generic Roboy member
- dialog, a dialog team member
- vision, a vision team member
- memory, a memory team member (developer)


Package Running
--------------------------------------------------

For using roboy_memory package properly, please update the configuration file with the username and password specified for you::

    public final static String ROS_MASTER_URI = "***";
    public final static String ROS_HOSTNAME = "***";
    public final static String NEO4J_ADDRESS = "***";
    public final static String NEO4J_USERNAME = "***";
    public final static String NEO4J_PASSWORD = "***";
    public final static String REDIS_URI = "***";
    public final static String REDIS_PASSWORD = "***";

You may use either remote or local addresses and credentials. If using local configuration, then::

    public final static String ROS_MASTER_URI = "http://127.0.0.1:11311/";
    public final static String ROS_HOSTNAME = "127.0.0.1";
    public final static String NEO4J_ADDRESS = "bolt://127.0.0.1:7687";
    public final static String REDIS_URI = "redis://127.0.0.1:6379/0";


ROS Configuration (remote)
---------------------------------------------------

If you are using memory module on the PC other then roscore, ROS interfaces require `network setup <http://wiki.ros.org/ROS/NetworkSetup>`_.

For this two variables in Config class (util package) should be changed:

- ROS_MASTER_URI - defines an URI of roscore module in the network, e.g. "http://bot.roboy.org:11311/"
- ROS_HOSTNAME - defines the IP address of the machine with rosjava mudule in the network, e.g. "192.168.1.1"

If you running ros in a virtual machine, please configure brisging and use the respective IP adresses.


Running the package
---------------------------------------------------

After you have entered the proper configuration:

- in the project directory do::

	mvn clean install

- navigate to::

    cd target

- run the package::

    java -jar roboy_memory-0.9.0-jar-with-dependencies.jar


Development
--------------------------------------------------

For further development we recommend using Intellij IDEA IDE.
The community edition is availiable here: `Download IDEA <https://www.jetbrains.com/idea/download/>`_.

If you are eligible, we suggest applying for `this package <https://www.jetbrains.com/student/>`_ containing the full versions of JetBrains software for free.