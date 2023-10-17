/*
没有包含 main 函数，是一个lib(库)
编译命令：rustc --crate-type=lib lib/library.rs
生成 liblibrary.rlib => 可以使用 crate_name 修改编译后的名称
 */
pub fn public_function() {
    println!("called rary's public_function()");
}

fn private_function() {
    println!("called rary's private_function()");
}

pub fn indirect_access() {
    println!("called rary's indirect_access, that\n");
    private_function();
}