package com.danner.bigdata.hadoop.mapreduce.servercase;

import com.danner.bigdata.hadoop.mapreduce.utils.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * 每个 userid 中消费最多的服务器名称   ==> Top 1
 * 10000,ruozedata003,300
 * 10001,hadoop000,1000
 * 10001,hadoop001,2000
 * 10001,hadoop003,3000
 * 10002,jepson001,1000
 */
public class TopOne {

    public static void main(String[] args) throws Exception {
        Configuration configuration = new Configuration();
        Job job = Job.getInstance(configuration);


        String inputFile = "input/topN.log";
        String outFile = "out/serverCast";
        FileUtils.clearFile(outFile,configuration);
        FileInputFormat.setInputPaths(job,inputFile);
        FileOutputFormat.setOutputPath(job,new Path(outFile));

        job.setJarByClass(TopOne.class);

        job.setMapperClass(MyMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(ServerCast.class);

        job.setReducerClass(MyReducer.class);
        job.setOutputKeyClass(ServerCast.class);
        job.setOutputValueClass(NullWritable.class);

        int status = job.waitForCompletion(true) ? 0 : 1;
        System.exit(status);
    }

    public static class MyMapper extends Mapper<LongWritable,Text,Text,ServerCast>{
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

            String[] split = value.toString().split(",");
            if (split.length == 3){
                String userId = split[0];
                String serverName = split[1];
                long cast = Long.parseLong(split[2]);
                context.write(new Text(userId),new ServerCast(userId,serverName,cast));
            }
        }
    }

    public static class MyReducer extends Reducer<Text,ServerCast,ServerCast,NullWritable>{
        @Override
        protected void reduce(Text key, Iterable<ServerCast> values, Context context) throws IOException, InterruptedException {
            ServerCast maxServerCast = new ServerCast();

            for (ServerCast serverCast:values){
                if (serverCast.cast > maxServerCast.cast){
                    maxServerCast.userId = serverCast.userId;
                    maxServerCast.serverName = serverCast.serverName;
                    maxServerCast.cast = serverCast.cast;
                }
            }

            context.write(maxServerCast,NullWritable.get());
        }
    }
}
