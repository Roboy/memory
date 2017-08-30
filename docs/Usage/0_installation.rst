Installation
=============

Maven
--------------------------------------------------

The project requires Maven. You may get it here: `Download Maven <https://maven.apache.org/download.cgi>`_

Consider checking this entries: `Install <https://maven.apache.org/install.html>`_,
`Configure <https://maven.apache.org/configure.html>`_ and `Run <https://maven.apache.org/run.html>`_


Local Neo4j Instance
--------------------------------------------------

There are several options (for a Unix-based OS)

**Docker Container Distribution** 

- get the container with::
	
	docker pull neo4j

**Using the Debian Repository** 

- to use the repository, add it to the list of sources::
	
	wget -O - https://debian.neo4j.org/neotechnology.gpg.key | sudo apt-key add -
	echo 'deb https://debian.neo4j.org/repo stable/' | sudo tee /etc/apt/sources.list.d/neo4j.list
	sudo apt-get update

- install the latest Neo4j version::

	sudo apt-get install neo4j

- **cd** into **/usr/bin** and run::

	neo4j start

- congratulations! You have started the Neo4j instance!

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

- congratulations! You have started the Neo4j instance!

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

- congratulations! You have started the Neo4j instance!

**Build it yourself** 

- clone a git project with:: 
	
	git clone git@github.com:neo4j/neo4j.git

- in the project directory do:: 

	mvn clean install

- after building artifacts with Maven do::

	export PATH="bin:$PATH" && make clean all

- **cd** into **packaging/standalone/target** and run::

	bin/neo4j start

- congratulations! You have started the Neo4j instance!

Local Redis Instance
--------------------------------------------------



Remote Neo4j Instance
--------------------------------------------------

If the local instance is not necessary, use a remote Neo4j instance by establishing a connection to the Roboy server. Please, refer to :ref:`getting-started`

Remote Redis Instance
--------------------------------------------------

If the local instance is not necessary, use a remote Redis instance by establishing a connection to the Roboy server. Please, refer to :ref:`getting-started`

Installing ROS
--------------------------------------------------



Package Installation
--------------------------------------------------

The project is implemented upon a build automation tool - Maven, so the dependencies are tracked automatically, if there is a dependency missing or dependency related exception, please leave a feedback at the GitHub repository.

- clone a git project with:: 
	
	git clone git@github.com:Roboy/roboy_memory.git

