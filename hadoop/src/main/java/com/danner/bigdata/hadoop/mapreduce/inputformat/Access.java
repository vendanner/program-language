package com.danner.bigdata.hadoop.mapreduce.inputformat;

import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class Access implements Writable {

    public String phone;
    public long upload;
    public long down;
    public long sum;


    // 空的构造函数必须要，反序列化的时候反射使用
    public Access(){

    }

    public Access(String phone, long upload, long down){
        this.phone = phone;
        this.upload = upload;
        this.down = down;
        this.sum = upload + down;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeUTF(phone);
        out.writeLong(upload);
        out.writeLong(down);
        out.writeLong(sum);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        // 注意读取和写顺序必须相同
        phone = in.readUTF();
        upload = in.readLong();
        down = in.readLong();
        sum = in.readLong();
    }

    @Override
    public String toString() {
        return  phone + "\t" + upload + "\t" + down + "\t" + sum ;
    }

}
