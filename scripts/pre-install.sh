#!/bin/bash
  
##
# 2012, Nikos Vasilakis
# n.c.vasilakis@gmail.com
#
# A script that setups the env on EC2
#
# Usage: ./pre-install.sh
#
##

sudo yum update
sudo yum install git
git clone https://nvasilakis@github.com/nvasilakis/yippee.git
sudo yum install ant
cd yippee/
ant usage
export JAVA_HOME="/usr/lib/jvm/java-1.6.0-openjdk-1.6.0.0/"
ant project-prod-crawler -DbIp=10.208.121.79 -Dbport=9000 -Dlport=9000
history
