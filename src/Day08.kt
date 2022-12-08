import kotlin.math.max
import kotlin.math.min

fun main() {
    fun isTreeHighestLowerBound(tree: Int, list: List<Int>, index: Int): Boolean {
        if (index == 0) {
            return true
        }
        return list.slice(0 until index).max() < tree
    }

    fun isTreeHighestUpperBound(tree: Int, list: List<Int>, index: Int): Boolean {
        val max = list.size
        if (index + 1 >= max) {
            return true
        }
        return tree > list.slice(index + 1 until max).max()
    }

    fun isTreeHighestForRowIndex(tree: Int, rowIndex: Int, colIndex: Int, matrix: List<List<Int>>): Boolean {
        val row = matrix[rowIndex]
        val lowerBound = isTreeHighestLowerBound(tree, row, colIndex)
        val upperBound = isTreeHighestUpperBound(tree, row, colIndex)
        return lowerBound || upperBound
    }

    fun isTreeHighestForColIndex(tree: Int, rowIndex: Int, colIndex: Int, matrix: List<List<Int>>): Boolean {
        val listOfColValues = mutableListOf<Int>()
        for (row in matrix) {
            listOfColValues.add(row[colIndex])
        }
        val lowerBound = isTreeHighestLowerBound(tree, listOfColValues, rowIndex)
        val upperBound = isTreeHighestUpperBound(tree, listOfColValues, rowIndex)
        return lowerBound || upperBound
    }

    fun buildMatrix(input: List<String>): List<List<Int>> {
        val matrix = arrayListOf<List<Int>>()
        for (line in input) {
            matrix.add(line.chunked(1).map { it.toInt() })
        }
        return matrix
    }

    fun isTreeVisible(tree: Int, rowIndex: Int, colIndex: Int, matrix: List<List<Int>>): Boolean {
        return isTreeHighestForColIndex(tree, rowIndex, colIndex, matrix) || isTreeHighestForRowIndex(
            tree,
            rowIndex,
            colIndex,
            matrix
        )
    }

    fun getMatrixSliceAroundIndex(rowIndex: Int, colIndex: Int, size: Int, matrix: List<List<Int>>): List<List<Int>> {
        val colLowerBound = max(colIndex - size, 0)
        val colUpperBound = min(colIndex + size, matrix.size - 1)

        val rowLowerBound = max(rowIndex - size, 0)
        val rowUpperBound = min(rowIndex + size, matrix[0].size - 1)

        // Mark the tree in question
        val mutMatrixCopy = matrix.toMutableList().map { it.toMutableList() }
        mutMatrixCopy[rowIndex][colIndex] = -1

        return mutMatrixCopy.slice(rowLowerBound..rowUpperBound).map { it.slice(colLowerBound..colUpperBound) }
    }

    fun getAdjustedTreeIndexes(matrixSlice: List<List<Int>>): Pair<Int, Int>? {
        for ((i, row) in matrixSlice.withIndex()) {
            for ((j, tree) in row.withIndex()) {
                if (tree == -1) {
                    return Pair(i, j)
                }
            }
        }
        return null
    }

    fun isAnOuterTree(rowIndex: Int, colIndex: Int, matrix: List<List<Int>>): Boolean {
        if (rowIndex == 0 || rowIndex == matrix.size - 1) {
            return true
        }
        if (colIndex == 0 || colIndex == matrix[0].size - 1) {
            return true
        }
        return false
    }

    fun part1(input: List<String>): Int {
        val matrix = buildMatrix(input)
        val answerMatrix = mutableListOf<List<Int>>()

        for ((rowIndex, row) in matrix.withIndex()) {
            val newRow = mutableListOf<Int>()
            for ((colIndex, tree) in row.withIndex()) {
                newRow.add(if (isTreeVisible(tree, rowIndex, colIndex, matrix)) 1 else 0)
            }
            answerMatrix.add(newRow)
        }
        return answerMatrix.sumOf { it.sum() }
    }

    fun part2(input: List<String>): Int {
        val matrix = buildMatrix(input)
        val listOfScenicScores = mutableListOf<Int>()

        for ((rowIndex, row) in matrix.withIndex()) {
            for ((colIndex, tree) in row.withIndex()) {
                if (isAnOuterTree(rowIndex, colIndex, matrix)) {
                    continue
                }

                var northDist = 0
                var eastDist = 0
                var southDist = 0
                var westDist = 0

                for (size in 1 until matrix.size) {
                    if (listOf(northDist, eastDist, southDist, westDist).all { it != 0 }) {
                        break
                    }
                    val matrixSlice = getMatrixSliceAroundIndex(rowIndex, colIndex, size, matrix)
                    val (adjustedRowIndex, adjustedColIndex) = getAdjustedTreeIndexes(matrixSlice)!!

                    val adjustedRow = matrixSlice[adjustedRowIndex]
                    val adjustedCol = mutableListOf<Int>()
                    for (rowSlice in matrixSlice) {
                        adjustedCol.add(rowSlice[adjustedColIndex])
                    }
                    adjustedCol.reverse()

                    // North
                    if (northDist == 0 && (!isTreeHighestUpperBound(
                            tree,
                            adjustedCol,
                            adjustedRowIndex
                        ) || rowIndex - size <= 0)
                    ) {
                        northDist = size
                    }
                    // East
                    if (eastDist == 0 && (!isTreeHighestUpperBound(
                            tree,
                            adjustedRow,
                            adjustedColIndex
                        ) || colIndex + size >= matrix.size - 1)
                    ) {
                        eastDist = size
                    }
                    // South
                    if (southDist == 0 && (!isTreeHighestLowerBound(
                            tree,
                            adjustedCol,
                            adjustedRowIndex
                        ) || rowIndex + size >= matrix.size - 1)
                    ) {
                        southDist = size
                    }
                    // West
                    if (westDist == 0 && (!isTreeHighestLowerBound(
                            tree,
                            adjustedRow,
                            adjustedColIndex
                        ) || colIndex - size <= 0)
                    ) {
                        westDist = size
                    }

                }
                if (northDist == 0) {
                    northDist = matrix.size
                }
                if (eastDist == 0) {
                    eastDist = matrix.size
                }
                if (southDist == 0) {
                    southDist = matrix.size
                }
                if (westDist == 0) {
                    westDist = matrix.size
                }
                listOfScenicScores.add(northDist * eastDist * southDist * westDist)
            }
        }
        return listOfScenicScores.max()
    }

    printSolutionFromInputLines("Day08", ::part1)
    printSolutionFromInputLines("Day08", ::part2)
}

