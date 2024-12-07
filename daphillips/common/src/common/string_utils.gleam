import gleam/int
import gleam/io
import gleam/list
import gleam/string
import simplifile

pub fn read_file(filename) {
  let assert Ok(content) = simplifile.read(filename)
  content
}

pub fn lines_from_file(filename) {
  read_file(filename) |> lines_from_content
}

pub fn lines_from_content(content) {
  string.trim_end(content) |> string.split("\n")
}

pub fn line_to_ints(line, delim) {
  string.trim(line)
  |> string.split(delim)
  |> list.map(fn(n) {
    let assert Ok(parsed_num) = int.parse(n)
    parsed_num
  })
}
