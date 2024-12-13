import java.io.File;

var machines = mutableListOf<Triple<Pair<Double, Double>, Pair<Double, Double>, Pair<Double, Double>>>()
fun main(args : Array<String>) {
    val lines = File(args.first()).readLines()

    var aButton = Pair<Double, Double>(0.0, 0.0)
    var bButton = Pair<Double, Double>(0.0, 0.0)
    var prize = Pair<Double, Double>(0.0, 0.0)
    for (line in lines) {
      if (!line.isEmpty()) {
         if (line.contains("A: ")) {
            var parts = line.split(", ")
            var x = parts[0].split("+")[1].toDouble()
            var y = parts[1].split("+")[1].toDouble()
            aButton = Pair(x, y)
         } else if (line.contains("B: ")) {
            var parts = line.split(", ")
            var x = parts[0].split("+")[1].toDouble()
            var y = parts[1].split("+")[1].toDouble()
            bButton = Pair(x, y)
         } else {
            var parts = line.split(", ")
            var x = parts[0].split("=")[1].toDouble()
            var y = parts[1].split("=")[1].toDouble()
            prize = Pair(x, y)
         }
      } else {
         machines.add(Triple(aButton, bButton, prize))
      }
    }
    machines.add(Triple(aButton, bButton, prize))

    println("Solution 1: ${solution1()}")
    println("Solution 2: ${solution2()}")
}

private fun solution1() :Int {
   var cost = 0
   for (machine in machines) {
      if (pressA(machine) % 1 == 0.0 && pressB(machine) % 1 == 0.0) {
         cost += (pressB(machine) + (pressA(machine) * 3)).toInt()
      }
   }
   return cost
}

private fun solution2() :Long {
   var cost = 0L
   var newPrizePosition = 10000000000000.0
   for (m in machines) {
      // modify the prize coordinates
      var oldPrize = m.third
      var newPrize = Pair(newPrizePosition + oldPrize.first, newPrizePosition + oldPrize.second)
      var machine = Triple(m.first, m.second, newPrize)

      if (pressA(machine) % 1 == 0.0 && pressB(machine) % 1 == 0.0) {
         cost += (pressB(machine).toLong() + (pressA(machine) * 3).toLong())
      }
   }
   return cost
}

private fun pressB(machine: Triple<Pair<Double, Double>, Pair<Double, Double>, Pair<Double, Double>>): Double {
   var A = machine.first
   var B = machine.second
   var prize = machine.third
   return ((prize.second * A.first - prize.first * A.second)) / ((B.second * A.first - B.first * A.second)) 
}

private fun pressA(machine: Triple<Pair<Double, Double>, Pair<Double, Double>, Pair<Double, Double>>): Double {
   var A = machine.first
   var B = machine.second
   var prize = machine.third
   return (prize.first - (pressB(machine) * B.first)) / A.first
}