package com.danner.springboot.app.ch03.mapper;


import com.danner.springboot.app.ch03.model.MyBatisCoffee;
import org.apache.ibatis.annotations.*;


/**
 * 定义 coffee 这个 model 的操作
 * @Mapper  Mybatis 注解，自动生成  mapper
 * @Insert  insert 语句
 * @Select  select 语句
 * @Results 返回包装
 */
@Mapper
public interface CoffeeMapper {
    @Insert("insert into t_coffee (name, price, create_time, update_time)"
            + "values (#{name}, #{price}, now(), now())")
    @Options(useGeneratedKeys = true)
    int save(MyBatisCoffee coffee);

    @Select("select * from t_coffee where id = #{id}")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "create_time", property = "createTime"),
            // map-underscore-to-camel-case = true 可以实现一样的效果
            // @Result(column = "update_time", property = "updateTime"),
    })
    MyBatisCoffee findById(@Param("id") Long id);
}
