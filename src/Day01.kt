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

    fun part2(inputStringOfKcal: String): Int {
        return inputStringOfKcal
                .split("\n\n")
                .map{it
                        .split("\n")
                        .map{stringKcal -> stringKcal.toInt()}
                        .reduce{totalKcal, kcal -> totalKcal + kcal}
                }
                .sortedByDescending{it}
                .slice(IntRange(0,2))
                .sum()
    }

    val input = lineInput("Day01")
    println(part1(input))
    val inputText = textInput("Day01")
    println(part2(inputText))
}
