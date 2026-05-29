package tw.i3x.threecornerime.ime

import java.io.InputStream
import java.util.TreeMap

/**
 * 聯想字詞庫。
 *
 * 根據已輸入的字串 prefix，查詢所有以該 prefix 開頭的詞，
 * 回傳下一個字的候選列表（按詞頻排序、去重）。
 *
 * 例如：prefix = "台" → 回傳 ["灣", "北", "中", "南", "東", ...]
 *       prefix = "台灣" → 回傳 ["人", "話", "省", "大", ...]
 */
class AssociationDictionary {

    // key = first char, value = list of (word, freq) sorted by freq desc
    private val dictByFirstChar = HashMap<Char, MutableList<WordEntry>>()

    // Full TreeMap for multi-char prefix lookup
    private val allWords = TreeMap<String, Int>()

    val size: Int get() = allWords.size

    fun load(inputStream: InputStream) {
        inputStream.bufferedReader(Charsets.UTF_8).useLines { lines ->
            for (line in lines) {
                val tabIndex = line.indexOf('\t')
                if (tabIndex == -1) continue
                val word = line.substring(0, tabIndex)
                val freq = line.substring(tabIndex + 1).trim().toIntOrNull() ?: continue
                if (word.length < 2) continue

                allWords[word] = freq

                val firstChar = word[0]
                dictByFirstChar
                    .getOrPut(firstChar) { mutableListOf() }
                    .add(WordEntry(word, freq))
            }
        }

        // Sort each list by frequency descending
        for ((_, entries) in dictByFirstChar) {
            entries.sortByDescending { it.freq }
        }
    }

    /**
     * Given a prefix string (one or more characters the user has already committed),
     * return the next-character suggestions sorted by frequency.
     *
     * @param prefix The characters already entered (e.g., "台" or "台灣")
     * @param limit Maximum number of suggestions to return
     * @return List of single-character strings for the next position
     */
    fun getAssociations(prefix: String, limit: Int = 30): List<String> {
        if (prefix.isEmpty()) return emptyList()

        val nextCharIndex = prefix.length
        val seen = LinkedHashSet<String>() // preserves insertion order, deduplicates

        if (prefix.length == 1) {
            // Fast path: use the first-char index
            val entries = dictByFirstChar[prefix[0]] ?: return emptyList()
            for (entry in entries) {
                if (entry.word.length > nextCharIndex) {
                    seen.add(entry.word[nextCharIndex].toString())
                    if (seen.size >= limit) break
                }
            }
        } else {
            // Multi-char prefix: use TreeMap range scan
            val end = prefix.substring(0, prefix.length - 1) +
                    (prefix.last() + 1).toChar()
            for ((word, _) in allWords.subMap(prefix, end)) {
                if (word.length > nextCharIndex && word.startsWith(prefix)) {
                    seen.add(word[nextCharIndex].toString())
                    if (seen.size >= limit) break
                }
            }
        }

        return seen.toList()
    }

    private data class WordEntry(val word: String, val freq: Int)
}
