package com.yippee.pagerank;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;


public class PageRank {

	public static class PRMapper
	extends Mapper<Object, Text, Text, Text> {

		private Text mapKey = new Text();
		private Text incomingLinks = new Text();
		private Text outgoingLinks = new Text();
		private static String DEL = "', '";

		@Override
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

	public static class PRReducer extends Reducer<Text, Text, Text, Text> {
		private Text outKey = new Text();
		private Text result = new Text();
		

		private static String DEL = "', '";

		@Override
		public void reduce(Text targetPage, Iterable<Text> values, Context context ) throws IOException, InterruptedException {

			int outgoingLinkCount = 0;
			Double pagerank = new Double(0);
			List<String> outgoingLinks = new LinkedList<String>();

			for (Text value : values) {
				String[] parts = value.toString().split(DEL);
				if (parts[0].contains("IN")) {

					//String fromPage = parts[1].trim();
					double fromRank = Double.parseDouble(parts[2].trim().substring(1, parts[2].length() - 1));
					int fromOutNum = Integer.parseInt(parts[3].trim().substring(1, parts[3].length() - 1));

					pagerank += fromRank / fromOutNum;


				} else {
					// This are outgoing links
					outgoingLinkCount++;

					String outLink = parts[1].trim();
					outgoingLinks.add(outLink);
				}
			}


			for(String s : outgoingLinks){
				outKey.set(targetPage);
				result.set("'" + pagerank + DEL + s + DEL + outgoingLinkCount + "'");
				context.write(targetPage, result);
			}

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
		job.setMapperClass(PRMapper.class);
		//        job.setCombinerClass(IntSumReducer.class);
		job.setReducerClass(PRReducer.class);
		// Set the outputs for the Map
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		FileInputFormat.addInputPath(job, new Path(args[1]));
		FileOutputFormat.setOutputPath(job, new Path(args[2]));
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}