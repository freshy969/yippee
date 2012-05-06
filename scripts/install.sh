#!/bin/bash
  
##
# 2012, Nikos Vasilakis
# n.c.vasilakis@gmail.com
#
# A script that installs all necessary tools for backup
#
# Usage: ./install.sh
##

##
#Function used for debugging output
##
function e {
  echo "$1";
}

##
#Function used for outputting script usage
##
function usage {
  e
  e 'Please run ./install <uname>'
  e
  e '  <uname>: your username at Penn'
}
if [[ "$1" != 'nvas' -a "$1" != 'tdu2' -a "$1" != 'mmiran' -a "$1" != 'imbriano']]; then
  usage;
  exit;
fi

cd ../
PROJECTROOT=`pwd`;
cd scripts;
e 'generating key pair..'
e 'enter your ssh password at EC2 and leave next fields blank!'
ssh-keygen -t rsa -f ~/.ssh/id_rsa -C `whoami`@`uname -n`
e 'sending pair to eniac..'
e 'enter your eniac ssh password'
ssh-copy-id -i $1@eniac.seas.upenn.edu
e 'adding pair to ssh agent..'
ssh-add ~/.ssh/id_rsa
e 'setting up backup scripts..'
cp backup.sh ~/
sed -i "s;#user#;$1;" ~/backup.sh;
sed -i "s;#dire#;$PROJECTROOT;" ~/backup.sh;
#sed -i "s/dire/$PROJECTROOT/" ~/backup.sh;
# we could rsync to make sure that it works
e 'logging to '$1'@eniac.seas.upenn.edu should not require password..'
e 
e '******************************************'
e '***  type exit or <control>+D to quit  ***'
e '******************************************'
echo "# Log file for project in $PROJECTROOT generated on $(date)" > ~/.backup.log
ssh $1@eniac.seas.upenn.edu
