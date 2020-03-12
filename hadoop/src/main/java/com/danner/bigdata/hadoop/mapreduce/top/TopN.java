package com.danner.bigdata.hadoop.mapreduce.top;

import com.danner.bigdata.hadoop.mapreduce.utils.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class TopN {

    public static void main(String[] args) throws Exception {

        // 第一步 定义 job
        Configuration configuration = new Configuration();
        configuration.set("topN","3");
        Job job = Job.getInstance(configuration);

        String inputPath = "input/item.log";
        String outputPath = "out/item";
//        String inputPath = args[0];
//        String outputPath = args[1];
        FileUtils.clearFile(outputPath,configuration);

        // 第二步 添加 job jar
        job.setJarByClass(TopN.class);

        // 第三步 添加 mapreduce jar
        job.setMapperClass(MyMapper.class);
//        job.setReducerClass(MyReducer.class);
        job.setNumReduceTasks(0);

        // 第四步 设定 mapper 输出key value
        job.setMapOutputKeyClass(ItemInfo.class);
        job.setMapOutputValueClass(NullWritable.class);

        // 第五步 设定 reducer 输出 key value
//        job.setOutputKeyClass(ItemInfo.class);
//        job.setOutputValueClass(NullWritable.class);

        // 第六步 设置输入输出路径
        FileInputFormat.setInputPaths(job,new Path(inputPath));
        FileOutputFormat.setOutputPath(job,new Path(outputPath));

        // 第七步  提交作业
        int status = job.waitForCompletion(true) ? 0 : 1;
        System.exit(status);
    }

    /**
     * mapper 端直接选出 topN，节省传到 reducer 数据量
     */
    public static class MyMapper extends Mapper<LongWritable,Text,ItemInfo,NullWritable>{

        TreeMap<Integer,String> treeMap;
        int topN;

        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            topN = Integer.valueOf(context.getConfiguration().get("topN"));
            treeMap = new TreeMap<Integer,String>(new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    if(o1.equals(o2)){
                        return -1;
                    }else{
                        return o2 - o1;
                    }
                }
            });
        }

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] splits = value.toString().split(",");
            if(splits.length == 2){
                String name = splits[0];
                int price = Integer.valueOf(splits[1]);
                // key 相同，value 会被覆盖
                // 修改treeMap 中的key 比较方法compare,相同时当作小于
                // 但这个时候，不能用 get(key) 来取值，会出问题,因为equals 失效
                treeMap.put(price,name);
                if (treeMap.size() > topN ){
                    treeMap.pollLastEntry();

                }
            }
        }

        @Override
        protected void cleanup(Context context) throws IOException, InterruptedException {
            // 无法 get(key) 来取值 ，此时迭代取值
            for (Map.Entry<Integer,String> entry :treeMap.entrySet()){
                context.write(new ItemInfo(entry.getValue(),entry.getKey()),NullWritable.get());
            }
        }
    }
}
