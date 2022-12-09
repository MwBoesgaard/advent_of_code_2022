import kotlin.math.max
import kotlin.math.abs

fun main() {
    class Knot(var posX: Int, var posY: Int, var attachedTo: Knot?) {
        val setOfPlacesVisited = mutableSetOf(Pair(0, 0))

        fun moveInDirection(direction: String) {
            when (direction) {
                "U" -> posY += 1
                "R" -> posX += 1
                "D" -> posY -= 1
                "L" -> posX -= 1
            }
        }

        fun distanceFromOtherKnot(other: Knot): Int {
            // Chebyshev (or chess) distance
            return max(abs(other.posX - this.posX), abs(other.posY - this.posY))
        }

        fun moveTowardsOtherKnot(other: Knot) {
            if (other.posY - this.posY > 0) moveInDirection("U")
            if (other.posY - this.posY < 0) moveInDirection("D")
            if (other.posX - this.posX > 0) moveInDirection("R")
            if (other.posX - this.posX < 0) moveInDirection("L")

            this.setOfPlacesVisited.add(Pair(posX, posY))
        }
    }

    class Rope(size: Int) {
        val knots = MutableList(size) { Knot(0, 0, null) }
        var head: Knot
        var tail: Knot

        init {
            knots.forEachIndexed { i, knot -> if (i != 0) knot.attachedTo = knots[i - 1] }
            head = knots.first()
            tail = knots.last()
        }
    }

    fun part1(input: List<String>): Int {
        val rope = Rope(2)

        for (move in input) {
            val (direction, distance) = move.split(" ")

            for (step in 1..distance.toInt()) {
                rope.head.moveInDirection(direction)

                if (rope.tail.distanceFromOtherKnot(rope.tail.attachedTo!!) > 1) {
                    rope.tail.moveTowardsOtherKnot(rope.tail.attachedTo!!)
                }
            }
        }
        return rope.tail.setOfPlacesVisited.size
    }

    fun part2(input: List<String>): Int {
        val rope = Rope(10)

        for (move in input) {
            val (direction, distance) = move.split(" ")

            for (step in 1..distance.toInt()) {
                rope.head.moveInDirection(direction)

                for (knot in rope.knots.drop(1)) {
                    if (knot.distanceFromOtherKnot(knot.attachedTo!!) > 1) {
                        knot.moveTowardsOtherKnot(knot.attachedTo!!)
                    }
                }
            }
        }
        return rope.tail.setOfPlacesVisited.size
    }

    printSolutionFromInputLines("Day09", ::part1)
    printSolutionFromInputLines("Day09", ::part2)
}