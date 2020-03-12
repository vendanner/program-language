package com.danner.bigdata.hadoop.mapreduce.inputformat;

import com.danner.bigdata.hadoop.mapreduce.utils.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.CombineTextInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * 小文件 合并成一个输入(默认是在不超过块大小的文件，一个文件一个mapper)
 */
public class CombineFileInputFormatApp {

    public static void main(String[] args) throws Exception {

        // 第一步 定义 job
        Configuration configuration = new Configuration();
        Job job = Job.getInstance(configuration);

        String inputPath = "input/simplefile";
        String outputPath = "out/wc";
//        String inputPath = args[0];
//        String outputPath = args[1];
        FileUtils.clearFile(outputPath,configuration);

        // 第二步 添加 job jar
        job.setJarByClass(CombineFileInputFormatApp.class);

        // 第三步 添加 mapreduce jar
        job.setMapperClass(MyMapper.class);
        job.setReducerClass(MyReducer.class);

        // 第四步 设定 mapper 输出key value
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Access.class);

        // 第五步 设定 reducer 输出 key value
        job.setOutputKeyClass(NullWritable.class);
        job.setOutputValueClass(Access.class);

        // 输入 mapper 时，合并小文件;设定文件后文件大小
        // CombineTextInputFormat 工作步骤
        // 一：分割：小于maxSize 不用例会，大于 maxSize 但小于2*maxSize，平均分割成2个文件
        // 二：合并：合并文件直到超过 maxSize
        job.setInputFormatClass(CombineTextInputFormat.class);
        CombineTextInputFormat.setMaxInputSplitSize(job,1024*1024*8);

        // 第六步 设置输入输出路径
        FileInputFormat.setInputPaths(job,new Path(inputPath));
        FileOutputFormat.setOutputPath(job,new Path(outputPath));


        // 第七步  提交作业
        int status = job.waitForCompletion(true) ? 0 : 1;
        System.exit(status);
    }
    public static class MyMapper extends Mapper<LongWritable, Text, Text, Access> {
        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            System.out.println("MyMapper starting ...");
        }

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

            // 1363157985066 	13726230503	00-FD-07-A4-72-B8:CMCC	120.196.100.82	i02.c.aliimg.com
            // 24	27	2481	24681	200
            String[] split = value.toString().split("\t");
            if ( split.length > 9){
                String phone = split[1];
                long upload = Long.parseLong(split[8]);
                long down = Long.parseLong(split[9]);
                context.write(new Text(phone),new Access(phone,upload,down));
            }
        }
    }

    public static class MyReducer extends Reducer<Text, Access,NullWritable,Access> {
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
