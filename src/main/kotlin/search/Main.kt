package search

import java.io.File
import java.util.*

fun main() {
    val lines = File("src/main/resources/names.txt").readLines()
    val invertedIndex = buildInvertedIndex(lines)
    val searchStrategy = SearchStrategy(invertedIndex, lines)

    while (true) {
        println("=== Menu ===")
        println("1. Find a person")
        println("2. Print all people")
        println("0. Exit")

        when (readlnOrNull()?.trim()) {  // Используем readlnOrNull и trim
            "1" -> {
                println("Select a matching strategy: ALL, ANY, NONE")
                val strategy = readlnOrNull()?.uppercase()

                if (strategy in setOf("ALL", "ANY", "NONE")) {
                    println("Enter a name or email to search:")
                    val query = readlnOrNull()?.lowercase()?.split("\\s+".toRegex()) ?: emptyList()
                    val results = when (strategy) {
                        "ALL" -> searchStrategy.findAll(query)
                        "ANY" -> searchStrategy.findAny(query)
                        else -> searchStrategy.findNone(query)
                    }
                    printResults(results)
                } else {
                    println("Invalid strategy. Please try again.")
                }
            }
            "2" -> printAllPeople(lines)
            "0" -> {
                println("Bye!")
                return
            }
            else -> println("Invalid input. Please enter 1, 2 or 0.")
        }
        println()  // Пустая строка для визуального разделения
    }
}

fun buildInvertedIndex(lines: List<String>): Map<String, List<Int>> {
    val invertedIndex = mutableMapOf<String, MutableList<Int>>()
    lines.forEachIndexed { index, line ->
        line.lowercase(Locale.getDefault())
            .split("\\s+".toRegex())
            .forEach { word ->
                invertedIndex.getOrPut(word) { mutableListOf() }.add(index)
            }
    }
    return invertedIndex
}

fun handleSearch(searchStrategy: SearchStrategy, lines: List<String>) {
    println("Select a matching strategy: ALL, ANY, NONE")
    val strategy = readln().uppercase(Locale.getDefault())

    println("Enter a name or email to search all matching people.")
    val query = readln().lowercase(Locale.getDefault()).split("\\s+".toRegex())

    val result = when (strategy) {
        "ALL" -> searchStrategy.findAll(query)
        "ANY" -> searchStrategy.findAny(query)
        "NONE" -> searchStrategy.findNone(query)
        else -> {
            println("Invalid strategy.")
            return
        }
    }

    printResults(result)
}

fun printResults(results: List<String>) {
    if (results.isEmpty()) {
        println("No matching people found.")
    } else {
        println("${results.size} persons found:")
        results.forEach(::println)
    }
}

fun printAllPeople(lines: List<String>) {
    lines.forEach(::println)
}