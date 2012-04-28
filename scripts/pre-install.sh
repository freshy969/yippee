#!/bin/bash
  
##
# 2012, Nikos Vasilakis
# n.c.vasilakis@gmail.com
#
# A script that setups the env on EC2
#
# Usage: ./pre-install.sh
#
# TODO:
#        *  
##

sudo yum install git
git clone https://nvasilakis@github.com/nvasilakis/yippee.git
sudo yum install ant
cd yippee/
ant usage
history 
