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
	
	./populate.sh -u your_username -p your_password

- wait the script to execute.

This would populate the database with the intial Knowledge Representation data.


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

If you are using roboy_memory package in local mode, please use the username and password defined in the user configuration step together with '-m local' command line parameter to run from **$ROBOY_MEMORY** folder::

	java roboy_memory -m local -u your_username -p your_password

If you are using roboy_memory package in remote mode, please use the username and password for the remote access together with '-m remote' command line parameter to run from **$ROBOY_MEMORY** folder::

	java roboy_memory -m remote -u user -p user_password

	OR

	java roboy_memory -m remote -u dialog -p dialog_password
	
	OR
	
	java roboy_memory -m remote -u vision -p vision_password
	
	OR
	
	java roboy_memory -m remote -u memory -p memory_password


ROS Configuration (optional)
---------------------------------------------------

If you are using memory module on another PC, ROS interfaces require network setup. (more info: http://wiki.ros.org/ROS/NetworkSetup )

For this two variables in Config class (util package) should be changed.

    ROS_MASTER_URI - defines an URI of roscore module in the network, e.g. "http://bot.roboy.org:11311/"
    ROS_HOSTNAME - defines the IP address of the machine with rosjava mudule in the network, e.g. "192.168.1.1"


.. todo::
  Starting roboy_memory package and add interfaces for local and remote usage 
