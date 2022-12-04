fun main() {
    fun doesOneRangeContainTheOther(rangeA: IntRange, rangeB: IntRange): Boolean {
        return rangeA.all { it in rangeB } || rangeB.all { it in rangeA }
    }

    fun doRangesHaveAnyValueThatOverlap(rangeA: IntRange, rangeB: IntRange): Boolean {
        return rangeA.any { it in rangeB }
    }

    fun part1(input: List<String>): Int {
        return input
            .map {
                it.split(",")
                    .map { joinedStrRange -> joinedStrRange.split("-") }
                    .map { strRange -> IntRange(strRange[0].toInt(), strRange[1].toInt()) }
            }
            .count { doesOneRangeContainTheOther(it[0], it[1]) }
    }

    fun part2(input: List<String>): Int {
        return input
            .map {
                it.split(",")
                    .map { joinedStrRange -> joinedStrRange.split("-") }
                    .map { strRange -> IntRange(strRange[0].toInt(), strRange[1].toInt()) }
            }
            .count { doRangesHaveAnyValueThatOverlap(it[0], it[1]) }
    }

    printSolutionFromInputLines("Day04", ::part1)
    printSolutionFromInputLines("Day04", ::part2)
}

