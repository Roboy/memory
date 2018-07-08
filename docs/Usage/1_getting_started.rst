.. _getting-started:

Getting started
===============

Local Instances 
-------------------------------------

Local Neo4j Instance
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Before proceeding further, please commence a user configuration step:

- please navigate inside the package folder **$ROBOY_MEMORY** to::
	
	cd scripts

- run::
	
	./user_conf.sh -u your_username -p your_password

- wait the script to execute.

You may proceed with your current DB now (you need to put the data there) or fetch the remote DB contents.

To copy remote Neo4j DB into your local instance:

- open the script intext editor::

    vi backup.sh OR nano backup.sh

- enter the password to connect to bot.roboy.org into respective line
- run the script specifying the path where to copy the DB files::

    ./backup.sh

- wait the script to execute. You will find the DB in ~/Neo4J/Backups/"date" (if it didn't work automatically, then create the ~/Neo4J/Backups/ directory and try again)
- copy the contents of "databases" directory to your local `DB directory <https://neo4j.com/docs/operations-manual/current/configuration/file-locations/>`_.

.. warning::

    Be cautious! This procedure (unlikely) might overwrite your credentials with the remote ones, see below.

Local Redis Instance
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

In order to have Redis properly configured, go through the next steps:

- create a directory where to store your Redis config files and your data::

    sudo mkdir /etc/redis
    sudo mkdir /var/redis

- copy the template configuration file you'll find in the root directory of the Redis distribution::

    sudo cp redis.conf /etc/redis/6379.conf

- create a directory that will work as data and working directory::

    sudo mkdir /var/redis/6379

- in the configuration file: set the **pidfile** to /var/run/redis_6379.pid, set the **logfile** to /var/log/redis_6379.log, set the **dir** to /var/redis/6379

Before proceeding further, please commence a password configuration step:

- please navigate to Redis configuration::

	cd /etc/redis/

- open configuration file with a text editor::

	vi 6379.conf OR nano 6379.conf

- find the line conatining 'requirepass', uncomment it and enter your password::

    requirepass some_passphrase

- save and start Redis with the updated configuration::

    ./redis-server /etc/redis/6379.conf

Remote Instance
--------------------------------------------------

.. warning::

    Be careful while using remote and/or interacting with bot.roboy.org server! You are responsible to keep it functioning properly!

Please, do not crush everything. You would make little `kittens very sad <http://goo.gl/FZsTTm>`_.


Remote Neo4j Instance
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

To use a remote intance of Neo4j containing the most recent Knowledge Representation, ensure your connectivity to the Roboy server.
If the server is up, use the roboy_memory package in the remote mode (default):

- bolt://bot.roboy.org:7687 - for the package configuration (enter this in config file)
- http://bot.roboy.org:7474 - for the GUI access in web-browser

For this, please use a remote Neo4j password related to your specific user:

- user, a generic Roboy member
- dialog, a dialog team member
- vision, a vision team member
- memory, a memory team member (developer)

Remote Redis Instance
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

To use a remote instance of Redis containing the most recent faces features, ensure your connectivity to the Roboy server.
If the server is up, use the roboy_memory package in the remote mode (default):

- redis://bot.roboy.org:6379/0 - for the package configuration

For this, please use the remote Redis password.


ROS Configuration
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

.. deprecated:: 1.1

    Using of ROS is deprecated.

Before you can use ROS, you will need to initialize rosdep::

    sudo rosdep init
    rosdep update

To install dependencies for building ROS packages, run::

    sudo apt-get install python-rosinstall python-rosinstall-generator python-wstool build-essential

Afterwords, procceed with installing catkin::

    sudo apt-get install ros-kinetic-catkin

Source the environment like this::

    echo "source /opt/ros/kinetic/setup.bash" >> ~/.bashrc
    source ~/.bashrc

Build a catkin workspace::

    mkdir -p ~/catkin_ws/src
    cd ~/catkin_ws/
    catkin_make

Source your new setup.*sh file::

    source devel/setup.bash

Then in separate Terminal, run::

    roscore

If you are using Memory Module on the PC other then one with roscore, ROS interfaces require `network setup <http://wiki.ros.org/ROS/NetworkSetup>`_.

For this two variables in Config class (util folder of the Memory Module) should be changed:

- ROS_MASTER_URI - defines an URI of roscore module in the network, e.g. "http://bot.roboy.org:11311/"
- ROS_HOSTNAME - defines the IP address of the machine with rosjava mudule in the network, e.g. "192.168.1.1"

If you running ros in a virtual machine, please configure bridged networking and use the respective IP addresses:

- `VMware Fusion <https://docs.vmware.com/en/VMware-Fusion/8.0/com.vmware.fusion.using.doc/GUID-E498672E-19DD-40DF-92D3-FC0078947958.html>`_
- `VMware Workstation <https://docs.vmware.com/en/VMware-Workstation-Pro/12.0/com.vmware.ws.using.doc/GUID-0CE1AE01-7E79-41BB-9EA8-4F839BE40E1A.html>`_
- `Parallels <http://kb.parallels.com/en/4948>`_
- `VirtualBox <https://www.virtualbox.org/manual/ch06.html>`_
- `Hyper-V <https://docs.microsoft.com/en-us/virtualization/hyper-v-on-windows/quick-start/connect-to-network>`_. We don't recommend using this one, but as you like.


Running the Package
---------------------------------------------------

After you have entered the proper configuration:

- in the project directory do::

	mvn clean install

- navigate to::

    cd target

- run the package::

    java -jar roboy_memory-1.1.0-jar-with-dependencies.jar

Configuring the Package's Environment Variables
--------------------------------------------------

In the configuration file you will encounter the next important fields:

.. code-block:: java
	public final static String ROS_MASTER_URI
	public final static String ROS_HOSTNAME
	public final static String NEO4J_ADDRESS
	public final static String NEO4J_USERNAME
	public final static String NEO4J_PASSWORD
	public final static String REDIS_URI
	public final static String REDIS_PASSWORD

.. deprecated:: 1.1
    ROS_MASTER_URI and ROS_HOSTNAME

For using roboy_memory package in remote mode properly, please initialize specific environment variables.
To do so, open your bash profile file with text editor (depending on your preferences)::

    vi ~/.bashrc OR vi ~/.bash_profile OR nano ~/.bashrc OR nano ~/.bash_profile

and append the next lines with the information specified for you::

    export ROS_MASTER_URI="***"
    export ROS_HOSTNAME="***"
    export NEO4J_ADDRESS="***"
    export NEO4J_USERNAME="***"
    export NEO4J_PASSWORD="***"
    export REDIS_URI="***"
    export REDIS_PASSWORD="***"

.. deprecated:: 1.1
    ROS_MASTER_URI and ROS_HOSTNAME

You may use either remote or local addresses and credentials.

.. raw:: html 

    <details>  
    <summary>Local Address Example</summary> 
    <p> 
        export ROS_MASTER_URI="http://127.0.0.1:11311" <br>
        export ROS_HOSTNAME="127.0.0.1" <br>
        export NEO4J_ADDRESS="bolt://127.0.0.1:7687" <br> 
        export NEO4J_USERNAME="neo4j" <br>
        export NEO4J_PASSWORD="neo4jpassword" <br>
        export REDIS_URI="redis://localhost:6379/0" <br>
        export REDIS_PASSWORD="root"  <br>
    </p>  
    </details> 
    <br>


Development
--------------------------------------------------

For further development we recommend using Intellij IDEA IDE.
The community edition is available here: `Download IDEA <https://www.jetbrains.com/idea/download/>`_.

If you are eligible, we suggest applying for `this package <https://www.jetbrains.com/student/>`_ containing the full versions of JetBrains software for free.
