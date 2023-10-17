/*
引入 garden 模块
    garden 模块 有子模块 vegetables

 */

use crate::garden::vegetables::Asparagus;

// 告诉编译器应该包含在 src/garden.rs 文件
pub mod garden;

fn main() {
    let plant = Asparagus {};
    println!("I'm growing {:?}", plant);
}