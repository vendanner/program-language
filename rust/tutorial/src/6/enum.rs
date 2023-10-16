/*
枚举：特殊的struct
Option：特殊的枚举
 */
// 该属性用于隐藏对未使用代码的警告
#[warn(dead_code)]
#[warn(unused_variables)]
// 标准库提供的Ip 枚举值，IpAddr
// 拥有隐式辨别值（从 0 开始）
enum _IpAddKind {
    V4(u8, u8, u8, u8),
    V6(String),
}

fn main() {
    let some_number = Some(5);
    let _some_char = Some('2');
    let _absent_number: Option<i32> = None;
    println!("some number: {:?}", some_number);
}