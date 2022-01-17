package org.danner.bigdata.flink.sql.explain;

/**
 * group by SQL 代码自动生成
 *
 * @author reese
 * @date 2021/09/06
 */
public final class GroupAggsHandler implements org.apache.flink.table.runtime.generated.AggsHandleFunction {
    int agg0_sum;
    boolean agg0_sumIsNull;
    long agg0_count;
    boolean agg0_countIsNull;
    private transient org.apache.flink.table.planner.functions.aggfunctions.MaxWithRetractAggFunction function_org$apache$flink$table$planner$functions$aggfunctions$MaxWithRetractAggFunction$d78f624eeff2a86742b5f64899608448;
    private transient org.apache.flink.table.runtime.typeutils.ExternalSerializer externalSerializer$2;
    private transient org.apache.flink.table.runtime.typeutils.ExternalSerializer externalSerializer$3;
    private org.apache.flink.table.runtime.dataview.StateMapView agg1$map_dataview;
    private org.apache.flink.table.data.binary.BinaryRawValueData agg1$map_dataview_raw_value;
    private org.apache.flink.table.runtime.dataview.StateMapView agg1$map_dataview_backup;
    private org.apache.flink.table.data.binary.BinaryRawValueData agg1$map_dataview_backup_raw_value;
    long agg2_count1;
    boolean agg2_count1IsNull;
    private transient org.apache.flink.table.data.conversion.StructuredObjectConverter converter$4;
    org.apache.flink.table.data.GenericRowData acc$6 = new org.apache.flink.table.data.GenericRowData(4);
    org.apache.flink.table.data.GenericRowData acc$7 = new org.apache.flink.table.data.GenericRowData(4);
    org.apache.flink.table.data.UpdatableRowData field$11;
    private org.apache.flink.table.data.RowData agg1_acc_internal;
    private org.apache.flink.table.planner.functions.aggfunctions.MaxWithRetractAggFunction.MaxWithRetractAccumulator agg1_acc_external;
    org.apache.flink.table.data.GenericRowData aggValue$41 = new org.apache.flink.table.data.GenericRowData(2);
    private org.apache.flink.table.runtime.dataview.StateDataViewStore store;

    public GroupAggsHandler(java.lang.Object[] references) throws Exception {
        function_org$apache$flink$table$planner$functions$aggfunctions$MaxWithRetractAggFunction$d78f624eeff2a86742b5f64899608448 = (((org.apache.flink.table.planner.functions.aggfunctions.MaxWithRetractAggFunction) references[0]));
        externalSerializer$2 = (((org.apache.flink.table.runtime.typeutils.ExternalSerializer) references[1]));
        externalSerializer$3 = (((org.apache.flink.table.runtime.typeutils.ExternalSerializer) references[2]));
        converter$4 = (((org.apache.flink.table.data.conversion.StructuredObjectConverter) references[3]));
    }

    private org.apache.flink.api.common.functions.RuntimeContext getRuntimeContext() {
        return store.getRuntimeContext();
    }

    @Override
    public void open(org.apache.flink.table.runtime.dataview.StateDataViewStore store) throws Exception {
        this.store = store;

        function_org$apache$flink$table$planner$functions$aggfunctions$MaxWithRetractAggFunction$d78f624eeff2a86742b5f64899608448.open(new org.apache.flink.table.functions.FunctionContext(store.getRuntimeContext()));

        agg1$map_dataview = (org.apache.flink.table.runtime.dataview.StateMapView) store.getStateMapView("agg1$map", false, externalSerializer$2, externalSerializer$3);
        agg1$map_dataview_raw_value = org.apache.flink.table.data.binary.BinaryRawValueData.fromObject(agg1$map_dataview);

        agg1$map_dataview_backup = (org.apache.flink.table.runtime.dataview.StateMapView) store.getStateMapView("agg1$map", false, externalSerializer$2, externalSerializer$3);
        agg1$map_dataview_backup_raw_value = org.apache.flink.table.data.binary.BinaryRawValueData.fromObject(agg1$map_dataview_backup);

        converter$4.open(getRuntimeContext().getUserCodeClassLoader());

    }

    /**
     * 累加计算
     * @param accInput
     * @throws Exception
     */
    @Override
    public void accumulate(org.apache.flink.table.data.RowData accInput) throws Exception {

        int field$13;
        boolean isNull$13;
        boolean isNull$14;
        int result$15;
        boolean isNull$18;
        long result$19;
        boolean isNull$21;
        long result$22;
        isNull$13 = accInput.isNullAt(1);
        field$13 = -1;
        if (!isNull$13) {
            field$13 = accInput.getInt(1);
        }

        int result$17 = -1;
        boolean isNull$17;
        if (isNull$13) {

            isNull$17 = agg0_sumIsNull;
            if (!isNull$17) {
                result$17 = agg0_sum;
            }
        } else {
            int result$16 = -1;
            boolean isNull$16;
            if (agg0_sumIsNull) {
                // sum 之前为 null，input 直接赋值
                isNull$16 = isNull$13;
                if (!isNull$16) {
                    result$16 = field$13;
                }
            } else {
                // sum 之前不为 null，input + agg0_sum
                isNull$14 = agg0_sumIsNull || isNull$13;
                result$15 = -1;
                if (!isNull$14) {
                    result$15 = (int) (agg0_sum + field$13);
                }

                isNull$16 = isNull$14;
                if (!isNull$16) {
                    result$16 = result$15;
                }
            }
            // result$17 保存最终 sum 结果
            isNull$17 = isNull$16;
            if (!isNull$17) {
                result$17 = result$16;
            }

        }
        // 到此处 sum 计算结束
        agg0_sum = result$17;
        agg0_sumIsNull = isNull$17;

        long result$20 = -1L;
        boolean isNull$20;
        if (isNull$13) {

            isNull$20 = agg0_countIsNull;
            if (!isNull$20) {
                result$20 = agg0_count;
            }
        } else {
            isNull$18 = agg0_countIsNull || false;
            result$19 = -1L;
            if (!isNull$18) {
                result$19 = (long) (agg0_count + ((long) 1L));

            }
            isNull$20 = isNull$18;
            if (!isNull$20) {
                result$20 = result$19;
            }
        }
        // 计算 sum 已累加的个数
        agg0_count = result$20;
        agg0_countIsNull = isNull$20;
        // 调用 max的 accumulate
        function_org$apache$flink$table$planner$functions$aggfunctions$MaxWithRetractAggFunction$d78f624eeff2a86742b5f64899608448.accumulate(agg1_acc_external, isNull$13 ? null : ((java.lang.Integer) field$13));

        isNull$21 = agg2_count1IsNull || false;
        result$22 = -1L;
        if (!isNull$21) {
            result$22 = (long) (agg2_count1 + ((long) 1L));
        }

        agg2_count1 = result$22;
        agg2_count1IsNull = isNull$21;

    }

    /**
     * 回撤计算
     * @param retractInput
     * @throws Exception
     */
    @Override
    public void retract(org.apache.flink.table.data.RowData retractInput) throws Exception {

        int field$23;
        boolean isNull$23;
        boolean isNull$24;
        int result$25;
        boolean isNull$26;
        int result$27;
        boolean isNull$30;
        long result$31;
        boolean isNull$33;
        long result$34;
        isNull$23 = retractInput.isNullAt(1);
        field$23 = -1;
        if (!isNull$23) {
            field$23 = retractInput.getInt(1);
        }

        int result$29 = -1;
        boolean isNull$29;
        if (isNull$23) {
            isNull$29 = agg0_sumIsNull;
            if (!isNull$29) {
                result$29 = agg0_sum;
            }
        } else {
            int result$28 = -1;
            boolean isNull$28;
            if (agg0_sumIsNull) {
                isNull$24 = false || isNull$23;
                result$25 = -1;
                if (!isNull$24) {
                    result$25 = (int) (((int) 0) - field$23);
                }
                isNull$28 = isNull$24;
                if (!isNull$28) {
                    result$28 = result$25;
                }
            } else {
                isNull$26 = agg0_sumIsNull || isNull$23;
                result$27 = -1;
                if (!isNull$26) {
                    result$27 = (int) (agg0_sum - field$23);
                }
                isNull$28 = isNull$26;
                if (!isNull$28) {
                    result$28 = result$27;
                }
            }
            isNull$29 = isNull$28;
            if (!isNull$29) {
                result$29 = result$28;
            }
        }
        // agg_sum = 之前的agg0_sum - input 值
        agg0_sum = result$29;
        agg0_sumIsNull = isNull$29;

        long result$32 = -1L;
        boolean isNull$32;
        if (isNull$23) {
            isNull$32 = agg0_countIsNull;
            if (!isNull$32) {
                result$32 = agg0_count;
            }
        } else {
            isNull$30 = agg0_countIsNull || false;
            result$31 = -1L;
            if (!isNull$30) {
                result$31 = (long) (agg0_count - ((long) 1L));
            }
            isNull$32 = isNull$30;
            if (!isNull$32) {
                result$32 = result$31;
            }
        }
        // 若input 不为null，agg_count-1
        agg0_count = result$32;
        agg0_countIsNull = isNull$32;
        // max retract
        function_org$apache$flink$table$planner$functions$aggfunctions$MaxWithRetractAggFunction$d78f624eeff2a86742b5f64899608448.retract(agg1_acc_external, isNull$23 ? null : ((java.lang.Integer) field$23));

        isNull$33 = agg2_count1IsNull || false;
        result$34 = -1L;
        if (!isNull$33) {
            result$34 = (long) (agg2_count1 - ((long) 1L));
        }

        agg2_count1 = result$34;
        agg2_count1IsNull = isNull$33;
    }

    @Override
    public void merge(org.apache.flink.table.data.RowData otherAcc) throws Exception {
        throw new java.lang.RuntimeException("This function not require merge method, but the merge method is called.");
    }

    /**
     * acc 初始化 agg0_sum、agg0_count、agg1_acc_internal、agg2_count1
     * @param acc
     * @throws Exception
     */
    @Override
    public void setAccumulators(org.apache.flink.table.data.RowData acc) throws Exception {

        int field$8;
        boolean isNull$8;
        long field$9;
        boolean isNull$9;
        org.apache.flink.table.data.RowData field$10;
        boolean isNull$10;
        long field$12;
        boolean isNull$12;
        isNull$8 = acc.isNullAt(0);
        field$8 = -1;
        if (!isNull$8) {
            field$8 = acc.getInt(0);
        }
        isNull$9 = acc.isNullAt(1);
        field$9 = -1L;
        if (!isNull$9) {
            field$9 = acc.getLong(1);
        }
        isNull$12 = acc.isNullAt(3);
        field$12 = -1L;
        if (!isNull$12) {
            field$12 = acc.getLong(3);
        }

        isNull$10 = acc.isNullAt(2);
        field$10 = null;
        if (!isNull$10) {
            field$10 = acc.getRow(2, 3);
        }
        field$11 = null;
        if (!isNull$10) {
            field$11 = new org.apache.flink.table.data.UpdatableRowData(
                    field$10,
                    3);

            agg1$map_dataview_raw_value.setJavaObject(agg1$map_dataview);
            field$11.setField(2, agg1$map_dataview_raw_value);
        }

        agg0_sum = field$8;
        agg0_sumIsNull = isNull$8;

        agg0_count = field$9;
        agg0_countIsNull = isNull$9;

        agg1_acc_internal = field$11;
        agg1_acc_external = (org.apache.flink.table.planner.functions.aggfunctions.MaxWithRetractAggFunction.MaxWithRetractAccumulator) converter$4.toExternal((org.apache.flink.table.data.RowData) agg1_acc_internal);

        agg2_count1 = field$12;
        agg2_count1IsNull = isNull$12;
    }

    @Override
    public void resetAccumulators() throws Exception {

        agg0_sum = ((int) -1);
        agg0_sumIsNull = true;

        agg0_count = ((long) 0L);
        agg0_countIsNull = false;

        agg1_acc_external = (org.apache.flink.table.planner.functions.aggfunctions.MaxWithRetractAggFunction.MaxWithRetractAccumulator) function_org$apache$flink$table$planner$functions$aggfunctions$MaxWithRetractAggFunction$d78f624eeff2a86742b5f64899608448.createAccumulator();
        agg1_acc_internal = (org.apache.flink.table.data.RowData) converter$4.toInternalOrNull((org.apache.flink.table.planner.functions.aggfunctions.MaxWithRetractAggFunction.MaxWithRetractAccumulator) agg1_acc_external);

        agg2_count1 = ((long) 0L);
        agg2_count1IsNull = false;
    }

    /**
     * 返回 acc
     * @return
     * @throws Exception
     */
    @Override
    public org.apache.flink.table.data.RowData getAccumulators() throws Exception {

        acc$7 = new org.apache.flink.table.data.GenericRowData(4);

        if (agg0_sumIsNull) {
            acc$7.setField(0, null);
        } else {
            acc$7.setField(0, agg0_sum);
        }

        if (agg0_countIsNull) {
            acc$7.setField(1, null);
        } else {
            acc$7.setField(1, agg0_count);
        }

        agg1_acc_internal = (org.apache.flink.table.data.RowData) converter$4.toInternalOrNull((org.apache.flink.table.planner.functions.aggfunctions.MaxWithRetractAggFunction.MaxWithRetractAccumulator) agg1_acc_external);
        if (false) {
            acc$7.setField(2, null);
        } else {
            acc$7.setField(2, agg1_acc_internal);
        }

        if (agg2_count1IsNull) {
            acc$7.setField(3, null);
        } else {
            acc$7.setField(3, agg2_count1);
        }

        return acc$7;
    }

    /**
     * 创建
     *
     * @return
     * @throws Exception
     */
    @Override
    public org.apache.flink.table.data.RowData createAccumulators() throws Exception {

        acc$6 = new org.apache.flink.table.data.GenericRowData(4);

        if (true) {
            acc$6.setField(0, null);
        } else {
            acc$6.setField(0, ((int) -1));
        }

        if (false) {
            acc$6.setField(1, null);
        } else {
            acc$6.setField(1, ((long) 0L));
        }

        org.apache.flink.table.data.RowData acc_internal$5 = (org.apache.flink.table.data.RowData) (org.apache.flink.table.data.RowData) converter$4.toInternalOrNull((org.apache.flink.table.planner.functions.aggfunctions.MaxWithRetractAggFunction.MaxWithRetractAccumulator) function_org$apache$flink$table$planner$functions$aggfunctions$MaxWithRetractAggFunction$d78f624eeff2a86742b5f64899608448.createAccumulator());
        if (false) {
            acc$6.setField(2, null);
        } else {
            acc$6.setField(2, acc_internal$5);
        }

        if (false) {
            acc$6.setField(3, null);
        } else {
            acc$6.setField(3, ((long) 0L));
        }

        return acc$6;
    }

    /**
     * 获取 agg
     * @return
     * @throws Exception
     */
    @Override
    public org.apache.flink.table.data.RowData getValue() throws Exception {

        boolean isNull$35;
        boolean result$36;

        aggValue$41 = new org.apache.flink.table.data.GenericRowData(2);

        isNull$35 = agg0_countIsNull || false;
        result$36 = false;
        if (!isNull$35) {
            result$36 = agg0_count == ((long) 0L);
        }

        int result$37 = -1;
        boolean isNull$37;
        if (result$36) {
            isNull$37 = true;
            if (!isNull$37) {
                result$37 = ((int) -1);
            }
        } else {
            isNull$37 = agg0_sumIsNull;
            if (!isNull$37) {
                result$37 = agg0_sum;
            }
        }
        if (isNull$37) {
            aggValue$41.setField(0, null);
        } else {
            aggValue$41.setField(0, result$37);
        }

        java.lang.Integer value_external$38 = (java.lang.Integer)
                function_org$apache$flink$table$planner$functions$aggfunctions$MaxWithRetractAggFunction$d78f624eeff2a86742b5f64899608448.getValue(agg1_acc_external);
        java.lang.Integer value_internal$39 =
                value_external$38;
        boolean valueIsNull$40 = value_internal$39 == null;

        if (valueIsNull$40) {
            aggValue$41.setField(1, null);
        } else {
            aggValue$41.setField(1, value_internal$39);
        }
        return aggValue$41;
    }

    @Override
    public void cleanup() throws Exception {
        agg1$map_dataview.clear();
    }

    @Override
    public void close() throws Exception {
        function_org$apache$flink$table$planner$functions$aggfunctions$MaxWithRetractAggFunction$d78f624eeff2a86742b5f64899608448.close();
    }
}



