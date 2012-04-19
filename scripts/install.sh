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

function usage {
  e
  e 'Please run ./usage <uname>'
  e
  e '  <uname>: your username at Penn'
}
if [[ $1 != 'nvas' ]]; then
  usage;
  exit;
fi

e 'generating key pair..'
e 'enter your ssh password at EC2 and leave next fields blank!'
ssh-keygen -t rsa -f ~/.ssh/id_rsa -C `whoami`@`uname -n`
e 'sending pair to eniac..'
e 'enter your eniac ssh password'
ssh-copy-id -i $1@eniac.seas.upenn.edu
e 'adding pair to ssh agent..'
ssh-add ~/.ssh/id_rsa
# we could rsync to make sure that it works
e 'please run ssh '$1'@eniac.seas.upenn.edu and confirm'
