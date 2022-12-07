fun main() {

    fun extractCargoLines(input: List<String>): Pair<List<String>, Pair<Int, Int>> {
        val cargoLines: MutableList<String> = mutableListOf()
        var numOfCargoCols = 0
        var numOfRowsProcessed = 0
        for ((i, row) in input.withIndex()) {
            if (row[1] == '1') {
                numOfCargoCols = row.last().toString().toInt()
                numOfRowsProcessed = i
                break
            }
            val filteredRow = row
                .replace("    ", "x")
                .filter { c -> c.isLetter() }

            cargoLines.add(filteredRow)
        }
        return Pair(cargoLines, Pair(numOfCargoCols, numOfRowsProcessed))
    }

    fun getInitialCargoSetup(input: List<String>): Pair<MutableList<MutableList<String>>, Int> {
        val (cargoLines, numberPair) = extractCargoLines(input)
        val numOfCargoCols = numberPair.first
        val numOfRowsProcessed = numberPair.second
        val cargo = mutableListOf<MutableList<String>>()

        for (i in 0 until numOfCargoCols) {
            cargo.add(mutableListOf())
        }

        for (line in cargoLines) {
            for ((col, char) in line.withIndex()) {
                if (char == 'x') {
                    continue
                }
                cargo[col].add(char.toString())
            }
        }

        for (line in cargo) {
            line.reverse()
        }

        return Pair(cargo, numOfRowsProcessed)
    }

    fun moveCargoOneByOne(cargo: MutableList<MutableList<String>>, numberTaken: Int, origin: Int, destination: Int) {
        for (move in 0 until numberTaken) {
            val elementToMove = cargo[origin].takeLast(1)
            if (elementToMove.isEmpty()) {
                continue
            }
            cargo[origin].removeLast()
            cargo[destination] += elementToMove
        }
    }

    fun moveCargoInBatches(cargo: MutableList<MutableList<String>>, numberTaken: Int, origin: Int, destination: Int) {
        val elementsToMove = cargo[origin].takeLast(numberTaken)
        for (x in 0 until numberTaken) {
            cargo[origin].removeLast()
        }
        cargo[destination] += elementsToMove
    }

    fun part1(input: List<String>): String {
        val (cargo, numOfRowsProcessed) = getInitialCargoSetup(input)
        for ((i, row) in input.withIndex()) {
            if (i <= numOfRowsProcessed + 1) {
                continue
            }

            val instructions = row
                .split(" ")
                .filter { it.toIntOrNull() != null }
                .map { it.toInt() - 1 }

            moveCargoOneByOne(cargo, instructions[0] + 1, instructions[1], instructions[2])
        }

        return cargo.joinToString("") { it.last() }
    }

    fun part2(input: List<String>): String {
        val (cargo, numOfRowsProcessed) = getInitialCargoSetup(input)
        for ((i, row) in input.withIndex()) {
            if (i <= numOfRowsProcessed + 1) {
                continue
            }

            val instructions = row
                .split(" ")
                .filter { it.toIntOrNull() != null }
                .map { it.toInt() - 1 }

            moveCargoInBatches(cargo, instructions[0] + 1, instructions[1], instructions[2])
        }

        return cargo.joinToString("") { it.last() }
    }

    printSolutionFromInputLines("Day05", ::part1)
    printSolutionFromInputLines("Day05", ::part2)
}


