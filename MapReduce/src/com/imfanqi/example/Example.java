package com.imfanqi.example;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class Example extends Configured implements Tool {
	/**
	 * Map任务
	 */
	public static class Map extends Mapper<LongWritable, Text, Text, Text> {
		public void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {

		}
	}

	/**
	 * Reduce任务
	 */
	public static class Reduce extends Reducer<Text, Text, Text, Text> {
		public void reduce(Text key, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {

		}
	}

	@Override
	public int run(String[] args) throws Exception {
		Configuration conf = getConf();

		Job job = new Job(conf, "Example");								//任务名
		job.setJarByClass(Example.class);								//指定Class
		
		FileInputFormat.addInputPath( job, new Path(args[0]) );			//输入路径
		FileOutputFormat.setOutputPath( job, new Path(args[1]) );		//输出路径
		
		job.setMapperClass( Map.class );								//调用上面Map类作为Map任务代码
		job.setReducerClass ( Reduce.class );							//调用上面Reduce类作为Reduce任务代码
		job.setOutputFormatClass( TextOutputFormat.class );
		job.setOutputKeyClass( Text.class );							//指定输出的KEY的格式
		job.setOutputValueClass( Text.class );							//指定输出的VALUE的格式
		
		job.waitForCompletion(true);
		return job.isSuccessful() ? 0 : 1;
	}

	/**
	 * 设置系统说明 设置MapReduce任务
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		//运行任务
		int res = ToolRunner.run(new Configuration(), new Example(), args);
        System.exit(res);
	}

}
