package com.danner.bigdata.hadoop.homework;

import com.alibaba.fastjson.JSON;
import com.danner.bigdata.hadoop.mapreduce.utils.FileUtils;
import com.danner.bigdata.utils.IpUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
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
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ETLApp {
    public static void main(String[] args) throws Exception {
        if (args.length != 4){
            System.out.println("need four arg ...");
            System.exit(1);
        }
        for(String arg:args){
            System.out.println(arg);
        }


        Configuration configuration = new Configuration();
        Job job = Job.getInstance(configuration);

        job.setJarByClass(ETLApp.class);
        job.setMapperClass(MyMapper.class);

        job.setMapOutputKeyClass(AccessLog.class);
        job.setMapOutputValueClass(NullWritable.class);

        // 第六步 设置输入输出路径
//        String outputPath = "out/join";
//        // 压缩文件在 FileInputFormat 读取文件时会根据后缀自动解压缩
//        String inputPath = "input/FlumeData.1568776321547.bz2";
        String inputPath = args[1];
        String outputPath = args[2];
        FileUtils.clearFile(outputPath,configuration);

        FileInputFormat.setInputPaths(job,new Path(inputPath));
        FileOutputFormat.setOutputPath(job,new Path(outputPath));

        job.setNumReduceTasks(0);
        job.addCacheFile(new URI(args[3]));

        // 第七步  提交作业
        int status = job.waitForCompletion(true) ? 0 : 1;

        long total = job.getCounters().findCounter("etl", "total").getValue();
        long used = job.getCounters().findCounter("etl", "used").getValue();
        System.out.println("total : " + total + " ; used " + used);
        System.exit(status);
    }
    public static class MyMapper extends Mapper<LongWritable,Text,AccessLog,NullWritable> {

        private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        private ArrayList<IpUtil.IpInfo> ipInfos = new ArrayList<>(1000);

        @Override
        protected void setup(Context context) throws IOException {
            // 直接载入小文件，mapper 端join
            String filePath = context.getCacheFiles()[0].toString();
            FileSystem fileSystem =  FileSystem.get(context.getConfiguration());
            FSDataInputStream fis = fileSystem.open(new Path(filePath));
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

            String line;
            while ((line = reader.readLine()) != null){
                // 解析ip
                // 1.0.1.0|1.0.3.255|16777472|16778239|亚洲|中国|福建|福州||电信|350100|China|CN|119.306239|26.075302
                String[] splits = line.split("\\|");
                if (splits.length == 15){
                    IpUtil.IpInfo ipInfo = new IpUtil.IpInfo();
                    ipInfo.setStart(Long.parseLong(splits[2]));
                    ipInfo.setEnd(Long.parseLong(splits[3]));
                    ipInfo.setProvince(splits[6]);
                    ipInfo.setCity(splits[7]);
                    ipInfo.setIsp(splits[9]);
                    ipInfos.add(ipInfo);
                }
            }

            IOUtils.closeStream(reader);
            IOUtils.closeStream(fis);
        }

        @Override
        protected void map(LongWritable key, Text value, Context context)  {
            // 记录数据质量 = 能用的数据/总的数据
            context.getCounter("etl","total").increment(1);

            /**
             * 数据清洗相关的字段
             * ip==> provice  city isp
             * time => year month day
             */
            try{
                String log = value.toString();
                AccessLog accessLog = JSON.parseObject(log, AccessLog.class);

                long size = Long.parseLong(accessLog.getTraffic());
                accessLog.setSize(size);

                Date date = format.parse(accessLog.getTime());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                String y = calendar.get(Calendar.YEAR) + "";
                String m = calendar.get(Calendar.MONTH) + 1 + "";
                String d = calendar.get(Calendar.DATE) + "";
                accessLog.setY(y);
                accessLog.setM(m);
                accessLog.setD(d);

                // ip
                IpUtil.IpInfo ipInfo = IpUtil.searchIp(ipInfos, accessLog.getIp());
                accessLog.setIsp(ipInfo.getIsp());
                accessLog.setProvince(ipInfo.getProvince());
                accessLog.setCity(ipInfo.getCity());

                context.getCounter("etl","used").increment(1);
                context.write(accessLog,NullWritable.get());

            }catch (Exception e){

            }
        }
    }
}
