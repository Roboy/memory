#!/usr/bin/env bash
echo Password: **********
scp -r -P 2002 roboy@bot.roboy.org:/var/lib/neo4j/data/ ~/Neo4J/Backups/$(date +%Y%m%d-%H%M%S)
echo finish!
read -p "$*"