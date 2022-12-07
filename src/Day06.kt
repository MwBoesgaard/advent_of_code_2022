fun main() {

    fun part1(input: String): Int {
        return input
            .windowed(4)
            .map {
                it
                    .split("")
                    .filter { it != "" }
                    .groupBy { it }
                    .size
            }
            .indexOf(4) + 4
    }

    fun part2(input: String): Int {
        return input
            .windowed(14)
            .map {
                it
                    .split("")
                    .filter { it != "" }
                    .groupBy { it }
                    .size
            }
            .indexOf(14) + 14
    }

    printSolutionFromInputRaw("Day06", ::part1)
    printSolutionFromInputRaw("Day06", ::part2)
}

