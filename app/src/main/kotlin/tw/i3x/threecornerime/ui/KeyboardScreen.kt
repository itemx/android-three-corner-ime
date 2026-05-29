package tw.i3x.threecornerime.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tw.i3x.threecornerime.ime.InputAction
import tw.i3x.threecornerime.ime.InputMode
import tw.i3x.threecornerime.ime.InputStateManager

@Composable
fun KeyboardScreen(
    stateManager: InputStateManager,
    onAction: (InputAction) -> Unit,
) {
    val composingText by stateManager.composingText.collectAsState()
    val candidates by stateManager.candidates.collectAsState()
    val associations by stateManager.associations.collectAsState()
    val inputMode by stateManager.inputMode.collectAsState()
    val isShiftOn by stateManager.isShiftOn.collectAsState()
    val symbolPage by stateManager.symbolPage.collectAsState()

    Column(modifier = Modifier.fillMaxWidth()) {
        // Composing text display (Chinese mode only)
        if (inputMode == InputMode.CHINESE && composingText.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp)
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = composingText.replace('*', '_'),
                    fontSize = 16.sp,
                    fontFamily = FontFamily.Monospace,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(4.dp))
                val remaining = 6 - composingText.length
                if (remaining > 0) {
                    Text(
                        text = "_ ".repeat(remaining).trim(),
                        fontSize = 16.sp,
                        fontFamily = FontFamily.Monospace,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        }

        // Candidate bar (Chinese mode, while composing)
        if (inputMode == InputMode.CHINESE && candidates.isNotEmpty()) {
            CandidateBar(
                candidates = candidates,
                onCandidateSelected = { index ->
                    onAction(stateManager.onCandidateSelected(index))
                }
            )
        }

        // Association bar (Chinese mode, after committing a character, no composing)
        if (inputMode == InputMode.CHINESE && composingText.isEmpty() && associations.isNotEmpty()) {
            CandidateBar(
                candidates = associations,
                onCandidateSelected = { index ->
                    onAction(stateManager.onAssociationSelected(index))
                }
            )
        }

        // Keyboard layout based on mode
        when (inputMode) {
            InputMode.CHINESE -> ChineseKeyboardLayout(
                onDigit = { digit -> onAction(stateManager.onDigitKey(digit)) },
                onChar = { char -> onAction(stateManager.onCharKey(char)) },
                onBackspace = { onAction(stateManager.onBackspace()) },
                onEnter = { onAction(stateManager.onEnter()) },
                onSpace = { onAction(stateManager.onSpace()) },
                onSwitchMode = { mode -> stateManager.switchMode(mode) },
            )

            InputMode.ENGLISH -> EnglishKeyboardLayout(
                isShiftOn = isShiftOn,
                onChar = { char -> onAction(stateManager.onCharKey(char)) },
                onBackspace = { onAction(stateManager.onBackspace()) },
                onEnter = { onAction(stateManager.onEnter()) },
                onSpace = { onAction(stateManager.onSpace()) },
                onToggleShift = { stateManager.toggleShift() },
                onSwitchMode = { mode -> stateManager.switchMode(mode) },
            )

            InputMode.SYMBOL -> SymbolKeyboardLayout(
                page = symbolPage,
                onChar = { char -> onAction(stateManager.onCharKey(char)) },
                onBackspace = { onAction(stateManager.onBackspace()) },
                onEnter = { onAction(stateManager.onEnter()) },
                onSpace = { onAction(stateManager.onSpace()) },
                onTogglePage = { stateManager.toggleSymbolPage() },
                onSwitchMode = { mode -> stateManager.switchMode(mode) },
            )
        }
    }
}
