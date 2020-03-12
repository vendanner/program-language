package com.danner.bigdata.hadoop.mapreduce.join;

import com.danner.bigdata.hadoop.mapreduce.utils.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * reducer 端 join，部门编号为 key
 */
public class ReducerJoinApp {
    public static void main(String[] args) throws Exception {

        Configuration configuration = new Configuration();
        Job job = Job.getInstance(configuration);

        job.setJarByClass(ReducerJoinApp.class);
        job.setMapperClass(MyMapper.class);
        job.setReducerClass(MyReducer.class);

        job.setMapOutputKeyClass(IntWritable.class);
        job.setMapOutputValueClass(Info.class);
        job.setOutputKeyClass(Info.class);
        job.setOutputValueClass(NullWritable.class);

        // 第六步 设置输入输出路径
        String inputPath = "input/join/";
        String outputPath = "out/join";
//        String inputPath = args[0];
//        String outputPath = args[1];
        FileUtils.clearFile(outputPath,configuration);

        FileInputFormat.setInputPaths(job,new Path(inputPath));
        FileOutputFormat.setOutputPath(job,new Path(outputPath));

        int status = job.waitForCompletion(true) ? 0 : 1;
        System.exit(status);
    }
    public static class MyMapper extends Mapper<LongWritable ,Text,IntWritable,Info>{
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String fileName = ((FileSplit) context.getInputSplit()).getPath().getName();

            // 区分是那张表
            if (fileName.contains("em")){
                String[] splits = value.toString().split("\t");
                if (splits.length == 8){
                    int empNo = Integer.valueOf(splits[0]);
                    String empName = splits[1];
                    int depNo = Integer.valueOf(splits[7]);

                    Info info = new Info();
                    info.setEmpNo(empNo);
                    info.setEmpName(empName);
                    info.setDepNo(depNo);
                    info.setDepName("");
                    info.setFlag(1);
                    context.write(new IntWritable(depNo),info);
                }
            }else if (fileName.contains("dep")) {
                String[] splits = value.toString().split(",");
                if (splits.length == 3){
                    int depNo = Integer.valueOf(splits[0]);
                    String depName = splits[1];

                    Info info = new Info();
                    info.setEmpNo(0);
                    info.setEmpName("");
                    info.setDepNo(depNo);
                    info.setDepName(depName);
                    info.setFlag(2);
                    context.write(new IntWritable(depNo),info);
                }
            }
        }
    }

    public static class MyReducer extends Reducer<IntWritable,Info,Info,NullWritable>{
        @Override
        protected void reduce(IntWritable key, Iterable<Info> values, Context context) throws IOException, InterruptedException {
            // reducer 端 join，以key 为主键

            String depName = "";
            List<Info> list = new ArrayList<>();

            for (Info info :values){
                if (info.getFlag() == 1){
                    Info tmpInfo = new Info();
                    tmpInfo.setEmpNo(info.getEmpNo());
                    tmpInfo.setEmpName(info.getEmpName());
                    tmpInfo.setDepNo(info.getDepNo());
                    list.add(tmpInfo);
                }else if(info.getFlag() == 2){
                    depName =info.getDepName();
                }
            }
            for(Info info:list){
                info.setDepName(depName);
                context.write(info,NullWritable.get());
            }
        }
    }
}
