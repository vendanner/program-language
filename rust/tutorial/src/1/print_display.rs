/*
https://rustwiki.org/zh-CN/rust-by-example/hello/print/print_display.html
display 打印：{}
 */
use std::fmt;

#[derive(Debug)]
struct Complex {
    real : f64,
    imag : f64,
}

// 为了使用 `{}` 标记，必须手动为类型实现 `fmt::Display` trait。
// 自定义Complex 的Display
impl fmt::Display for Complex {
    // 这个 trait 要求 `fmt` 使用与下面的函数完全一致的函数签名
    fn fmt(&self, f: &mut fmt::Formatter) -> fmt::Result {
        write!(f, "{} + {}i", self.real, self.imag)
    }
}

fn main() {
    let com = Complex{real:3.2, imag:2.2};
    println!("Complex Display:");
    println!("Display: {}", com);
    println!("Debug: {:?}", com);
}