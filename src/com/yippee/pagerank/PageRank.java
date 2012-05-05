package com.yippee.pagerank;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

//import org.apache.hadoop.util.GenericOptionsParser;

public class PageRank {

    public static class TokenizerMapper
            extends Mapper<Object, Text, Text, Text> {

        private Text from = new Text();
        private Text to = new Text();

        public void map(Object key, Text value, Context context)
                throws IOException, InterruptedException {
            String[] line = value.toString().split(", ");
            if (line.length>1) {
                String fromString = line[0].trim();
                String toString   = line[1].trim();
                from.set(fromString);
                to.set(toString);
                context.write(to, from);
            }
        }
    }

    public static class IntSumReducer
            extends Reducer<Text, Text, Text, IntWritable> {
        private IntWritable result = new IntWritable();

        public void reduce(Text key, Iterable<Text> values,
                           Context context
        ) throws IOException, InterruptedException {
            int sum = 0;
            for (Text val : values) {
                sum ++;
            }
            result.set(sum);
            context.write(key, result);
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        //String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
        if (args.length != 3) {
            System.err.println("Usage: PageRank <in> <out>");
            System.exit(2);
        }
        Job job = new Job(conf, "PageRank");
        job.setJarByClass(PageRank.class);
        job.setMapperClass(TokenizerMapper.class);
//        job.setCombinerClass(IntSumReducer.class);
        job.setReducerClass(IntSumReducer.class);
        // Set the outputs for the Map
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        FileInputFormat.addInputPath(job, new Path(args[1]));
        FileOutputFormat.setOutputPath(job, new Path(args[2]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}