use std::fmt;
use std::fmt::{Formatter};

struct Matrix(f32, f32, f32, f32);

impl fmt::Display for Matrix {
    fn fmt(&self, f: &mut Formatter<'_>) -> fmt::Result {
        write!(f, "( {} {} )\n( {} {} )", self.0, self.1, self.2, self.3)
    }
}

fn transpose(matrix: &Matrix) -> Matrix {
    let (f0, f1, f2, f3) = (matrix.0, matrix.1, matrix.2, matrix.3);    // 元组
    Matrix(f0, f2, f1, f3)
}
fn main() {
    let matrix = Matrix(1.1, 1.2, 2.1, 2.2);
    println!("Matrix:\n{}", matrix);
    println!("Transpose:\n{}", transpose(&matrix));
}