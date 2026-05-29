package tw.i3x.threecornerime.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import tw.i3x.threecornerime.ime.InputMode

private val ROW1 = listOf('q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p')
private val ROW2 = listOf('a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l')
private val ROW3 = listOf('z', 'x', 'c', 'v', 'b', 'n', 'm')

@Composable
fun EnglishKeyboardLayout(
    isShiftOn: Boolean,
    onChar: (String) -> Unit,
    onBackspace: () -> Unit,
    onEnter: () -> Unit,
    onSpace: () -> Unit,
    onToggleShift: () -> Unit,
    onSwitchMode: (InputMode) -> Unit,
    modifier: Modifier = Modifier,
    keyHeight: Dp = 48.dp,
) {
    val funcColor = MaterialTheme.colorScheme.surface
    val funcText = MaterialTheme.colorScheme.onSurface
    val primaryColor = MaterialTheme.colorScheme.primary
    val onPrimary = MaterialTheme.colorScheme.onPrimary
    val shiftColor = if (isShiftOn) primaryColor else funcColor
    val shiftTextColor = if (isShiftOn) onPrimary else funcText

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 2.dp, vertical = 2.dp)
    ) {
        // Row 1: q w e r t y u i o p
        Row(modifier = Modifier.fillMaxWidth()) {
            ROW1.forEach { c ->
                val label = if (isShiftOn) c.uppercase() else c.toString()
                KeyButton(label, { onChar(label) }, Modifier.weight(1f), keyHeight,
                    fontSize = 18)
            }
        }
        // Row 2: a s d f g h j k l (with side padding)
        Row(modifier = Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.weight(0.5f))
            ROW2.forEach { c ->
                val label = if (isShiftOn) c.uppercase() else c.toString()
                KeyButton(label, { onChar(label) }, Modifier.weight(1f), keyHeight,
                    fontSize = 18)
            }
            Spacer(modifier = Modifier.weight(0.5f))
        }
        // Row 3: ⇧ z x c v b n m ⌫
        Row(modifier = Modifier.fillMaxWidth()) {
            KeyButton("⇧", onToggleShift, Modifier.weight(1.5f), keyHeight,
                shiftColor, shiftTextColor, 18)
            ROW3.forEach { c ->
                val label = if (isShiftOn) c.uppercase() else c.toString()
                KeyButton(label, { onChar(label) }, Modifier.weight(1f), keyHeight,
                    fontSize = 18)
            }
            KeyButton("⌫", onBackspace, Modifier.weight(1.5f), keyHeight,
                funcColor, funcText, 18)
        }
        // Row 4: [中] [,] [空白] [.] [↵]
        Row(modifier = Modifier.fillMaxWidth()) {
            KeyButton("中", { onSwitchMode(InputMode.CHINESE) }, Modifier.weight(1.2f), keyHeight,
                funcColor, funcText, 16)
            KeyButton(",", { onChar(",") }, Modifier.weight(1f), keyHeight,
                funcColor, funcText, 18)
            KeyButton("空白", onSpace, Modifier.weight(3.6f), keyHeight,
                funcColor, funcText, 14)
            KeyButton(".", { onChar(".") }, Modifier.weight(1f), keyHeight,
                funcColor, funcText, 18)
            KeyButton("↵", onEnter, Modifier.weight(1.2f), keyHeight,
                primaryColor, onPrimary, 18)
        }
    }
}
