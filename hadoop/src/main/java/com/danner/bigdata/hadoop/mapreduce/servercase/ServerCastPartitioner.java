package com.danner.bigdata.hadoop.mapreduce.servercase;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

public class ServerCastPartitioner extends Partitioner<Text,ServerCast> {

    @Override
    public int getPartition(Text text, ServerCast serverCast, int numPartitions) {
        String userId = text.toString();

        return  userId.hashCode()%numPartitions;
//        if (userId.equals("10000")){
//            return 0;
//        }else if(userId.equals("10001")){
//            return 1;
//        }else{
//            return 2;
//        }
    }
}
