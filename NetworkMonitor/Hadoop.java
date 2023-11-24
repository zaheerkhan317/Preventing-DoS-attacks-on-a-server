package com;
import java.io.*;
import java.util.ArrayList;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapred.lib.MultipleInputs;
public class Hadoop{
public static void run(String path){
	try{
		JobConf conf = new JobConf(Hadoop.class);
		conf.setJobName("MR-EASSFIM");
		conf.setOutputKeyClass(Text.class); 
		conf.setOutputValueClass(Text.class);
		conf.setInputFormat(TextInputFormat.class);
		conf.setOutputFormat(TextOutputFormat.class);
		conf.setMapperClass(MapReduce.class);
		FileInputFormat.setInputPaths(conf, new Path(path));
		FileOutputFormat.setOutputPath(conf, new Path("output"));
		JobClient.runJob(conf);
	}catch(Exception e){
		e.printStackTrace();
	}
}
}