package com.danner.bigdata.hadoop.mapreduce.outputformat;

import com.danner.bigdata.hadoop.mapreduce.utils.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * 自定义 outputformat
 */
public class App {

    public static void main(String[] args) throws Exception {

        Configuration configuration = new Configuration();
        Job job = Job.getInstance(configuration);

        String inputPath = "input/item.log";
        String outputPath = "out/wc";
//        String inputPath = args[0];
//        String outputPath = args[1];
        FileUtils.clearFile(outputPath,configuration);

        job.setJarByClass(App.class);

        job.setMapperClass(MyMapper.class);
        job.setReducerClass(MyReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(NullWritable.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(NullWritable.class);

        job.setOutputFormatClass(MyOutputFormat.class);

        FileInputFormat.setInputPaths(job,new Path(inputPath));
        FileOutputFormat.setOutputPath(job,new Path(outputPath));

        // 第七步  提交作业
        int status = job.waitForCompletion(true) ? 0 : 1;
        System.exit(status);
    }

    public static class MyMapper extends Mapper<LongWritable ,Text,Text,NullWritable>{
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            context.write(value,NullWritable.get());
        }
    }

    public static class MyReducer extends Reducer<Text,NullWritable,Text,NullWritable>{
        @Override
        protected void reduce(Text key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException {
            for (NullWritable value:values){
                context.write(new Text(key.toString()+"\r\n"),value);
            }
        }
    }

    /**
     * 自定义 outputformat,需要定制 RecordWriter
     */
    public static class MyOutputFormat extends FileOutputFormat<Text,NullWritable>{
        @Override
        public RecordWriter<Text, NullWritable> getRecordWriter(TaskAttemptContext job) throws IOException, InterruptedException {
            // 返回自定义 recordWrite 执行写操作
            return new MyRecord(FileSystem.get(job.getConfiguration()));
        }
    }

    /**
     * 自定义 RecordWrite
     */
    public static class MyRecord extends RecordWriter<Text, NullWritable>{

        private FileSystem fileSystem = null;
        FSDataOutputStream out1 = null;
        FSDataOutputStream out2 = null;

        public MyRecord(FileSystem fileSystem) throws IOException {
            this.fileSystem = fileSystem;
            out1 = fileSystem.create(new Path("out/item1.log"));
            out2 = fileSystem.create(new Path("out/item2.log"));
        }

        @Override
        public void write(Text key, NullWritable value) throws IOException, InterruptedException {
            if (key.toString().contains("香蕉")){
                out1.write(key.toString().getBytes());
            }else {
                out2.write(key.toString().getBytes());
            }
        }

        @Override
        public void close(TaskAttemptContext context) throws IOException, InterruptedException {
            out1.close();
            out2.close();
            fileSystem.close();
        }
    }
}
