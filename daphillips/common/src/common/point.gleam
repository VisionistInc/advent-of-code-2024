import gleam/int
import gleam/option
import gleam/pair

pub type Point =
  #(Int, Int)

pub const directions: List(Point) = [
  #(1, 0), #(-1, 0), #(0, 1), #(0, -1), #(1, 1), #(1, -1), #(-1, 1), #(-1, -1),
]

pub fn neighbors(point: Point) {
  // TODO list vs tuple?
  [
    pair.map_first(point, fn(r) { r + 1 }),
    pair.map_first(point, fn(r) { r - 1 }),
    pair.map_second(point, fn(c) { c + 1 }),
    pair.map_second(point, fn(c) { c - 1 }),
    pair.map_first(point, fn(r) { r + 1 }) |> pair.map_second(fn(c) { c + 1 }),
    pair.map_first(point, fn(r) { r + 1 }) |> pair.map_second(fn(c) { c - 1 }),
    pair.map_first(point, fn(r) { r - 1 }) |> pair.map_second(fn(c) { c + 1 }),
    pair.map_first(point, fn(r) { r - 1 }) |> pair.map_second(fn(c) { c - 1 }),
  ]
}

pub fn cardinal_neighbors(point: Point) {
  [
    pair.map_first(point, fn(r) { r + 1 }),
    pair.map_first(point, fn(r) { r - 1 }),
    pair.map_second(point, fn(c) { c + 1 }),
    pair.map_second(point, fn(c) { c - 1 }),
  ]
}

pub fn translate(point: Point, translation: Point) {
  pair.map_first(point, fn(r) { r + pair.first(translation) })
  |> pair.map_second(fn(c) { c + pair.second(translation) })
}

pub fn at_boundary(point: Point, far_end: Point) {
  let row_zero = pair.first(point) == 0
  let col_zero = pair.second(point) == 0
  let row_far_end = pair.first(point) == pair.first(far_end)
  let col_far_end = pair.second(point) == pair.second(far_end)
  row_zero || col_zero || row_far_end || col_far_end
}

pub fn past_boundary(point: Point, far_end: Point) {
  let row_zero = pair.first(point) < 0
  let col_zero = pair.second(point) < 0
  let row_far_end = pair.first(point) > pair.first(far_end)
  let col_far_end = pair.second(point) > pair.second(far_end)
  row_zero || col_zero || row_far_end || col_far_end
}

pub fn manhattan_distance(point: Point, other: Point) {
  int.absolute_value(
    pair.first(point)
    - pair.first(other)
    + pair.second(point)
    - pair.second(other),
  )
}
