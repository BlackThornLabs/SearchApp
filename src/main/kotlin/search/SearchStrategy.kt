package search

class SearchStrategy(
    private val invertedIndex: Map<String, List<Int>>,
    private val lines: List<String>
) {
    fun findAll(query: List<String>): List<String> {
        val indices = query.map { invertedIndex[it] ?: emptyList() }
            .map { it.toSet() }
            .reduceOrNull { acc, set -> acc.intersect(set) }
            ?: return emptyList()

        return indices.map { lines[it] }.distinct()
    }

    fun findAny(query: List<String>): List<String> {
        val indices = query.flatMap { invertedIndex[it] ?: emptyList() }
            .toSet()
        return indices.map { lines[it] }.distinct()
    }

    fun findNone(query: List<String>): List<String> {
        val indicesContainingQuery = query.flatMap { invertedIndex[it] ?: emptyList() }
            .toSet()
        return lines.indices
            .filterNot { it in indicesContainingQuery }
            .map { lines[it] }
            .distinct()
    }
}