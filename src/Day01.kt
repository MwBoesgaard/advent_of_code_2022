fun main() {
    fun part1(input: List<String>): Int {
        var totalKcal = 0
        var highestKcal = 0
        for (kcal in input) {
            if (kcal == "") {
                if (totalKcal > highestKcal) {
                    highestKcal = totalKcal
                }
                totalKcal = 0
            } else {
                val intKcal = kcal.toInt()
                totalKcal += intKcal
            }


        }
        return highestKcal
    }

    fun part2(inputString: String): Int {
        return inputString
            .split(Regex("\\r?\\n\\r?\\n"))
            .map {
                it
                    .split(Regex("\\r?\\n"))
                    .sumOf { kcal -> kcal.toInt() }
            }
            .sortedDescending()
            .take(3)
            .sum()
    }
    printSolutionFromInputLines("Day01", ::part1)
    printSolutionFromInputText("Day01", ::part2)
}

