/*
debug 打印：{:?}
加关键字 derive，能打印任何类型
{:#?}：美化debug 打印
 */
#[derive(Debug)]
struct Structure(i32);

fn main() {
    // Now Structure(3) will print
    println!("Now {:?} will print", Structure(3));
    /*
    美化打印：
    Now Structure(
        3,
    ) will print
     */
    println!("Now {:#?} will print", Structure(3));
}