package com.danner.bigdata.hadoop.mapreduce.top;


import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class ItemInfo implements Writable {

    private String name;
    private int price;

    public ItemInfo(){

    }

    public ItemInfo(String name,int price){
        this.name = name;
        this.price = price;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeUTF(this.name);
        out.writeInt(price);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.name = in.readUTF();
        this.price = in.readInt();
    }

    @Override
    public String toString() {
        return name + "\t" + price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
