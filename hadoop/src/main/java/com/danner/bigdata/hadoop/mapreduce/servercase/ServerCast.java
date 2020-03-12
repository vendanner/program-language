package com.danner.bigdata.hadoop.mapreduce.servercase;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class ServerCast implements WritableComparable<ServerCast> {

    public String userId;
    public String serverName;
    public long cast;

    public ServerCast(){

    }

    public ServerCast(String userId,String serverName,long cast){
        this.userId = userId;
        this.serverName = serverName;
        this.cast = cast;
    }
    @Override
    public void write(DataOutput out) throws IOException {
        out.writeUTF(userId);
        out.writeUTF(serverName);
        out.writeLong(cast);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        userId = in.readUTF();
        serverName = in.readUTF();
        cast = in.readLong();
    }

    @Override
    public String toString() {
        return userId + "," + serverName + "," + cast;
    }

    @Override
    public int compareTo(ServerCast o) {
        // 先比较 cast，再比较 serverName
        if (o == null){
            return -1;
        }else if (this.cast > o.cast){
            return -1;
        }else if (this.cast < o.cast){
            return 1;
        }else{
            return this.serverName.compareTo(o.serverName);
        }
    }
}
