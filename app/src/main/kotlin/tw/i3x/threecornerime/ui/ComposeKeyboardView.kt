package tw.i3x.threecornerime.ui

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.AbstractComposeView
import tw.i3x.threecornerime.ime.InputAction
import tw.i3x.threecornerime.ime.InputStateManager

class ComposeKeyboardView(
    context: Context,
    private val stateManager: InputStateManager,
    private val onAction: (InputAction) -> Unit,
) : AbstractComposeView(context) {

    @Composable
    override fun Content() {
        ThreeCornerTheme {
            KeyboardScreen(
                stateManager = stateManager,
                onAction = onAction
            )
        }
    }
}
