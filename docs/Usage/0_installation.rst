Installation
=============

Maven
--------------------------------------------------

The project requires Maven. You may get it here: `Download Maven <https://maven.apache.org/download.cgi>`_

.. seealso:: Consider checking out these entries: `Install <https://maven.apache.org/install.html>`_,`Configure <https://maven.apache.org/configure.html>`_ and `Run <https://maven.apache.org/run.html>`_

Local Setup
--------------------------------------------------

Local Neo4j Instance
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

There are several options (for a Unix-based OS)

**[Recommended] Docker Container Distribution** 

- download Docker from apt::

	sudo apt-get install docker.io

- download and run Neo4J via Docker by::

	sudo docker run \
		--publish=7474:7474 --publish=7687:7687 \
		--volume=$HOME/neo4j/data:/data \
		--volume=$HOME/neo4j/logs:/logs \
		neo4j:3.4

- to quit Neo4J, simply CTRL+C or exit the terminal window

.. warning:: You will be running Neo4J in sudo mode. If you do not wish to do so, see the other methods or `here <https://neo4j.com/docs/operations-manual/current/installation/docker/#docker-user>`_ 

**Using the Debian Repository** 

- to use the repository, add it to the list of sources::
	
	wget -O - https://debian.neo4j.org/neotechnology.gpg.key | sudo apt-key add -
	echo 'deb https://debian.neo4j.org/repo stable/' | sudo tee /etc/apt/sources.list.d/neo4j.list
	sudo apt-get update

- install the latest Neo4j version::

	sudo apt-get install neo4j

- **cd** into **/usr/bin** and run::

	neo4j start

**RPM repository**

Follow these steps as **root**:

- add the repository::
	
	rpm --import http://debian.neo4j.org/neotechnology.gpg.key
	cat <<EOF>  /etc/yum.repos.d/neo4j.repo
	[neo4j]
	name=Neo4j RPM Repository
	baseurl=http://yum.neo4j.org/stable
	enabled=1
	gpgcheck=1
	EOF
 
- install by executing::

 	yum install neo4j-3.2.0-rc3 (or the newer version)

- **cd** into **/usr/bin** and run::

	neo4j start

**Tarball installation**

- download the latest release from::
	
	http://neo4j.com/download/

- select the appropriate **tar.gz** distribution for your platform
- extract the contents of the archive, using:: 
	
	tar -xf <filename>

- refer to the top-level extracted directory as **NEO4J_HOME**
- change directory to **$NEO4J_HOME**
- run::
	
	 ./bin/neo4j console

**Build it yourself** 

- clone a git project with:: 
	
	git clone git@github.com:neo4j/neo4j.git

- in the project directory do:: 

	mvn clean install

- after building artifacts with Maven do::

	export PATH="bin:$PATH" && make clean all

- **cd** into **packaging/standalone/target** and run::

	bin/neo4j start

Congratulations! You have started the Neo4j instance!

Local Redis Instance
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

In order to compile Redis follow this simple steps:

- get the source code::

    wget http://download.redis.io/redis-stable.tar.gz

- unzip the tarball::

    tar xvzf redis-stable.tar.gz

- navigate to::

    cd redis-stable

- compile::

    make


Remote Setup
--------------------------------------------------

Remote Neo4j Instance
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

If the local instance is not necessary, use a remote Neo4j instance by establishing a connection to the Roboy server. Please, refer to :ref:`getting-started`

Remote Redis Instance
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

If the local instance is not necessary, use a remote Redis instance by establishing a connection to the Roboy server. Please, refer to :ref:`getting-started`

Installing ROS
--------------------------------------------------

.. deprecated:: 1.1

    The project is using `rosjava <http://wiki.ros.org/rosjava?distro=kinetic>`_ which requires ROS `kinetic <http://wiki.ros.org/kinetic>`_.

Simple installation (assuming Ubuntu 16.04 LTS):

- setup your sources.list::

    sudo sh -c 'echo "deb http://packages.ros.org/ros/ubuntu $(lsb_release -sc) main" >
    /etc/apt/sources.list.d/ros-latest.list'

- set up your keys::

    sudo apt-key adv --keyserver hkp://ha.pool.sks-keyservers.net:80
    --recv-key 421C365BD9FF1F717815A3895523BAEEB01FA116

- update Debian package index::

    sudo apt-get update

- commence desktop full installation of kinetic::

    sudo apt-get install ros-kinetic-desktop-full

If the simple installation was not successful, please, refer to `this guide <http://wiki.ros.org/ShadowRepository>`_.

Roboy Memory Package Installation
--------------------------------------------------

The project is implemented upon a build automation tool - Maven, so the dependencies are tracked automatically, if there is a dependency missing or dependency related exception, please leave a feedback at the GitHub repository.

- clone a git project with:: 
	
	git clone git@github.com:Roboy/roboy_memory.git
