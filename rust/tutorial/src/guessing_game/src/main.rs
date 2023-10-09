use std::io;
use rand::Rng;
use std::cmp::Ordering;

fn main() {
    println!("Guess the number");

    let num = rand::thread_rng()
        .gen_range(1..=100);
    println!("The secret number: {num}");

    loop {
        println!("Please input your guess.");

        // let 创建变量
        // mut 表示变量可变(可重新赋值)
        let mut guess = String::new();

        io::stdin()
            .read_line(&mut guess)
            .expect("Failed to read line");

        let guess: u32 = match guess.trim().parse() {
            Ok(num) => num,
            Err(_) => continue
        };
        println!("You guessed: {guess}");

        match guess.cmp(&num) {
            Ordering::Less => println!("too small"),
            Ordering::Equal => {
                println!("you win");
                break;
            },
            Ordering::Greater => println!("too big")
        }
    }
}
