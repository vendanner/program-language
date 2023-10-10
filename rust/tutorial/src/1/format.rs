use std::fmt::{self, Formatter, Display};

struct City {
    name: &'static str,
    lat: f32,
    lon: f32,
}

impl Display for City {
    fn fmt(&self, f: &mut Formatter<'_>) -> fmt::Result {
        let lat_c = if self.lat >= 0.0 { 'N'} else { 'S'};
        let lon_c = if self.lon >= 0.0 {'E'} else {'W'};
        write!(f, "{}: {:.3}{} {:.3}{}", self.name, self.lat.abs(), lat_c, self.lon.abs(), lon_c)
    }
}

#[derive(Debug)]
struct Color {
    red: u8,
    green: u8,
    blue: u8,
}

impl Display for Color {
    fn fmt(&self, f: &mut Formatter<'_>) -> fmt::Result {
        // let sum: u32 =  self.red as u32 * 65536 + self.green as u32 * 256 + self.blue as u32;
        write!(f, "RGB ({}, {}, {}) 0x{}{}{}", self.red, self.green, self.blue,
               get_x(self.red), get_x(self.green), get_x(self.blue))
    }
}

// 返回String(涉及所有权变更)， 而不是引用
fn get_x(value: u8) -> String {
    if value > 15 {
        format!("{:X}", value)
    } else if value > 0 {
        format!("{:0X}", value)
    } else {
        format!("{:02}", value)
    }
}

fn main() {
    for city in [
        City { name: "Dublin", lat: 53.347778, lon: -6.259722 },
        City { name: "Oslo", lat: 59.95, lon: 10.75 },
        City { name: "Vancouver", lat: 49.25, lon: -123.1 },
    ].iter() {
        println!("{}", city);
    }

    for color in [
        Color { red: 128, green: 255, blue: 90 },
        Color { red: 0, green: 3, blue: 254 },
        Color { red: 0, green: 0, blue: 0 },
    ].iter() {
        println!("{}", *color)
    }
}