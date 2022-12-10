fun main() {

    class Register(var value: Int) {
        val signalStrengths = mutableListOf<Int>()
        val queue = mutableListOf<Int>()

        fun executeQueue() {
            val queueElement = if (queue.size > 0) queue.removeFirst() else 0
            value += queueElement
        }

        fun checkSignalStrength(cycle: Int) {
            if (cycle != 0 && (cycle % 20 == 0 && cycle % 40 != 0)) {
                signalStrengths.add(cycle * value)
            }
        }
    }

    class Display(val height: Int, val width: Int) {
        val matrix = MutableList(this.height) { MutableList(this.width) { "░" } }

        fun getSpritePos(cycle: Int): Set<Int> {
            val spritePos = mutableSetOf<Int>()

            for (c in (cycle - 1)..(cycle + 1)) {
                spritePos.add((c - 1) % 40)
            }
            return spritePos
        }

        fun drawPixel(cycle: Int, registerValue: Int) {
            val currentX = (cycle - 1) % 40
            val currentY = (cycle - 1) / 40

            val spriteRange = getSpritePos(registerValue)

            if (spriteRange.contains(currentX)) {
                matrix[currentY][currentX] = "▓"
            }
        }

        fun printScreen() {
            matrix.forEach { println(it.joinToString(" ")) }
        }
    }

    fun part1(input: List<String>): Int {
        var cycle = 1
        val registerX = Register(1)

        for (line in input) {
            if (registerX.queue.size > 0) {
                registerX.checkSignalStrength(cycle)
                registerX.executeQueue()
                cycle++
            }

            val instructions = line.split(" ")
            val opcode = instructions[0]

            if (opcode == "addx") {
                val value = instructions[1].toInt()
                registerX.queue.add(value)
            }

            registerX.checkSignalStrength(cycle)
            cycle++
        }

        return registerX.signalStrengths.sum()
    }

    fun part2(input: List<String>) {
        var cycle = 1
        val registerX = Register(1)
        val display = Display(6, 40)

        for (line in input) {
            if (registerX.queue.size > 0) {
                registerX.executeQueue()
                display.drawPixel(cycle, registerX.value)
                cycle++
            }

            val instructions = line.split(" ")
            val opcode = instructions[0]

            if (opcode == "addx") {
                val value = instructions[1].toInt()
                registerX.queue.add(value)
            }

            display.drawPixel(cycle, registerX.value)
            cycle++

        }

        return display.printScreen()
    }

    printSolutionFromInputLines("Day10", ::part1)
    printSolutionFromInputLines("Day10", ::part2)
}


