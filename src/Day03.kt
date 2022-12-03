fun main() {
    fun part1(input: List<String>): Int {
        val pointsByCharacter = generatePointsByCharacterMap()

        return input.sumOf {
            val firstCompartment = it.substring(0, it.length / 2).toSet()
            val secondCompartment = it.substring(it.length / 2, it.length).toSet()
            val misplacedItem = firstCompartment.intersect(secondCompartment).first()
            pointsByCharacter[misplacedItem]!!
        }
    }

    fun part2(input: List<String>): Int {
        val pointsByCharacter = generatePointsByCharacterMap()

        return input
            .chunked(3)
            .map {
                it.map { elf -> elf.toSet() }
                    .reduce { combinedSets, elfSet -> combinedSets.intersect(elfSet) }
                    .first()
            }
            .sumOf { pointsByCharacter[it]!! }
    }

    printSolutionFromInputLines("Day03", ::part1)
    printSolutionFromInputLines("Day03", ::part2)
}

fun generatePointsByCharacterMap(): Map<Char, Int> {
    val alphabet = ('a'..'z').toList() + ('A'..'Z').toList()
    val numbers = (1..alphabet.size + 1).toList()
    return alphabet.zip(numbers).associate { it }
}




