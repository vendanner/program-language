/*
类型转换
 */

// #![allow(overflowing_literals)]

// 给u64 取个别名(为冗长的类型取个简单的类型别名)
type Inch = u64;
fn main() {
    let a = 258;
    /*
     会报错，256 给u8 会溢出，overflowing_literals)
     如果设置 #![allow(overflowing_literals)]，能编译成功，输出0
     */
    // let b:u8 = a;
    // println!("num: {}", b);

    let c:u8 = a as u8;         // 正常做法，使用as 显式的将数字转换为u8
    println!("num: {}", c);

    /*
     此行，vec 是不知道Vec 存什么，所以 vec:Vec<?>（此时vec 不能用，因为存储的类型未确定）
     当 vec.push(2)，才确定vec 是什么类型的Vec => vec:Vec<i32>
     */
    let mut vec = Vec::new();
    vec.push(2);
    println!("{:?}", vec);

    let d: Inch = 3;
    println!("inch: {}", d);
}