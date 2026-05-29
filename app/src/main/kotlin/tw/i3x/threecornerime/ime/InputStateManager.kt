package tw.i3x.threecornerime.ime

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class InputMode {
    CHINESE, ENGLISH, SYMBOL
}

class InputStateManager(
    private val cinTable: CinTable,
    private val associationDict: AssociationDictionary? = null,
) {

    private val _composingText = MutableStateFlow("")
    val composingText: StateFlow<String> = _composingText.asStateFlow()

    private val _candidates = MutableStateFlow<List<String>>(emptyList())
    val candidates: StateFlow<List<String>> = _candidates.asStateFlow()

    private val _associations = MutableStateFlow<List<String>>(emptyList())
    val associations: StateFlow<List<String>> = _associations.asStateFlow()

    private val _inputMode = MutableStateFlow(InputMode.CHINESE)
    val inputMode: StateFlow<InputMode> = _inputMode.asStateFlow()

    private val _isShiftOn = MutableStateFlow(false)
    val isShiftOn: StateFlow<Boolean> = _isShiftOn.asStateFlow()

    private val _symbolPage = MutableStateFlow(0)
    val symbolPage: StateFlow<Int> = _symbolPage.asStateFlow()

    // Track the last committed text for association chaining
    private var associationPrefix = ""

    // --- Digit input (Chinese mode) ---

    fun onDigitKey(digit: Char): InputAction {
        // Starting a new code input clears association state
        if (_composingText.value.isEmpty()) {
            clearAssociations()
        }

        val current = _composingText.value
        if (current.length >= 6) return InputAction.None
        val newText = current + digit
        _composingText.value = newText
        updateCandidates(newText)
        return InputAction.UpdateComposing(newText)
    }

    // --- Character input (punctuation, symbol, english letter) ---

    fun onCharKey(char: String): InputAction {
        // If composing in Chinese mode, commit first candidate then the char
        if (_inputMode.value == InputMode.CHINESE && _composingText.value.isNotEmpty()) {
            val candidates = _candidates.value
            if (candidates.isNotEmpty()) {
                resetComposing()
                clearAssociations()
                return InputAction.CommitText(candidates[0] + char)
            }
        }
        resetComposing()
        clearAssociations()
        if (_inputMode.value == InputMode.ENGLISH && _isShiftOn.value) {
            _isShiftOn.value = false
        }
        return InputAction.CommitText(char)
    }

    // --- Association: user taps an association suggestion ---

    fun onAssociationSelected(index: Int): InputAction {
        val assocList = _associations.value
        if (index < 0 || index >= assocList.size) return InputAction.None
        val char = assocList[index]

        // Extend the prefix for chaining: "台" → "台灣" → "台灣人"
        associationPrefix += char
        updateAssociations()

        return InputAction.CommitText(char)
    }

    // --- Common keys ---

    fun onBackspace(): InputAction {
        if (_inputMode.value == InputMode.CHINESE && _composingText.value.isNotEmpty()) {
            val newText = _composingText.value.dropLast(1)
            _composingText.value = newText
            if (newText.isEmpty()) {
                _candidates.value = emptyList()
                return InputAction.FinishComposing
            }
            updateCandidates(newText)
            return InputAction.UpdateComposing(newText)
        }
        clearAssociations()
        return InputAction.SendBackspace
    }

    fun onEnter(): InputAction {
        if (_inputMode.value == InputMode.CHINESE && _composingText.value.isNotEmpty()) {
            val composing = _composingText.value
            val results = cinTable.lookupWithTrailingZeros(composing)
            if (results.size == 1) {
                val char = results[0]
                resetComposing()
                triggerAssociation(char)
                return InputAction.CommitText(char)
            }
            if (results.isNotEmpty()) {
                _candidates.value = results + composing.padEnd(6, '0')
                _composingText.value = composing.padEnd(6, '0')
                return InputAction.UpdateComposing(_composingText.value)
            }
            // No CIN match at all — commit the digits as a number
            val digits = composing
            resetComposing()
            clearAssociations()
            return InputAction.CommitText(digits)
        }
        return InputAction.SendEnter
    }

    fun onSpace(): InputAction {
        if (_inputMode.value == InputMode.CHINESE && _composingText.value.isNotEmpty()) {
            val current = _composingText.value
            if (current.length < 6) {
                // Insert wildcard '*' (displayed as '_')
                val newText = current + "*"
                _composingText.value = newText
                updateCandidates(newText)
                return InputAction.UpdateComposing(newText)
            }
            // Already 6 chars — select first candidate (existing behavior)
            val candidates = _candidates.value
            if (candidates.isNotEmpty()) {
                val selected = candidates[0]
                resetComposing()
                if (!selected.all { it.isDigit() }) {
                    triggerAssociation(selected)
                } else {
                    clearAssociations()
                }
                return InputAction.CommitText(selected)
            }
        }
        clearAssociations()
        return InputAction.CommitText(" ")
    }

    fun onCandidateSelected(index: Int): InputAction {
        val candidates = _candidates.value
        if (index < 0 || index >= candidates.size) return InputAction.None
        val selected = candidates[index]
        val composing = _composingText.value
        resetComposing()
        // If user selected the raw digit string, don't trigger association
        if (selected.all { it.isDigit() }) {
            clearAssociations()
        } else {
            triggerAssociation(selected)
        }
        return InputAction.CommitText(selected)
    }

    // --- Mode switching ---

    fun switchMode(mode: InputMode) {
        if (_inputMode.value == InputMode.CHINESE) {
            resetComposing()
        }
        clearAssociations()
        _inputMode.value = mode
        if (mode == InputMode.SYMBOL) {
            _symbolPage.value = 0
        }
    }

    fun toggleShift() {
        _isShiftOn.value = !_isShiftOn.value
    }

    fun toggleSymbolPage() {
        _symbolPage.value = (_symbolPage.value + 1) % 2
    }

    // --- Reset ---

    fun reset() {
        resetComposing()
        clearAssociations()
    }

    // --- Private helpers ---

    private fun resetComposing() {
        _composingText.value = ""
        _candidates.value = emptyList()
    }

    private fun updateCandidates(code: String) {
        val cinResults = if (code.contains('*')) {
            cinTable.wildcardLookup(code)
        } else if (code.length == 6) {
            cinTable.exactLookup(code)
        } else {
            cinTable.prefixLookup(code)
        }
        // Single digit (0-9): put the digit FIRST so it's easy to input numbers
        // Multi-digit or wildcard: append the raw code at the end
        val rawCode = code.replace("*", "")
        _candidates.value = if (code.length == 1 && !code.contains('*')) {
            listOf(rawCode) + cinResults
        } else {
            cinResults + rawCode
        }
    }

    /**
     * After committing a character via CIN table, trigger association lookup.
     * The committed char becomes the new association prefix.
     */
    private fun triggerAssociation(committedChar: String) {
        associationPrefix = committedChar
        updateAssociations()
    }

    private fun updateAssociations() {
        val dict = associationDict
        if (dict == null || associationPrefix.isEmpty()) {
            _associations.value = emptyList()
            return
        }
        _associations.value = dict.getAssociations(associationPrefix)
    }

    private fun clearAssociations() {
        associationPrefix = ""
        _associations.value = emptyList()
    }
}

sealed class InputAction {
    data object None : InputAction()
    data class CommitText(val text: String) : InputAction()
    data class UpdateComposing(val text: String) : InputAction()
    data object FinishComposing : InputAction()
    data object SendBackspace : InputAction()
    data object SendEnter : InputAction()
}
