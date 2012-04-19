#!/bin/bash
  
##
# 2012, Nikos Vasilakis
# n.c.vasilakis@gmail.com
#
# A script that keeps backup of the database
#
# Usage: ./backup.sh
##


##
#Function used for appending log to a file
##
function e {
  echo "$1" >> ~/.backup.log
}

e "# Backup log of $(date)"

