#!/bin/bash
  
##
# 2012, Nikos Vasilakis
# n.c.vasilakis@gmail.com
#
# A script that runs pagerank process
#
# Usage: ./pagerank.sh
#
# TODO:
#        *  
##

echo "Starting PageRank.."
# rsync -av --progress 

if [[ $1 == "" ]]; then
  echo "./pagerank.sh input-file.hadoop"
fi

for (( i = 0; i < 30; i++ )); do
  echo "iteration $i";
  # Copy input to hdfs
  hadoop dfs -copyFromLocal pr-input-$i.hadoop /home/hduser/pr-input.hadoop
  
  # Run hadoop process
  hadoop jar build/yippee.jar PageRank /home/hduser/pr-input.hadoop /home/hduser/pr-output.hadoop

  # Copy a backup in ext3 and re-run process
  hadoop dfs -copyToLocal /home/hduser/pr-output.hadoop pr-input-$((i+1)).hadoop

  # Clean previous input and output on hdfs
  hadoop fs -rmr /home/hduser/pr-input.hadoop
  hadoop fs -rmr /home/hduser/pr-output.hadoop
done

