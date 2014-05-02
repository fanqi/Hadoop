package com.imfanqi.routerlog;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;



/**
 * 路由日志筛选程序
 * 现有一批路由日志（有删减），需要提取MAC地址和时间，删去其他内容
 * @author fanqi
 * 
 */
public class RouterLogFiltrate extends Configured implements Tool {

	enum Counter {
		LINESKIP; // 出错的行
	}

	/**
	 * Map任务
	 */
	public static class Map extends Mapper<LongWritable, Text, NullWritable, Text> {
		public void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
	String line = value.toString();				//读取源数据
			
			try
			{
				//数据处理
				String [] lineSplit = line.split(" ");
				String month = lineSplit[0];
				String time = lineSplit[1];
				String mac =lineSplit[6];
				Text out=new Text(month +" "+time +" "+mac);
				context.write(NullWritable.get(),out );	//输出 key \t value
			}
			catch ( java.lang.ArrayIndexOutOfBoundsException e )
			{
				context.getCounter(Counter.LINESKIP).increment(1);	//出错令计数器+1
				return;
			}
		}
	}
	@Override
	public int run(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Configuration conf = getConf();

		Job job = new Job(conf, "RouterLogFiltrate");								//任务名
		job.setJarByClass(RouterLogFiltrate.class);								//指定Class
		
		FileInputFormat.addInputPath( job, new Path(args[0]) );			//输入路径
		FileOutputFormat.setOutputPath( job, new Path(args[1]) );		//输出路径
		
		job.setMapperClass( Map.class );								//调用上面Map类作为Map任务代码
		job.setOutputFormatClass( TextOutputFormat.class );
		job.setOutputKeyClass( NullWritable.class );					//指定输出的KEY的格式
		job.setOutputValueClass( Text.class );							//指定输出的VALUE的格式
	
		job.waitForCompletion(true);
		return job.isSuccessful() ? 0 : 1;
	}
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		int res = ToolRunner.run(new Configuration(), new RouterLogFiltrate(), args);
        System.exit(res);
	}



}
