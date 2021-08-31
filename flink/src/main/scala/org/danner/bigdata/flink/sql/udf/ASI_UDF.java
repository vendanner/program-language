package org.danner.bigdata.flink.sql.udf;

import org.apache.flink.table.functions.FunctionContext;
import org.apache.flink.table.functions.ScalarFunction;

public class ASI_UDF extends ScalarFunction {
    @Override
    public void open(FunctionContext context) throws Exception {
        super.open(context);
    }

    public String eval(String s, Integer begin, Integer end) {

        return s.substring(begin, end);
    }
}