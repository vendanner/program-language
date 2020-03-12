package com.danner.bigdata.hadoop.mapreduce.wc;

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
 * 根据 access.log 文件统计用户流量
 */
public class FlowPartitionApp {

    public static void main(String[] args) throws Exception {

        // 第一步 定义 job
        Configuration configuration = new Configuration();
        Job job = Job.getInstance(configuration);

        String inputPath = "input/access.log";
        String outputPath = "out/wc";
//        String inputPath = args[0];
//        String outputPath = args[1];
        FileUtils.clearFile(outputPath,configuration);

        // 第二步 添加 job jar
        job.setJarByClass(FlowPartitionApp.class);
        // 第三步 添加 mapreduce jar
        job.setMapperClass(MyMapper.class);
        job.setReducerClass(MyReducer.class);

        // 第四步 设定 mapper 输出key value
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Access.class);

        // 第五步 设定 reducer 输出 key value
        job.setOutputKeyClass(NullWritable.class);
        job.setOutputValueClass(Access.class);

        // 第六步 设置输入输出路径
        FileInputFormat.setInputPaths(job,new Path(inputPath));
        FileOutputFormat.setOutputPath(job,new Path(outputPath));

        // 设置 PartitionerClass ,必须设置 NumReduceTasks 才有效果
        job.setPartitionerClass(AccessPartitioner.class);
        // NumReduceTasks 要大于等于 PartitionerClass 中的分区数；多余的文件输出空
        //  1 < NumReduceTasks < PartitionerClass ;出错，有些分区没有 reducer 任务执行
        // 1 = NumReduceTasks 强制 输出一个文件
        // 分区数和 reducer 任务数要匹配
        job.setNumReduceTasks(1);


        // 第七步  提交作业
        int status = job.waitForCompletion(true) ? 0 : 1;
        System.exit(status);
    }
    public static class MyMapper extends Mapper<LongWritable, Text, Text, Access> {
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

            // 1363157985066 	13726230503	00-FD-07-A4-72-B8:CMCC	120.196.100.82	i02.c.aliimg.com
            // 24	27	2481	24681	200
            String[] split = value.toString().split("\t");
            String phone = split[1];
            long upload = Long.parseLong(split[8]);
            long down = Long.parseLong(split[9]);
            context.write(new Text(phone),new Access(phone,upload,down));
        }
    }

    public static class MyReducer extends Reducer<Text, Access,NullWritable,Access>{
        @Override
        protected void reduce(Text key, Iterable<Access> values, Context context) throws IOException, InterruptedException {

            String phone = key.toString();
            long upload = 0;
            long down = 0;

            for(Access access:values){
                upload +=  access.upload;
                down += access.down;
            }
            context.write(NullWritable.get(),new Access(phone,upload,down));
        }
    }
}
