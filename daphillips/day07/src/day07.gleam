import common/string_utils
import gleam/int
import gleam/io
import gleam/list
import gleam/option
import gleam/pair
import gleam/result
import gleam/string

const debug = False

pub fn main() {
  let filename = "input.txt"
  let debug_lines =
    string_utils.lines_from_content(
      "190: 10 19
3267: 81 40 27
83: 17 5
156: 15 6
7290: 6 8 6 15
161011: 16 10 13
192: 17 8 14
21037: 9 7 18 13
292: 11 6 16 20",
    )

  let lines = case debug {
    True -> debug_lines
    False -> string_utils.lines_from_file(filename)
  }

  let tests =
    list.map(lines, fn(line) {
      let assert Ok(#(test_val, nums)) = string.split_once(line, ": ")
      let assert Ok(parsed_test_val) = int.parse(test_val)

      let nums =
        string.split(nums, " ")
        |> list.map(fn(n) {
          let assert Ok(num) = int.parse(n)
          num
        })
      #(parsed_test_val, nums)
    })

  tests
  |> list.filter(fn(t) {
    perform_operation(pair.first(t), option.None, pair.second(t))
  })
  |> list.fold(0, fn(acc, val) { acc + pair.first(val) })
  |> io.debug

  tests
  |> list.filter_map(fn(t) {
    case perform_operation_part2(pair.first(t), option.None, pair.second(t)) {
      True -> Ok(pair.first(t))
      False -> Error(Nil)
    }
  })
  |> list.reduce(fn(acc, val) { acc + val })
  |> result.lazy_unwrap(or: fn() { panic })
  |> io.debug
}

fn perform_operation(target, val, nums) {
  case val {
    option.Some(v) if v > target -> False
    option.Some(v) if v == target -> True
    _ -> {
      let val = option.unwrap(val, or: 0)
      case nums {
        [next, ..rest] -> {
          let add = val + next
          let mult = val * next

          perform_operation(target, option.Some(add), rest)
          || perform_operation(target, option.Some(mult), rest)
        }
        [] -> False
      }
    }
  }
}

fn perform_operation_part2(target, val, nums) {
  case val {
    option.Some(v) if v > target -> False
    _ -> {
      let val = option.unwrap(val, or: 0)
      case nums {
        [next, ..rest] -> {
          let add = val + next
          let mult = val * next
          let assert Ok(cat) =
            int.parse(int.to_string(val) <> int.to_string(next))

          perform_operation_part2(target, option.Some(add), rest)
          || perform_operation_part2(target, option.Some(mult), rest)
          || perform_operation_part2(target, option.Some(cat), rest)
        }
        [] -> target == val
      }
    }
  }
}
