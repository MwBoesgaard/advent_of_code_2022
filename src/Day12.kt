import java.util.PriorityQueue
import kotlin.math.abs

fun main() {

    class ElevationMap(inputMapData: List<String>) {
        val height = inputMapData.size
        val width = inputMapData[0].length

        val map = MutableList(height) { MutableList(width) { "?" } }
        val listOfPotentialStartingPoints: MutableList<Pair<Int, Int>> = mutableListOf()

        lateinit var startPos: Pair<Int, Int>
        lateinit var endPos: Pair<Int, Int>

        val priorityByLetter = ('a'..'z')
            .toList()
            .map { it.toString() }
            .zip((1..('a'..'z').toList().size))
            .associate { it }
            .toMutableMap()

        init {
            priorityByLetter["S"] = 1
            priorityByLetter["E"] = 26
            generateFromInput(inputMapData)
        }

        fun setStartingPos(pos: Pair<Int, Int>) {
            this.startPos = pos
        }

        fun generateFromInput(input: List<String>) {
            for ((rowIndex, line) in input.withIndex()) {
                for ((colIndex, char) in line.chunked(1).withIndex()) {
                    if (char == "a") {
                        listOfPotentialStartingPoints.add(Pair(rowIndex, colIndex))
                    }
                    if (char == "S") {
                        startPos = Pair(rowIndex, colIndex)
                    }
                    if (char == "E") {
                        endPos = Pair(rowIndex, colIndex)
                    }
                    map[rowIndex][colIndex] = char
                }
            }
        }

        fun print() {
            map.forEach { println(it.joinToString("")) }
            println("Starting coordinates: $startPos, End coordinates $endPos")
        }

        fun printPath(path: List<Pair<Int, Int>>) {
            val pathSet = path.toSet()
            for ((rowIndex, row) in map.withIndex()) {
                for ((colIndex, letter) in row.withIndex()) {
                    val letterToPrint = if (Pair(rowIndex, colIndex) == endPos) {
                        "E"
                    } else if (Pair(rowIndex, colIndex) in pathSet) {
                        "."
                    } else {
                        letter
                    }
                    print(letterToPrint)
                }
                print("\n")
            }
            println()
        }

        fun searchForExit(): MutableList<Pair<Int, Int>> {
            val mapMapNodeComparator: Comparator<MapNode> = compareBy { it.combinedScore }

            val openList = PriorityQueue(mapMapNodeComparator)
            val closedSet = hashSetOf<Pair<Int, Int>>()

            openList.add(MapNode("S", startPos, null))

            while (openList.size > 0) {
                val highestPriorityNode = openList.poll()
                closedSet.add(highestPriorityNode.pos)

                val neighbours = highestPriorityNode.getNeighbours()

                for (neighbour in neighbours) {
                    if (neighbour.pos in closedSet) {
                        continue
                    }

                    if (priorityByLetter[neighbour.letter]!! > priorityByLetter[highestPriorityNode.letter]!! + 1) {
                        continue
                    }

                    if (neighbour.letter == "E") {
                        val path = mutableListOf<Pair<Int, Int>>()
                        var currentNode = neighbour
                        while (currentNode.parent != null) {
                            path.add(currentNode.pos)
                            currentNode = currentNode.parent!!
                        }
                        return path
                    }
                    openList.add(neighbour)
                }
            }
            return mutableListOf()
        }

        inner class MapNode(val letter: String, val pos: Pair<Int, Int>, var parent: MapNode?) {
            val distanceFromStartScore: Int = (parent?.distanceFromStartScore ?: 0) + 1
            val distanceFromGoalScore: Int = abs(pos.first - endPos.first) + abs(pos.second - endPos.second) //Manhattan
            val combinedScore: Int = distanceFromStartScore + distanceFromGoalScore

            fun getNeighbours(): List<MapNode> {
                val neighbours = mutableListOf<MapNode>()
                if (pos.first > 0) { //North
                    val letter = map[pos.first - 1][pos.second]
                    val node = MapNode(letter, Pair(pos.first - 1, pos.second), this)
                    neighbours.add(node)
                }

                if (pos.first < height - 1) { //South
                    val letter = map[pos.first + 1][pos.second]
                    val node = MapNode(letter, Pair(pos.first + 1, pos.second), this)
                    neighbours.add(node)
                }

                if (pos.second > 0) { //West
                    val letter = map[pos.first][pos.second - 1]
                    val node = MapNode(letter, Pair(pos.first, pos.second - 1), this)
                    neighbours.add(node)
                }

                if (pos.second < width - 1) { //East
                    val letter = map[pos.first][pos.second + 1]
                    val node = MapNode(letter, Pair(pos.first, pos.second + 1), this)
                    neighbours.add(node)
                }

                return neighbours
            }
        }
    }

    fun part1(input: List<String>): Int {
        val map = ElevationMap(input)
        return map.searchForExit().size
    }

    fun part2(input: List<String>): Int {
        val listOfResults = mutableListOf<Int>()
        val map = ElevationMap(input)

        // Elevation (a -> b) transitions, required to reach the end, can only be found on the far left of the map
        // Therefore, only these positions are examined.
        for (x in 0 until map.height) {
            map.setStartingPos(Pair(x, 0))
            val path = map.searchForExit()
            listOfResults.add(path.size)
        }
        return listOfResults.min()
    }

    printSolutionFromInputLines("Day12", ::part1)
    printSolutionFromInputLines("Day12", ::part2)
}