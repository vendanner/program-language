-- 煤阀开度 = 工艺算法(每分钟 (根据5分钟内 kv 测点数据)平均值 )
insert into MySinkTable
  select
  stove_id,
  HOP_ROWTIME(ts, Interval '1' minute, interval '5' minute ) as ts,
  gas_funs(AVG(温度),avg(煤气),avg(瓦斯)) as gas_flow
  from MySourceTable
  group by stove_id,HOP_ROWTIME(ts, Interval '1' minute, interval '5' minute )


--  求 14 天内 cnt 之和
sum(cnt) over(partition by city order by range between interval '14' day preceding and current row)


