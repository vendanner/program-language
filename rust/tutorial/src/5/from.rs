/*
from： 从int 中得到 Number
into： 将int 转变为 Number
 */
use std::convert::From;

#[derive(Debug)]
struct Number {
    value: i32,
}

// 为Num 实现From function
impl From<i32> for Number {
    fn from(it: i32) -> Self {
        Number {value: it}
    }
}

fn main() {
    let num = Number::from(1);  // from 将数字转成Number
    println!("{:?}", num.value);

    let num1: Number = 5.into();           // into 是from 的反方向，实现from 接口自动能用into
    println!("{:?}", num1.value);
}