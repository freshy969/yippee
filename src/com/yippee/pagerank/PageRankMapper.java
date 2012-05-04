package com.yippee.pagerank;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * This is the Mapper class, following the MapReduce programming model.
 */
public class PageRankMapper extends Mapper<LongWritable, Text, Text, Text>{

    @Override
    public void map(LongWritable key, Text value, Context context) throws
            IOException, InterruptedException {

        String[] line = value.toString().split(", ");
        if (line.length>2) {
            String from = line[0].substring(1,line[0].length()-1);
            String to   = line[1].substring(1,line[0].length()-1);
            context.write(new Text(to), new Text(from));
        }
    }
}