/*
常见集合
Vec
String
map
 */

use std::collections::HashMap;

fn main() {
    let mut v = vec![1, 2, 3]; // let v = Vec::new();
    v.push(5);

    println!("the third element is {}", v[2]);      // v[2] 最好用 &v[2]，表示引用，否则会获取所有权(基本类型除外)
    // let ee = v[10];     // v[10] 运行直接报错
    let ee = v.get(10);     // get 返回Option 类型
    println!("{:?}", ee);

    // String
    let data = "initial contents"; // 类型 &str
    let s = data.to_string();   // 类型 String
    let s = "initial contents".to_string();     // 类型 String

    let mut s = String::from("foo");
    s.push_str("bar");
    s.push('l');    // 添加字符

    let s1 = String::from("Hello, ");
    let s2 = String::from("world!");
    // let s3 = s1 + &s2;  // s1 所有权被转移，无法继续使用; s2 是引用
    let s4 = format!("{s1}{s2}");   // 多个string拼接，建议使用format，涉及的string 都是引用，不会转移所有权

    let sb = &s1[0..4];  // 获取s1 0-4 字节组成的str => 但一个字节对应一个ASCII 码
    // string 没有取第几个字符的功能

    // map
    let mut scores = HashMap::new();
    let key = String::from("Blue");
    let ss = scores.insert(key, 10);     // key 后续无法再使用，被移动了

    let val = scores.get(String::from("Blue")); // Option<&i32>
    let val = val.copied();     // copied 将Option<&i32> 转为 Option<i32>
    let val = val.unwrap_or(0); // 从Option 获取值，如果是None 填入默认值0

    // 如果有"Blue" 不更新，没有才更新; 返回 值的可变引用
    let old_value = scores.entry(String::from("Blue")).or_insert(50);
}