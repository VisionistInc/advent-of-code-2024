import gleam/int
import gleam/io
import gleam/list
import gleam/string
import simplifile

pub fn main() {
  let filename = "input.txt"
  let assert Ok(content) = simplifile.read(from: filename)

  let numbers =
    string.split(content, "\n")
    // drop empty at the end
    |> list.filter(fn(line) { line != "" })
    |> list.map(fn(l) {
      string.split(l, "   ")
      |> list.map(fn(n) {
        let assert Ok(parsed_num) = int.parse(n)
        parsed_num
      })
    })
    |> list.transpose()

  // TODO maybe a pattern match instead?
  let assert Ok(left) = list.first(numbers)
  let assert Ok(right) = list.last(numbers)

  let left = list.sort(left, int.compare)
  let right = list.sort(right, int.compare)

  let result =
    list.map2(left, right, fn(l, r) { int.absolute_value(l - r) })
    |> list.reduce(fn(acc, val) { acc + val })

  io.debug(result)

  // part 2
  let result2 =
    list.map(left, fn(l) { l * list.count(right, fn(r) { r == l }) })
    |> list.reduce(fn(acc, val) { acc + val })

  io.debug(result2)
}
