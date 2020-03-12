package com.danner.bigdata.hadoop.mapreduce.join;

import com.danner.bigdata.hadoop.mapreduce.utils.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.HashMap;

/**
 * mapper 端join，适用大文件 join 小文件
 * 小文件直接在 mapper 时读取
 *
 * 员工表和部门表 join
 */
public class MapperJoinApp {

    public static void main(String[] args) throws Exception {

        Configuration configuration = new Configuration();
        Job job = Job.getInstance(configuration);

        job.setJarByClass(MapperJoinApp.class);
        job.setMapperClass(MyMapper.class);

        job.setMapOutputKeyClass(Info.class);
        job.setMapOutputValueClass(NullWritable.class);

        // 第六步 设置输入输出路径
        String outputPath = "out/join";
        String inputPath = "input/join/emp.txt";
//        String inputPath = args[0];
//        String outputPath = args[1];
        FileUtils.clearFile(outputPath,configuration);

        FileInputFormat.setInputPaths(job,new Path(inputPath));
        FileOutputFormat.setOutputPath(job,new Path(outputPath));

        job.setNumReduceTasks(0);
        // 分布式缓存文件，类同 Spark 广播变量
        job.addCacheFile(new URI("input/join/dept.txt"));
        // 第七步  提交作业
        int status = job.waitForCompletion(true) ? 0 : 1;
        System.exit(status);
    }
    public static class MyMapper extends Mapper<LongWritable,Text,Info,NullWritable>{
        HashMap<Integer,String> dep = new HashMap<>();
        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            // 直接载入小文件，mapper 端join
            String filePath = context.getCacheFiles()[0].toString();
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath )));

            String line;
            while ((line = reader.readLine()) != null){
                // 解析部门表
                String[] splits = line.split(",");
                if (splits.length == 3){
                    dep.put(Integer.valueOf(splits[0]),splits[1]);
                }
            }

            IOUtils.closeStream(reader);
        }

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] splits = value.toString().split("\t");
            if (splits.length == 8){
                int empNo = Integer.valueOf(splits[0]);
                String empName = splits[1];
                int depNo = Integer.valueOf(splits[7]);
                String depName = dep.get(depNo);

                Info info = new Info();
                info.setEmpNo(empNo);
                info.setEmpName(empName);
                info.setDepNo(depNo);
                info.setDepName(depName);
                context.write(info,NullWritable.get());
            }
        }
    }
}
