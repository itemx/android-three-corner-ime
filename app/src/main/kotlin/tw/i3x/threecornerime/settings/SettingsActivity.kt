package tw.i3x.threecornerime.settings

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.LinearLayout
import android.widget.Button
import android.widget.TextView

class SettingsActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(48, 48, 48, 48)
        }

        layout.addView(TextView(this).apply {
            text = "三角輸入法"
            textSize = 24f
        })

        layout.addView(TextView(this).apply {
            text = "\n請在系統設定中啟用三角輸入法，\n然後在任意輸入框中切換使用。"
            textSize = 16f
        })

        layout.addView(Button(this).apply {
            text = "開啟輸入法設定"
            setOnClickListener {
                startActivity(Intent(Settings.ACTION_INPUT_METHOD_SETTINGS))
            }
        })

        setContentView(layout)
    }
}
