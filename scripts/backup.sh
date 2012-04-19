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

username=#user#;
direname=#dire#;

cd $direname;
dt=`date | sed 's/ /-/g'`;
filename=snapshot-$dt.tar.bz2
e
e "# Backup log of $dt"
cd db;
e "sending $filename to $username@eniac.seas.upenn.edu:~/"
tar -cjf ../$filename * >> ~/.backup.log
cd ../
rsync -av --progress ./$filename $username@eniac.seas.upenn.edu:~/ >> ~/.backup.log
