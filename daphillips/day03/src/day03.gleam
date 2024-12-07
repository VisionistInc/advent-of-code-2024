import common/string_utils
import gleam/int
import gleam/io
import gleam/list
import gleam/option
import gleam/pair
import gleam/regexp
import gleam/result
import gleam/string

const debug = False

const part2_debug = True

pub fn main() {
  let filename = "input.txt"
  let file_line = string_utils.read_file(filename)

  let debug_line =
    "xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))"

  let debug_line_2 =
    "xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))"

  let line = case debug {
    True if part2_debug -> debug_line_2
    True -> debug_line
    False -> file_line
  }

  let assert Ok(part1_regex) = regexp.from_string("mul\\((\\d+),(\\d+)\\)")

  // part 1
  let part1 =
    regexp.scan(part1_regex, line)
    // |> io.debug
    |> list.map(fn(match) {
      list.map(match.submatches, fn(num_str) {
        // parse each number found in the match
        // TODO how to better hanlde optionals?
        let assert option.Some(n) = num_str
        let assert Ok(num) = int.parse(n)
        num
      })
    })
    |> list.map(fn(nums) {
      // reduce the inner list by getting the product
      let assert [first, last] = nums
      first * last
    })
    // reduce by sum
    |> list.reduce(fn(acc, val) { acc + val })

  io.debug(part1)
  // part 2
  // let assert Ok(part1_regex) = regexp.from_string("mul\\((\\d+),(\\d+)\\)")
  let assert [first, ..dont_splits] = string.split(line, "don't()")

  // only keep the dont splits that have a "do()" in them -- segments between donts with no do are dropped
  // this doesn't have the first, because that one is always do
  let splits =
    list.filter(dont_splits, fn(split) { string.contains(split, "do()") })

  let part2_line =
    // make sure we concat the start back in!
    first
    <> list.map(splits, fn(s) {
      // put the fallback in the second position for ones that don't have dos
      string.split_once(s, "do()")
      // first is the one we need to drop, second is the one we need to keep
      |> result.unwrap(or: #("", s))
      |> pair.second
    })
    |> list.reduce(fn(acc, val) { acc <> val })
    |> result.unwrap(or: "")

  regexp.scan(part1_regex, part2_line)
  |> list.map(fn(match) {
    list.map(match.submatches, fn(num_str) {
      // parse each number found in the match
      // TODO how to better hanlde optionals?
      let assert option.Some(n) = num_str
      let assert Ok(num) = int.parse(n)
      num
    })
  })
  |> list.map(fn(nums) {
    // reduce the inner list by getting the product
    let assert [first, last] = nums
    first * last
  })
  // reduce by sum
  |> list.reduce(fn(acc, val) { acc + val })
  |> io.debug
}

fn filter_donts(segments, keep_next) {
  // io.debug(segments)
  case segments {
    [s] if keep_next -> [s]
    [s, ..rest] if keep_next -> [s, ..filter_donts(rest, !keep_next)]
    [_, ..rest] -> filter_donts(rest, !keep_next)
    _ -> []
  }
  // [..next, ..filter_donts()]
}
