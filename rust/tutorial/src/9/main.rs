/*
执行二进制文件时，添加"RUST_BACKTRACE=1" 可以打印堆栈: RUST_BACKTRACE=1 ./main
panic: 不可恢复的错误
Result：可恢复的错误
    unwrap: 错误直接 panic
    ? : 错误向上传播(提前结束)；函数返回值是Result/Option 才能用
 */
#![warn(unused_imports)]

use std::fs::File;
use std::io;
use std::io::{ErrorKind, Read};

/*
?: 发生Error 时直接返回error
 */
fn read_username_from_file() -> Result<String, io::Error> {
    let mut username = String::new();
    File::open("hello.txt")?.read_to_string(&mut username)?;
    Ok(username)
}

fn main() {
    // panic!("crash and burn");    // 手动触发 panic

    // let v = vec![1, 2, 3];
    // v[99];       // 会产生 panic

    // Result
    // let file_result = File::open("hello.txt");
    // let _file_p = match file_result {
    //     Ok(file) => file,
    //     Err(error) => match error.kind() {
    //         ErrorKind::NotFound => match File::create("hello.txt") {
    //             Ok(fc) => fc,
    //             Err(e) =>  panic!("Problem creating the file: {:?}", e),
    //         },
    //         other_error => {
    //             panic!("Problem opening the file: {:?}", other_error);
    //         }
    //     }
    // };

    // unwrap/expect: 返回文件句柄或调用 panic! 宏
    // let _greeting_file = File::open("hello.txt").unwrap();       // 简化代码，不用match
    // let _greeting_file = File::open("hello.txt").expect("file not found");  // 可以输出自定义的错误信息

}