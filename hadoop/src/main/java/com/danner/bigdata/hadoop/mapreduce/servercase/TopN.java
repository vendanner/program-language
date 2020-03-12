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
import java.util.Comparator;
import java.util.Random;
import java.util.TreeMap;

/**
 * 每个 userid 中消费最多的服务器名称   ==> 分组Top N
 * 分组 TopN 数据倾斜咋解决？？？
 * 10000,ruozedata003,300
 * 10001,hadoop000,1000
 * 10001,hadoop001,2000
 * 10001,hadoop003,3000
 * 10002,jepson001,1000
 *
 * 数据倾斜在于数据分布不均匀，在 reducer 阶段，某几个节点数据量超大，导致处理慢卡顿
 * 1、减少 mapper 端的输出，topN 问题中可以使 mapper 输出 topN 而不是整体输出，尽量减少数据量
 * 2、partitioner 时，使用随机key ，可以使后端 reducer 负载均衡，但此时需要二次 reducer 才能得到最终的 topN
 * 3、这个方案在第二次 reducer 时，使每个 reducer 处理数量差不多，但整体的资源消耗相对来说有增加
 * 4、当然你还可以更进一步的自定义 GroupingComparatorClass，使第一次 reducer 输出数据量减少
 * */

public class TopN {

    public static void main(String[] args) throws Exception {
        Configuration configuration = new Configuration();
        // top 2
        configuration.set("topN","2");
        Job job = Job.getInstance(configuration);

        String inputFile = "input/topN/";
        String outFile = "out/serverCast";
        FileUtils.clearFile(outFile,configuration);
        FileInputFormat.setInputPaths(job,inputFile);
        FileOutputFormat.setOutputPath(job,new Path(outFile));

        job.setJarByClass(TopN.class);

        job.setMapperClass(RandomMyMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(ServerCast.class);

        job.setReducerClass(MyReducer.class);
        job.setOutputKeyClass(ServerCast.class);
        job.setOutputValueClass(NullWritable.class);

        job.setCombinerClass(MyCombine.class);
        job.setPartitionerClass(ServerCastPartitioner.class);
        job.setNumReduceTasks(3);

        if(job.waitForCompletion(true)){
            Job job1 = Job.getInstance(configuration);

            inputFile = "out/serverCast";
            outFile = "out/serverCastEnd";
            FileUtils.clearFile(outFile,configuration);
            FileInputFormat.setInputPaths(job1,inputFile);
            FileOutputFormat.setOutputPath(job1,new Path(outFile));

            job1.setJarByClass(TopN.class);

            job1.setMapperClass(MyMapper.class);
            job1.setMapOutputKeyClass(Text.class);
            job1.setMapOutputValueClass(ServerCast.class);

            job1.setReducerClass(MyReducer.class);
            job1.setOutputKeyClass(ServerCast.class);
            job1.setOutputValueClass(NullWritable.class);

            job1.setCombinerClass(MyCombine.class);
            job1.setPartitionerClass(ServerCastPartitioner.class);
            job1.setNumReduceTasks(3);

            int status = job1.waitForCompletion(true)?0:1;
            System.exit(status);
        }

    }

    public static class RandomMyMapper extends Mapper<LongWritable,Text,Text,ServerCast>{

        int numReducer;
        Random random;
        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            numReducer = context.getNumReduceTasks();
            random = new Random();
        }
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] split = value.toString().split(",");
            if (split.length == 3){
                String userId = split[0];
                String serverName = split[1];
                long cast = Long.parseLong(split[2]);
                // 随机 key
                userId += "_" + random.nextInt(numReducer);
                context.write(new Text(userId),new ServerCast(userId,serverName,cast));
            }
        }
    }
    public static class MyMapper extends Mapper<LongWritable,Text,Text,ServerCast>{
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

            String[] split = value.toString().split(",");
            if (split.length == 3){
                String userId = split[0];
                String serverName = split[1];
                long cast = Long.parseLong(split[2]);
                // 去除之前增加的随机值
                userId = userId.split("_")[0];
                context.write(new Text(userId),new ServerCast(userId,serverName,cast));
            }
        }
    }

    public static class MyReducer extends Reducer<Text,ServerCast,ServerCast,NullWritable>{
        @Override
        protected void reduce(Text key, Iterable<ServerCast> values, Context context) throws IOException, InterruptedException {

            TreeMap<ServerCast,Integer> treeMap = getTopNServerCase(values,context,"reducer");
            for (ServerCast serverCast:treeMap.keySet()){
                context.write(serverCast,NullWritable.get());
            }
        }
    }

    /**
     * Combine 输出 topN
     */
    public static class MyCombine extends Reducer<Text,ServerCast,Text,ServerCast>{
        @Override
        protected void reduce(Text key, Iterable<ServerCast> values, Context context) throws IOException, InterruptedException {

            TreeMap<ServerCast,Integer> treeMap = getTopNServerCase(values,context,"mapper");
            for (ServerCast serverCast:treeMap.keySet()){
                context.write(key,serverCast);
            }
        }
    }
    public static TreeMap<ServerCast,Integer> getTopNServerCase(Iterable<ServerCast> values, Reducer.Context context,
                                                                String process){
        int topN = Integer.valueOf(context.getConfiguration().get("topN"));
        TreeMap<ServerCast,Integer> treeMap = new TreeMap<ServerCast,Integer>(new Comparator<ServerCast>() {
            @Override
            public int compare(ServerCast o1, ServerCast o2) {
                return o1.compareTo(o2);
            }
        });

        int index = 0;
        for (ServerCast serverCast:values){
            index ++;
            String userId = serverCast.userId;
            String serverName = serverCast.serverName;
            long cast = serverCast.cast;
            treeMap.put(new ServerCast(userId,serverName,cast),1);
            if (treeMap.size() > topN){
                treeMap.remove(treeMap.lastKey());
            }
        }
        // 输出当前 reducer 中每个 key 的数量
        System.out.println(process + " count : " + index);
        return treeMap;
    }
}
