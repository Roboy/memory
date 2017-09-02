#!/bin/bash

# Default user and password
username="neo4j"
pass="neo4j"

while getopts u:p: option
do
 case "${option}"
 in
 u) username=${OPTARG};;
 p) pass=$OPTARG;;
 esac
done

echo "CALL dbms.security.createUser('$username', '$pass', false); :exit" | /usr/bin/cypher-shell -u neo4j -p neo4j 
echo "CALL dbms.security.deleteUser('neo4j'); :exit" | /usr/bin/cypher-shell -u $username -p $pass 
