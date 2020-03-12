package com.danner.bigdata;

import com.danner.bigdata.hadoop.mapreduce.utils.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * 自定义 GroupingComparatorClass
 *
 */
public class App 
{

    public static void main(String[] args) throws Exception {
        Configuration configuration = new Configuration();
        Job job = Job.getInstance(configuration);

        String inputFile = "input/tmp.log";
        String outFile = "out/tmp";
        FileUtils.clearFile(outFile,configuration);
        FileInputFormat.setInputPaths(job,inputFile);
        FileOutputFormat.setOutputPath(job,new Path(outFile));

        job.setJarByClass(App.class);

        job.setMapperClass(MyMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(LongWritable.class);

        job.setReducerClass(MyReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);
        job.setGroupingComparatorClass(MyGroupingComparatorClass.class);

        System.exit(job.waitForCompletion(true) ? 0 : 1);

    }
    public static class MyMapper extends Mapper<LongWritable,Text,Text,LongWritable>{
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] splits = value.toString().split(",");
            if(splits.length == 2){
                context.write(new Text(splits[0]),new LongWritable(Long.parseLong(splits[1])));
            }
        }
    }

    public static class MyReducer extends Reducer<Text,LongWritable,Text,LongWritable>{
        @Override
        protected void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {
            long sum = 0;
            for (LongWritable longWritable :values){
                System.out.println(key.toString() + " ==> " + longWritable.get());
                sum += longWritable.get();
            }
            context.write(key,new LongWritable(sum));
        }
    }

    public static class MyGroupingComparatorClass extends WritableComparator {

        public MyGroupingComparatorClass(){
            // 必须先设置 WritableComparable 类型
            super(Text.class,true);
        }
        @Override
        public int compare(WritableComparable a, WritableComparable b) {
            Text wc1 = (Text)a;
            Text wc2 = (Text)b;

            return wc1.toString().split("_")[0].compareTo(wc2.toString().split("_")[0]);
        }
    }
}
