import gleam/bool
import gleam/dict
import gleam/int
import gleam/io
import gleam/list
import gleam/order
import gleam/set
import gleam/string
import simplifile

pub fn main() {
  let filename = "input.txt"
  let assert Ok(content) = simplifile.read(from: filename)

  // let content =
  //   "47|53
  // 97|13
  // 97|61
  // 97|47
  // 75|29
  // 61|13
  // 75|53
  // 29|13
  // 97|29
  // 53|29
  // 61|53
  // 97|53
  // 61|29
  // 47|13
  // 75|47
  // 97|75
  // 47|61
  // 75|61
  // 47|29
  // 75|13
  // 53|13

  // 75,47,61,53,29
  // 97,61,53,29,13
  // 75,29,13
  // 75,97,47,61,53
  // 61,13,29
  // 97,13,75,29,47"

  // TODO drop end???
  let lines = string.split(content, "\n")
  let first = list.take_while(lines, fn(l) { l != "" })
  let assert [_, ..second] = list.drop_while(lines, fn(l) { l != "" })

  // TODO consider split_while
  // TODO consider making this a tuple so that we can filter by key???
  let rules = {
    let assert [a, b] =
      list.map(first, fn(l) {
        string.split(l, "|")
        |> list.map(fn(n) {
          let assert Ok(parsed_num) = string.trim(n) |> int.parse
          parsed_num
        })
      })
      // |> io.debug
      |> list.transpose()
    list.zip(a, b)
  }
  // |> dict.from_list

  let updates =
    list.map(second, fn(l) {
      string.split(l, ",")
      |> list.map(fn(n) {
        let assert Ok(parsed_num) = string.trim(n) |> int.parse
        parsed_num
      })
    })

  let part1 =
    list.filter(updates, fn(up) { check_update(up, rules) })
    |> list.map(fn(working) {
      let assert [middle, ..] = list.drop(working, list.length(working) / 2)
      middle
    })
    |> list.reduce(fn(acc, val) { acc + val })

  io.debug(part1)

  let part2 =
    list.filter(updates, fn(up) {
      // keep the errors
      bool.negate(check_update(up, rules))
    })
    |> list.map(fn(up) {
      list.sort(up, fn(p1, p2) {
        let p2_deps_p1 = list.key_filter(rules, p1) |> list.contains(p2)
        let p1_deps_p2 = list.key_filter(rules, p2) |> list.contains(p1)

        case p2_deps_p1, p1_deps_p2 {
          True, _ -> order.Lt
          _, True -> order.Gt
          _, _ -> order.Eq
        }
      })
    })
    |> list.map(fn(working) {
      let assert [middle, ..] = list.drop(working, list.length(working) / 2)
      middle
    })
    |> list.reduce(fn(acc, val) { acc + val })

  io.debug(part2)
}

fn check_update(update: List(Int), rules: List(#(Int, Int))) -> Bool {
  case update {
    [] | [_] -> True
    [curr, next] -> {
      // we want to return true iff we don't have the missing thing here
      bool.negate(list.key_filter(rules, next) |> list.contains(curr))
    }
    // _ -> False
    [curr, next, ..rest] -> {
      case bool.negate(list.key_filter(rules, next) |> list.contains(curr)) {
        // make sure to add back in the one we used to check since it's next
        True -> check_update([next, ..rest], rules)
        False -> False
      }
    }
  }
}

fn fix_errors(update: List(Int), rules: List(#(Int, Int))) -> List(Int) {
  todo
  // case update_rev {
  //   [] -> []
  //   [first] -> [first]
  //   [curr, ..rest] -> {
  //     // if curr depends on all the 
  //     case list.key_filter(rules, next) |> list.contains(curr) {
  //       True -> [next, curr]
  //       False -> [curr, next]
  //     }

  //     todo
  //   }
  //   // [curr, next] -> {
  //   //   case list.key_filter(rules, next) |> list.contains(curr) {
  //   //     True -> [next, curr]
  //   //     False -> [curr, next]
  //   //   }
  //   // }
  //   // [curr, next, ..rest]
  // }
}
