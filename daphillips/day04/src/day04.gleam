import common/point
import common/string_utils
import gleam/dict
import gleam/io
import gleam/list
import gleam/option
import gleam/pair
import gleam/result
import gleam/string

const word = "XMAS"

const center = "A"

const debug = False

pub fn main() {
  let filename = "input.txt"
  let debug_lines =
    string_utils.lines_from_content(
      "MMMSXXMASM
MSAMXMSMSA
AMXSXMAAMM
MSAMASMSMX
XMASAMXAMM
XXAMMXXAMA
SMSMSASXSS
SAXAMASAAA
MAMMMXMMMM
MXMXAXMASX",
    )

  let first_char =
    string.pop_grapheme(word) |> result.unwrap(or: #("", "")) |> pair.first

  let lines = case debug {
    True -> debug_lines
    False -> string_utils.lines_from_file(filename)
  }

  // grid is a dict with key being row/col pairs and val being the char in the location
  let grid =
    list.index_map(lines, fn(row, r) {
      string.to_graphemes(row)
      |> list.index_map(fn(ch, c) { #(#(r, c), ch) })
    })
    |> list.flatten
    |> dict.from_list

  let starts =
    dict.filter(grid, fn(_point, ch) { ch == first_char }) |> dict.keys
  let part1 =
    list.map(starts, fn(s) { search(grid, s, word, option.None) })
    |> list.reduce(fn(acc, val) { acc + val })

  io.debug(part1)

  // part2
  let centers = dict.filter(grid, fn(_point, ch) { ch == center }) |> dict.keys

  list.map(centers, fn(c) {
    // hard coding corner checks because I don't care
    let bottom_right =
      dict.get(grid, point.translate(c, #(1, 1))) |> result.unwrap("")
    let bottom_left =
      dict.get(grid, point.translate(c, #(1, -1))) |> result.unwrap("")
    let top_right =
      dict.get(grid, point.translate(c, #(-1, 1))) |> result.unwrap("")
    let top_left =
      dict.get(grid, point.translate(c, #(-1, -1))) |> result.unwrap("")

    [top_left <> bottom_right, top_right <> bottom_left]
  })
  |> list.filter(fn(corners) {
    list.all(corners, fn(c) { c == "MS" || c == "SM" })
  })
  |> list.length
  |> io.debug
}

// TODO this needs to keep some sort of direction added to it!
fn search(grid, p, word, direction) {
  case string.pop_grapheme(word) {
    Error(_) -> {
      // we reached the end, so we must have found a match!
      1
    }
    // TODO just gotta fix this!
    Ok(#(ch, rest)) -> {
      case direction, dict.get(grid, p) |> result.unwrap(or: "") == ch {
        option.None, True ->
          list.map(point.directions, fn(d) {
            search(grid, point.translate(p, d), rest, option.Some(d))
          })
          // |> list.filter(fn(d) { d != 0 })
          // TODO fix this to return a correct number of things!
          // |> list.length
          |> list.reduce(fn(acc, val) { acc + val })
          |> result.unwrap(or: 0)
        option.Some(d), True ->
          search(grid, point.translate(p, d), rest, direction)
        // True -> list.any(point.neighbors(p), fn(n) { search(grid, n, rest) })
        _, False -> 0
      }
    }
  }
}
