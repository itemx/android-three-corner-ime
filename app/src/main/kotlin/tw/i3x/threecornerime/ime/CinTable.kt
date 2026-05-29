package tw.i3x.threecornerime.ime

import java.io.InputStream
import java.util.TreeMap

class CinTable {

    private val table = TreeMap<String, MutableList<String>>()

    val size: Int get() = table.size

    fun load(inputStream: InputStream) {
        var inCharDef = false
        inputStream.bufferedReader(Charsets.UTF_8).useLines { lines ->
            for (line in lines) {
                if (line.startsWith("%chardef begin")) {
                    inCharDef = true
                    continue
                }
                if (line.startsWith("%chardef end")) {
                    break
                }
                if (!inCharDef) continue

                val spaceIndex = line.indexOf(' ')
                if (spaceIndex == -1) continue
                val code = line.substring(0, spaceIndex)
                val char = line.substring(spaceIndex + 1)
                if (code.isEmpty() || char.isEmpty()) continue

                table.getOrPut(code) { mutableListOf() }.add(char)
            }
        }
    }

    fun exactLookup(code: String): List<String> {
        return table[code] ?: emptyList()
    }

    fun prefixLookup(prefix: String, limit: Int = 100): List<String> {
        if (prefix.isEmpty()) return emptyList()
        val end = prefix.substring(0, prefix.length - 1) +
                (prefix.last() + 1).toChar()
        val result = mutableListOf<String>()
        for ((_, chars) in table.subMap(prefix, end)) {
            for (c in chars) {
                result.add(c)
                if (result.size >= limit) return result
            }
        }
        return result
    }

    fun lookupWithTrailingZeros(partial: String): List<String> {
        if (partial.length > 6) return emptyList()
        val padded = partial.padEnd(6, '0')
        return exactLookup(padded)
    }

    /**
     * Lookup with wildcard support. '*' in the pattern matches any single digit (0-9).
     * Example: "22*013" expands to "220013", "221013", ..., "229013" and merges results.
     */
    fun wildcardLookup(pattern: String, limit: Int = 100): List<String> {
        if (!pattern.contains('*')) {
            return if (pattern.length == 6) exactLookup(pattern) else prefixLookup(pattern, limit)
        }
        val results = mutableListOf<String>()
        val seen = mutableSetOf<String>()
        expandWildcards(pattern, 0, StringBuilder(), results, seen, limit)
        return results
    }

    private fun expandWildcards(
        pattern: String, pos: Int, current: StringBuilder,
        results: MutableList<String>, seen: MutableSet<String>, limit: Int
    ) {
        if (results.size >= limit) return
        if (pos == pattern.length) {
            val code = current.toString()
            val matches = if (code.length == 6) exactLookup(code) else prefixLookup(code, limit - results.size)
            for (m in matches) {
                if (seen.add(m)) {
                    results.add(m)
                    if (results.size >= limit) return
                }
            }
            return
        }
        if (pattern[pos] == '*') {
            for (d in '0'..'9') {
                current.append(d)
                expandWildcards(pattern, pos + 1, current, results, seen, limit)
                current.deleteCharAt(current.length - 1)
                if (results.size >= limit) return
            }
        } else {
            current.append(pattern[pos])
            expandWildcards(pattern, pos + 1, current, results, seen, limit)
            current.deleteCharAt(current.length - 1)
        }
    }
}
