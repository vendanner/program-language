use std::fmt;
use std::fmt::Formatter;

// 结构体定义
struct Rectangle {
    width: u32,
    height: u32,
}
// 结构体具体实现
impl Rectangle {

    // 结构体方法(实例方法)
    fn area(&self) -> u32 {
        self.width * self.height
    }

    // 等同于类方法(static function)，无需创建实例也可调用
    fn new(width: u32, height: u32) -> Self {
        Self {
            width,
            height,
        }
    }
}

impl fmt::Display for Rectangle {
    fn fmt(&self, f: &mut Formatter<'_>) -> fmt::Result {
        write!(f, "Rectangle: [{}, {}]", self.width, self.height)
    }
}

fn main() {
    let rect = Rectangle { width: 30, height: 50};
    println!("rect is {}", rect);
    println!(
        "The area of the rectangle is {} square pixels.",
        rect.area()
    );

    println!("rect is {}", Rectangle::new(10, 20));
}