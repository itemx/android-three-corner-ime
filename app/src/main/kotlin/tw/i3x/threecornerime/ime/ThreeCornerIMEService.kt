package tw.i3x.threecornerime.ime

import android.inputmethodservice.InputMethodService
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.runBlocking
import tw.i3x.threecornerime.ThreeCornerApplication
import tw.i3x.threecornerime.ui.ComposeKeyboardView

class ThreeCornerIMEService : InputMethodService(), LifecycleOwner, SavedStateRegistryOwner {

    private val lifecycleRegistry = LifecycleRegistry(this)
    private val savedStateRegistryController = SavedStateRegistryController.create(this)

    override val lifecycle: Lifecycle get() = lifecycleRegistry
    override val savedStateRegistry: SavedStateRegistry
        get() = savedStateRegistryController.savedStateRegistry

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private lateinit var stateManager: InputStateManager

    override fun onCreate() {
        super.onCreate()
        savedStateRegistryController.performRestore(null)
        lifecycleRegistry.currentState = Lifecycle.State.STARTED

        val app = application as ThreeCornerApplication
        val cinTable = runBlocking { app.cinTableDeferred.await() }
        val associationDict = runBlocking { app.associationDictDeferred.await() }
        stateManager = InputStateManager(cinTable, associationDict)
    }

    override fun onCreateInputView(): View {
        lifecycleRegistry.currentState = Lifecycle.State.RESUMED

        // Must set lifecycle owners on the IME window's decorView BEFORE
        // the ComposeView is attached, otherwise Compose can't find them
        window?.window?.decorView?.let { decorView ->
            decorView.setViewTreeLifecycleOwner(this)
            decorView.setViewTreeSavedStateRegistryOwner(this)
        }

        return ComposeKeyboardView(
            context = this,
            stateManager = stateManager,
            onAction = ::handleAction
        )
    }

    override fun onStartInput(info: EditorInfo?, restarting: Boolean) {
        super.onStartInput(info, restarting)
        android.util.Log.d("ThreeCornerIME",
            "onStartInput: inputType=${info?.inputType}, " +
            "imeOptions=${info?.imeOptions}, " +
            "packageName=${info?.packageName}, " +
            "restarting=$restarting")
        if (::stateManager.isInitialized) {
            stateManager.reset()
        }
    }

    override fun onFinishInput() {
        super.onFinishInput()
        if (::stateManager.isInitialized) {
            stateManager.reset()
        }
    }

    override fun onDestroy() {
        lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
        scope.cancel()
        super.onDestroy()
    }

    private fun handleAction(action: InputAction) {
        val ic = currentInputConnection ?: return
        when (action) {
            is InputAction.CommitText -> {
                ic.commitText(action.text, 1)
            }
            is InputAction.UpdateComposing -> {
                ic.setComposingText(action.text.replace('*', '_'), 1)
            }
            is InputAction.FinishComposing -> {
                ic.finishComposingText()
            }
            is InputAction.SendBackspace -> {
                ic.sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL))
                ic.sendKeyEvent(KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL))
            }
            is InputAction.SendEnter -> {
                ic.sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER))
                ic.sendKeyEvent(KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER))
            }
            is InputAction.None -> {}
        }
    }
}
