package com.yippee.pagerank;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * This is the Mapper class, following the MapReduce programming model.
 */
public class PageRankMapper extends Mapper<LongWritable, Text, Text, IntWritable>{

    public void map(LongWritable key, Text value, Context context) throws
            IOException, InterruptedException {
    }


}