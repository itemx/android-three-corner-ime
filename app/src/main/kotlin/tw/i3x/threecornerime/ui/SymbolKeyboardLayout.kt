package tw.i3x.threecornerime.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import tw.i3x.threecornerime.ime.InputMode

private val PAGE1_ROW1 = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "0")
private val PAGE1_ROW2 = listOf("、", "…", "—", "～", "「", "」", "『", "』", "（", "）")
private val PAGE1_ROW3 = listOf("《", "》", "【", "】", "；", "：", "＃", "＠", "＆", "＊")
private val PAGE1_ROW4 = listOf("％", "＋", "－", "＝", "｜", "＼", "／", "＿")

private val PAGE2_ROW1 = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "0")
private val PAGE2_ROW2 = listOf("!", "@", "#", "$", "%", "^", "&", "*", "(", ")")
private val PAGE2_ROW3 = listOf("-", "+", "=", "_", "{", "}", "[", "]", "|", "\\")
private val PAGE2_ROW4 = listOf("<", ">", "~", "`", "\"", "'", ";", ":")

@Composable
fun SymbolKeyboardLayout(
    page: Int,
    onChar: (String) -> Unit,
    onBackspace: () -> Unit,
    onEnter: () -> Unit,
    onSpace: () -> Unit,
    onTogglePage: () -> Unit,
    onSwitchMode: (InputMode) -> Unit,
    modifier: Modifier = Modifier,
    keyHeight: Dp = 48.dp,
) {
    val funcColor = MaterialTheme.colorScheme.surface
    val funcText = MaterialTheme.colorScheme.onSurface
    val primaryColor = MaterialTheme.colorScheme.primary
    val onPrimary = MaterialTheme.colorScheme.onPrimary

    val row1 = if (page == 0) PAGE1_ROW1 else PAGE2_ROW1
    val row2 = if (page == 0) PAGE1_ROW2 else PAGE2_ROW2
    val row3 = if (page == 0) PAGE1_ROW3 else PAGE2_ROW3
    val row4 = if (page == 0) PAGE1_ROW4 else PAGE2_ROW4
    val pageLabel = "${page + 1}/2"

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 2.dp, vertical = 2.dp)
    ) {
        // Row 1: numbers
        Row(modifier = Modifier.fillMaxWidth()) {
            row1.forEach { sym ->
                KeyButton(sym, { onChar(sym) }, Modifier.weight(1f), keyHeight,
                    fontSize = 16)
            }
        }
        // Row 2: symbols
        Row(modifier = Modifier.fillMaxWidth()) {
            row2.forEach { sym ->
                KeyButton(sym, { onChar(sym) }, Modifier.weight(1f), keyHeight,
                    fontSize = 16)
            }
        }
        // Row 3: symbols
        Row(modifier = Modifier.fillMaxWidth()) {
            row3.forEach { sym ->
                KeyButton(sym, { onChar(sym) }, Modifier.weight(1f), keyHeight,
                    fontSize = 16)
            }
        }
        // Row 4: symbols + page toggle
        Row(modifier = Modifier.fillMaxWidth()) {
            row4.forEach { sym ->
                KeyButton(sym, { onChar(sym) }, Modifier.weight(1f), keyHeight,
                    fontSize = 16)
            }
            KeyButton(pageLabel, onTogglePage, Modifier.weight(2f), keyHeight,
                funcColor, funcText, 14)
        }
        // Row 5: [中] [空白] [⌫] [↵]
        Row(modifier = Modifier.fillMaxWidth()) {
            KeyButton("中", { onSwitchMode(InputMode.CHINESE) }, Modifier.weight(1.5f), keyHeight,
                funcColor, funcText, 16)
            KeyButton("空白", onSpace, Modifier.weight(4f), keyHeight,
                funcColor, funcText, 14)
            KeyButton("⌫", onBackspace, Modifier.weight(1.5f), keyHeight,
                funcColor, funcText, 18)
            KeyButton("↵", onEnter, Modifier.weight(1.5f), keyHeight,
                primaryColor, onPrimary, 18)
        }
    }
}
