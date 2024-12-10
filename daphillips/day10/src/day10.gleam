import common/point
import common/string_utils
import gleam/dict
import gleam/int
import gleam/io
import gleam/list
import gleam/option
import gleam/pair
import gleam/result
import gleam/set
import gleam/string

const debug = False

pub fn main() {
  let filename = "input.txt"
  let debug_lines =
    string_utils.lines_from_content(
      "0123
1234
8765
9876",
    )

  let lines = case debug {
    True -> debug_lines
    False -> string_utils.lines_from_file(filename)
  }

  let height = list.length(lines) - 1
  // how can I neatly take the result of a pipeline and subtract with the operator instead of the function?
  let width =
    list.first(lines) |> result.unwrap("") |> string.length |> int.subtract(1)

  let far_end = #(height, width)

  let grid =
    list.index_map(lines, fn(row, r) {
      string.to_graphemes(row)
      |> list.index_map(fn(ch, c) {
        let assert Ok(h) = int.parse(ch)
        #(#(r, c), h)
      })
    })
    |> list.flatten
    |> dict.from_list

  let trailheads = dict.filter(grid, fn(_, g) { g == 0 }) |> dict.keys

  let part1 =
    list.map(trailheads, fn(trailhead) {
      trail_score(trailhead, grid, far_end)
      |> set.from_list
      |> set.delete(option.None)
      |> set.size
    })
    |> list.reduce(int.add)
    |> io.debug

  let part2 =
    list.map(trailheads, fn(trailhead) {
      trail_score_part2(trailhead, grid, far_end)
    })
    |> list.reduce(int.add)
    |> io.debug
}

fn trail_score(pos, grid, far_end) {
  case dict.get(grid, pos) {
    Error(_) -> panic
    Ok(9) -> [option.Some(pos)]

    _ -> {
      case point.past_boundary(pos, far_end) {
        True -> [option.None]
        False ->
          point.cardinal_neighbors(pos)
          |> list.filter(fn(n) {
            dict.get(grid, n) |> result.unwrap(or: -1) |> int.subtract(1)
            == dict.get(grid, pos) |> result.unwrap(or: -1)
          })
          |> list.flat_map(fn(n) { trail_score(n, grid, far_end) })
      }
    }
  }
}

// actually implemented part2 first, thinking that was what part 1 was supposed to be
fn trail_score_part2(pos, grid, far_end) {
  case dict.get(grid, pos) {
    Error(_) -> panic
    Ok(9) -> 1

    _ -> {
      case point.past_boundary(pos, far_end) {
        True -> 0
        False ->
          point.cardinal_neighbors(pos)
          |> list.filter(fn(n) {
            dict.get(grid, n) |> result.unwrap(or: -1) |> int.subtract(1)
            == dict.get(grid, pos) |> result.unwrap(or: -1)
          })
          |> list.fold(0, fn(acc, n) {
            acc + trail_score_part2(n, grid, far_end)
          })
      }
    }
  }
}
