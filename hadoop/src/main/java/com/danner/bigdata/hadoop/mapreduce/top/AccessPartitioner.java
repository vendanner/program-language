package com.danner.bigdata.hadoop.mapreduce.top;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

class AccessPartitioner extends Partitioner<Access,Text> {

    @Override
    public int getPartition(Access access, Text text, int numReducers) {
        String phone = text.toString();
        if (phone.startsWith("13")){
            return 0;
        }
        else if( phone.startsWith("15")){
            return 1;
        }
        else{
            return 2;
        }
    }
}
