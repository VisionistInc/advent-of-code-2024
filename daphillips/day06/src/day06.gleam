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
      "....#.....
.........#
..........
..#.......
.......#..
..........
.#..^.....
........#.
#.........
......#...",
    )

  // let debug_lines =
  //   string_utils.lines_from_content(
  //     ".#....
  //   .....#
  //   #..#..
  //   ..#...
  //   .^...#
  //   ....#.",
  //   )

  //   let debug_lines =
  //     string_utils.lines_from_content(
  //       "...........
  // .......#...
  // ........#..
  // ...........
  // .^....#....
  // .......#...
  // ...........
  // ",
  //     )

  let lines = case debug {
    True -> debug_lines
    False -> string_utils.lines_from_file(filename)
  }

  let points_list =
    list.index_map(lines, fn(row, r) {
      string.trim(row)
      |> string.to_graphemes
      |> list.index_map(fn(ch, c) { #(#(r, c), ch) })
    })
    // |> io.debug
    |> list.flatten
    |> list.filter(fn(g) {
      let #(_, ch) = g
      ch != "."
    })

  let assert Ok(start_point) =
    list.find(points_list, fn(p) { pair.second(p) == "^" })

  // io.debug(start_point)

  let start = pair.first(start_point)

  let grid =
    list.filter(points_list, fn(p) { pair.second(p) != "^" })
    |> dict.from_list
  // |> io.debug

  let height = list.length(lines) - 1
  // how can I neatly take the result of a pipeline and subtract with the operator instead of the function?
  let width =
    list.first(lines) |> result.unwrap("") |> string.length |> int.subtract(1)

  let start_dir = #(-1, 0)
  let far_end = #(height, width)

  // part 1
  let visited = move(grid, start, start_dir, far_end, dict.new())
  // |> io.debug
  // |> dict.size
  // |> io.debug
  visited |> dict.size |> io.debug

  // part 2
  // 954 - too low
  dict.keys(grid)
  |> move_part2(start, start_dir, far_end, dict.new(), dict.new())
  // |> dict.keys
  // |> dict.size
  |> io.debug
}

// it needs to be a set of things that hold visited points
fn move(grid, pos, dir, far_end, visited) {
  let visited = dict.insert(visited, pos, dir)
  let at_boundary = point.at_boundary(pos, far_end)
  // TODO check for colledies by seeing if any of the points are in line with the current point
  case at_boundary {
    True -> visited
    False -> {
      // TOOD check collision first
      let proposed_next = point.translate(pos, dir)
      let #(dir, next) = case dict.has_key(grid, proposed_next) {
        False -> #(dir, proposed_next)
        // since translation is associative, having the ordering bad here doesn't matter
        True -> {
          let new_dir = rotate(dir)
          let next = point.translate(pos, new_dir)
          #(new_dir, next)
        }
      }
      // io.debug(pos)
      // io.debug("TO:")
      // io.debug(next)
      move(grid, next, dir, far_end, visited)
    }
  }
}

fn move_part2(
  obstacles: List(point.Point),
  pos: point.Point,
  dir: point.Point,
  far_end: point.Point,
  visited: dict.Dict(point.Point, set.Set(point.Point)),
  known_cycles,
) {
  // at my given point, will a rotation result in me going to a previously visited point and direction without hitting something else?
  // dict.upsert
  // let visited = dict.insert(visited, pos, dir)
  let visited =
    dict.upsert(visited, pos, fn(d) {
      case d {
        option.Some(dirs) -> set.insert(dirs, dir)
        // Some(dirs) -> list.append(dirs, dir)
        // None -> [dir]
        option.None -> set.new() |> set.insert(dir)
      }
    })

  // io.debug("VISIT")
  // io.debug(#(pos, dir))

  let next_dir = rotate(dir)
  // let next_rotated_point = point.translate(pos, next_dir)

  // TODO we need to check if one is on the path!

  // io.debug("CHECK CYCLE AT")
  // io.debug([pos, dir, next_dir])
  // io.debug(visited)
  // io.debug(dict.get(visited, pos))

  // we can't place an obstacle on a place we've already been
  let possible_cycle = case dict.has_key(visited, point.translate(pos, dir)) {
    True -> 0
    False ->
      check_possible_cycle(
        obstacles,
        pos,
        next_dir,
        far_end,
        visited,
        // dict.new() |> dict.insert(pos, set.new() |> set.insert(dir)),
      )
  }

  let at_boundary = point.at_boundary(pos, far_end)

  case at_boundary {
    True -> 0
    False -> {
      // io.debug(pos)
      // io.debug(possible_cycle)
      // let visited = dict.insert(visited, pos, dir)
      let proposed_next = point.translate(pos, dir)
      let #(dir, next) = case list.contains(obstacles, proposed_next) {
        False -> #(dir, proposed_next)
        True -> {
          #(next_dir, pos)
        }
      }

      // run the upsert again to account for direction-finding
      let visited =
        dict.upsert(visited, pos, fn(d) {
          case d {
            option.Some(dirs) -> set.insert(dirs, dir)
            // Some(dirs) -> list.append(dirs, dir)
            // None -> [dir]
            option.None -> set.new() |> set.insert(dir)
          }
        })

      // let possible_cycle = case
      //   dict.has_key(visited, point.translate(pos, dir))
      // {
      //   True -> 0
      //   False ->
      //     check_possible_cycle(
      //       obstacles,
      //       pos,
      //       dir,
      //       far_end,
      //       visited,
      //       // dict.new() |> dict.insert(pos, set.new() |> set.insert(dir)),
      //     )
      // }

      possible_cycle
      + move_part2(obstacles, next, dir, far_end, visited, known_cycles)
    }
  }
}

fn rotate(dir: point.Point) {
  // io.debug(dir)
  case dir {
    // down -> left
    #(1, 0) -> #(0, -1)
    // up -> right
    #(-1, 0) -> #(0, 1)
    // right -> down
    #(0, 1) -> #(1, 0)
    // left, up
    #(0, -1) -> #(-1, 0)
    _ -> panic
    // should never happen
  }
}

fn check_possible_cycle(obstacles, pos, dir, far_end, visited) {
  // io.debug([pos, dir])
  let at_boundary = point.at_boundary(pos, far_end)
  let on_obstacle = list.contains(obstacles, pos)

  // io.debug(list.contains(obstacles, #(4, 2)))

  let on_visited = case dict.get(visited, pos) {
    Ok(dirs) -> set.contains(dirs, dir)
    // Ok(d) if d == dir -> True
    _ -> False
  }

  let visited =
    dict.upsert(visited, pos, fn(d) {
      case d {
        option.Some(dirs) -> set.insert(dirs, dir)
        // Some(dirs) -> list.append(dirs, dir)
        // None -> [dir]
        option.None -> set.new() |> set.insert(dir)
      }
    })

  // io.debug(pos)

  case at_boundary || on_obstacle, on_visited {
    True, _ -> {
      0
    }
    _, True -> {
      // io.debug("YESSSSSSSSSSSSS")
      // io.debug(visited)
      // io.debug(dict.keys(visited))
      // io.debug(pos)
      // io.debug(dir)
      1
    }
    False, False -> {
      let proposed_next = point.translate(pos, dir)
      let #(dir, next) = case list.contains(obstacles, proposed_next) {
        False -> #(dir, proposed_next)
        True -> {
          let new_dir = rotate(dir)
          #(new_dir, pos)
        }
      }
      check_possible_cycle(obstacles, next, dir, far_end, visited)
    }
  }
}
