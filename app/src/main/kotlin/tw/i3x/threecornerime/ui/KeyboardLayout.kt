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

/**
 * 中文三角輸入法鍵盤 — 參照原版 5 欄佈局
 *
 * [；：] [7]  [8]  [9]  [？]
 * [ ␣ ] [4]  [5]  [6]  [！]
 * [符號] [1]  [2]  [3]  [⌫]
 * [ABC]  [，] [0]  [。] [↵]
 */
@Composable
fun ChineseKeyboardLayout(
    onDigit: (Char) -> Unit,
    onChar: (String) -> Unit,
    onBackspace: () -> Unit,
    onEnter: () -> Unit,
    onSpace: () -> Unit,
    onSwitchMode: (InputMode) -> Unit,
    modifier: Modifier = Modifier,
    keyHeight: Dp = 52.dp,
) {
    val funcColor = MaterialTheme.colorScheme.surface
    val funcText = MaterialTheme.colorScheme.onSurface
    val primaryColor = MaterialTheme.colorScheme.primary
    val onPrimary = MaterialTheme.colorScheme.onPrimary

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 2.dp, vertical = 2.dp)
    ) {
        // Row 1: [；：] [7] [8] [9] [？]
        Row(modifier = Modifier.fillMaxWidth()) {
            DualKeyButton("；", "：",
                onTap = { onChar("；") }, onLongPress = { onChar("：") },
                modifier = Modifier.weight(1.2f), height = keyHeight)
            KeyButton("7", { onDigit('7') }, Modifier.weight(1f), keyHeight)
            KeyButton("8", { onDigit('8') }, Modifier.weight(1f), keyHeight)
            KeyButton("9", { onDigit('9') }, Modifier.weight(1f), keyHeight)
            KeyButton("？", { onChar("？") }, Modifier.weight(1.2f), keyHeight,
                funcColor, funcText, 18)
        }
        // Row 2: [␣] [4] [5] [6] [！]
        Row(modifier = Modifier.fillMaxWidth()) {
            KeyButton("␣", onSpace, Modifier.weight(1.2f), keyHeight,
                funcColor, funcText, 20)
            KeyButton("4", { onDigit('4') }, Modifier.weight(1f), keyHeight)
            KeyButton("5", { onDigit('5') }, Modifier.weight(1f), keyHeight)
            KeyButton("6", { onDigit('6') }, Modifier.weight(1f), keyHeight)
            KeyButton("！", { onChar("！") }, Modifier.weight(1.2f), keyHeight,
                funcColor, funcText, 18)
        }
        // Row 3: [符號] [1] [2] [3] [⌫]
        Row(modifier = Modifier.fillMaxWidth()) {
            KeyButton("符號", { onSwitchMode(InputMode.SYMBOL) }, Modifier.weight(1.2f), keyHeight,
                funcColor, funcText, 14)
            KeyButton("1", { onDigit('1') }, Modifier.weight(1f), keyHeight)
            KeyButton("2", { onDigit('2') }, Modifier.weight(1f), keyHeight)
            KeyButton("3", { onDigit('3') }, Modifier.weight(1f), keyHeight)
            KeyButton("⌫", onBackspace, Modifier.weight(1.2f), keyHeight,
                funcColor, funcText, 18)
        }
        // Row 4: [ABC] [，] [0] [。] [↵]
        Row(modifier = Modifier.fillMaxWidth()) {
            KeyButton("ABC", { onSwitchMode(InputMode.ENGLISH) }, Modifier.weight(1.2f), keyHeight,
                funcColor, funcText, 14)
            KeyButton("，", { onChar("，") }, Modifier.weight(1f), keyHeight,
                funcColor, funcText, 18)
            KeyButton("0", { onDigit('0') }, Modifier.weight(1f), keyHeight)
            KeyButton("。", { onChar("。") }, Modifier.weight(1f), keyHeight,
                funcColor, funcText, 18)
            KeyButton("↵", onEnter, Modifier.weight(1.2f), keyHeight,
                primaryColor, onPrimary, 18)
        }
    }
}
