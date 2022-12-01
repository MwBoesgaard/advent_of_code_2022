import java.io.File
import java.nio.charset.Charset
import kotlin.system.measureTimeMillis

fun readInput(name: String): List<String> = File("src", "$name.txt").readLines(Charset.forName("UTF-8"))

fun textInput(name: String): String = File("src", "$name.txt").readText(Charset.forName("UTF-8"))

fun printSolutionFromInputLines(name: String, solutionFunction: (List<String>) -> Int) {
    var result: Int
    val runtime = measureTimeMillis {
        val input = readInput(name)
        result = solutionFunction(input)
    }
    println("${solutionFunction.toString().substring(9..13)} execution complete. Answer: $result. Runtime: $runtime ms")
}

fun printSolutionFromInputText(name: String, solutionFunction: (String) -> Int) {
    var result: Int
    val runtime = measureTimeMillis {
        val input = textInput(name)
        result = solutionFunction(input)
    }
    println("${solutionFunction.toString().substring(9..13)} execution complete. Answer: $result. Runtime: $runtime ms")
}