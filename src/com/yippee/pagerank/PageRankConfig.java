package com.yippee.pagerank;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import javax.xml.soap.Text;
import java.io.IOException;

/**
 * The MapReduce Configuration class, responsible for setting up the environment,
 * configuring inputs and outputs and launching the job.
 */
public class PageRankConfig {

    /**
     * The constructor is responsible for configuring the job.
     */
    public PageRankConfig(String input, String output) throws IOException,
            ClassNotFoundException, InterruptedException {
        Job job = new Job();
        job.setJarByClass(PageRankConfig.class);

        FileInputFormat.addInputPath(job, new Path(input));
        FileOutputFormat.setOutputPath(job, new Path(output));

        job.setMapperClass(PageRankMapper.class);
        job.setReducerClass(PageRankReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        System.exit(job.waitForCompletion(true)? 0:1);
    }

    public static void main(String[] args) throws  Exception{
        PageRankConfig config = new PageRankConfig(args[0], args[1]);

    }


}
