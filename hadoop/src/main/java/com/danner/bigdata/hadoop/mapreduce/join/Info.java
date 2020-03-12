package com.danner.bigdata.hadoop.mapreduce.join;

import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class Info implements Writable {

    private int empNo;
    private String empName;
    private int depNo;
    private String depName;
    private int flag;

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeInt(empNo);
        out.writeUTF(empName);
        out.writeInt(depNo);
        out.writeUTF(depName);
        out.writeInt(flag);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.empNo = in.readInt();
        this.empName = in.readUTF();
        this.depNo = in.readInt();
        this.depName = in.readUTF();
        this.flag = in.readInt();
    }

    @Override
    public String toString() {
        return empNo + "\t" + empName + "\t" + depNo + "\t" + depName;
    }

    public int getEmpNo() {
        return empNo;
    }

    public void setEmpNo(int empNo) {
        this.empNo = empNo;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public int getDepNo() {
        return depNo;
    }

    public void setDepNo(int depNo) {
        this.depNo = depNo;
    }

    public String getDepName() {
        return depName;
    }

    public void setDepName(String depName) {
        this.depName = depName;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

}
