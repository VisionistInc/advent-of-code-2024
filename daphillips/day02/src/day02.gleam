import common/string_utils
import gleam/io
import gleam/list
import gleam/pair

const debug = False

pub fn main() {
  let filename = "input.txt"
  let file_lines = string_utils.lines_from_file(filename)

  let debug_lines =
    string_utils.lines_from_content(
      "7 6 4 2 1
  1 2 7 8 9
  9 7 6 2 1
  1 3 2 4 5
  8 6 4 4 1
  1 3 6 7 9",
    )

  let lines = case debug {
    True -> debug_lines
    False -> file_lines
  }

  let part1 =
    list.map(lines, fn(line) { string_utils.line_to_ints(line, " ") })
    |> list.filter(is_monotonic)
    |> list.length

  io.debug(part1)

  let part2_fixed =
    list.map(lines, fn(line) { string_utils.line_to_ints(line, " ") })
    |> list.filter(fn(line) { !is_monotonic(line) })
    |> list.filter(fn(bad_line) {
      // fan out and not care about memory ftw!
      list.combinations(bad_line, list.length(bad_line) - 1)
      |> list.any(is_monotonic)
    })
    |> list.length

  let part2 = part1 + part2_fixed
  io.debug(part2)
}

fn is_monotonic(line) {
  let deltas =
    list.window_by_2(line) |> list.map(fn(p) { pair.first(p) - pair.second(p) })

  let all_decreasing =
    list.all(deltas, fn(d) { list.contains([-1, -2, -3], d) })
  let all_increasing = list.all(deltas, fn(d) { list.contains([1, 2, 3], d) })

  all_increasing || all_decreasing
}
