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

        private Text mapKey = new Text();
        private Text incomingLinks = new Text();
        private Text outgoingLinks = new Text();
        private static String DEL = "', '";

        public void map(Object key, Text value, Context context)
                throws IOException, InterruptedException {
            String[] line = value.toString().split(DEL);
            if (line.length>1) {
                String fromPage = line[0].trim();
                String fromRank = line[1].trim();
                String toPage = line[2].trim();
                String toOutNum = line[3].trim();
                // This is in order to calculate the pagerank of the 'to' page "C. Imbriano"
                mapKey.set(toPage);
                incomingLinks.set("'IN" + DEL + fromPage + DEL + fromRank + DEL + toOutNum + "'");
                context.write(mapKey, incomingLinks);
                // This is in order to keep track of outgoing links in order to
                // scale each page-rank voting power.
                outgoingLinks.set("'OUT" + DEL + fromPage + "'");
                context.write(outgoingLinks, mapKey);
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