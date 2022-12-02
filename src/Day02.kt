import HandType.*
import OutcomeType.*

fun main() {
    fun part1(input: List<String>): Int {
        /*
         *    opponent, me
         * Rock:     A, X
         * Paper:    B, Y
         * Scissors: C, Z
         *
         * Rock: 1 point
         * Paper: 2 points
         * Scissors: 3 points
         *
         * Win: 6 points
         * Draw: 3 points
         * Loss: 0 points
         */

        var totalScore = 0

        for (line in input) {
            val hands = line.split(" ")
            val opponentsHand: String = hands[0]
            val myHand: String = hands[1]

            val pointsByThrow = hashMapOf("A" to 1, "B" to 2, "C" to 3, "X" to 1, "Y" to 2, "Z" to 3)

            val matchPoints = when (pointsByThrow[opponentsHand]!! - pointsByThrow[myHand]!!) {
                2 -> 6 // Opponent has scissors I have rock, I win.
                1 -> 0 // Opponent has paper, I have rock OR opponent has scissors, I have paper, they win.
                0 -> 3 // We have the same, Draw.
                -1 -> 6 // Opponent has rock. I have paper OR opponent has paper, I have scissors, I win.
                -2 -> 0 // Opponent has rock, I have scissors, they win.
                else -> {
                    throw Exception("Point difference out of range.")
                }
            }
            totalScore += pointsByThrow[myHand]!! + matchPoints
        }
        return totalScore
    }

    fun part2(input: List<String>): Int {
        var totalScore = 0

        for (line in input) {
            val handAndOutcome = line.split(" ")
            val opponentsHand = HandType.fromInnerValue(handAndOutcome[0])
            val outcome = OutcomeType.fromInnerValue(handAndOutcome[1])

            val pointsByThrow = hashMapOf(ROCK to 1, PAPER to 2, SCISSOR to 3)
            val pointsByOutcome = hashMapOf(LOSS to 0, DRAW to 3, WIN to 6)

            val myHand = when (outcome) {
                LOSS -> when (opponentsHand) {
                    ROCK -> SCISSOR
                    PAPER -> ROCK
                    SCISSOR -> PAPER
                }
                DRAW -> when (opponentsHand) {
                    ROCK -> ROCK
                    PAPER -> PAPER
                    SCISSOR -> SCISSOR
                }
                WIN -> when (opponentsHand) {
                    ROCK -> PAPER
                    PAPER -> SCISSOR
                    SCISSOR -> ROCK
                }
            }
            totalScore += pointsByThrow[myHand]!! + pointsByOutcome[outcome]!!
        }
        return totalScore
    }

    printSolutionFromInputLines("Day02", ::part1)
    printSolutionFromInputLines("Day02", ::part2)
}

enum class HandType(private val value: String) {
    ROCK("A"),
    PAPER("B"),
    SCISSOR("C");

    companion object {
        fun fromInnerValue(type: String): HandType = values().first { it.value == type }
    }
}

enum class OutcomeType(private val value: String) {
    LOSS("X"),
    DRAW("Y"),
    WIN("Z");

    companion object {
        fun fromInnerValue(type: String): OutcomeType = values().first { it.value == type }
    }
}



