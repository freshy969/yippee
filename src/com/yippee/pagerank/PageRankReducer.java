package com.yippee.pagerank;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * This is the Reducer class, following the MapReduce programming model.
 */
public class PageRankReducer extends Reducer<Text, Text, Text, IntWritable> {

    public void reduce(Text key, Iterable<Text> values, Context context)
        throws IOException, InterruptedException {

        int pagerank = 0;
        for (Text text : values) {
            pagerank++;
        }
        context.write(key, new IntWritable(pagerank));

    }
}
