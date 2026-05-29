# 三角輸入法 (Three-Corner IME for Android)

Android 上的三角編號（3Corner）中文輸入法。每個字對應一組六位數字編碼，輸入後從候選列選字。

- **Min SDK**：24（Android 7.0+）
- **Target SDK**：35（Android 15）
- **語言**：Kotlin
- **UI**：Jetpack Compose + Material 3
- **字碼表**：32,913 筆（涵蓋常用漢字與大量罕字）
- **聯想詞**：基於 jieba 繁體詞庫，選完字後自動建議下一字

---

## 功能

- 中文三角輸入（數字鍵盤 + 標點符號）
- 英文 QWERTY 鍵盤
- 符號鍵盤
- 聯想字（選完一字後候選列顯示常見後續字）
- 深色模式跟隨系統

---

## 建置

需要 Android Studio（含 JDK 21）或本機 JDK 21 + Android SDK 35。

```bash
git clone https://github.com/<your-handle>/three-corner-ime.git
cd three-corner-ime
./gradlew assembleDebug
```

APK 產出於 `app/build/outputs/apk/debug/app-debug.apk`。

> 第一次 build 需自行建立 `local.properties`，內容一行：
> `sdk.dir=/path/to/Android/sdk`

---

## 安裝與啟用

1. 把 APK 推到手機並安裝：`adb install app/build/outputs/apk/debug/app-debug.apk`
2. 手機「設定 → 系統 → 語言與輸入 → 螢幕鍵盤 → 管理鍵盤」啟用「三角輸入法」
3. 在任一文字框切換輸入法即可使用

---

## 緣起

> 以下整理自 [`blog.md`](./blog.md)。

三角輸入法是一套用數字編碼輸入中文字的方法。每個字對應一組六位數字，使用者在數字鍵盤上輸入編碼，選字後即完成輸入。這套方法由王安電腦公司的胡立人、張源渭、黃克東三人設計，編碼規則是根據字形的三個角落特徵推導出數字，所以叫三角。它的特點是重碼率低，幾乎一字一碼，熟悉之後輸入速度不慢。

我自己沒有在用三角輸入法，但長輩有在用。他們從早期就學了這套輸入方式，用了三十幾年，手指記憶已經內建在肌肉裡。問題出在 Android 平台上唯一的三角輸入法 App，最後一次更新是 2015 年，隨著 Android 系統版本演進，這個 App 在近代手機上已經無法正常執行。

三角輸入法的核心邏輯很單純：讀取字碼對照表，使用者輸入數字代碼，程式查表列出候選字，選字送出。不需自然語言處理，不需機器學習，就是查表加上一個鍵盤介面。需求明確，規格清楚，正好適合 AI 輔助開發來做。整個製作過程大約兩到三個小時，由 Claude Code 協作完成。

完整版本請見 [`blog.md`](./blog.md)。

---

## 授權與致謝

本專案以 [MIT License](./LICENSE) 釋出。

包含或衍生自下列第三方著作，授權細節見 [`THIRD_PARTY_NOTICES.md`](./THIRD_PARTY_NOTICES.md)：

- **三角編號輸入法**：原始設計者胡立人、張源渭、黃克東（王安電腦）
- **字碼表 (`3corner.cin`)**：[chinese-opendesktop/cin-tables](https://github.com/chinese-opendesktop/cin-tables)，Public Domain；CIN 格式整理：趙惟倫 `<bluebat@member.fsf.org>`
- **聯想詞庫 (`association.txt`)**：衍生自 [APCLab/jieba-tw](https://github.com/APCLab/jieba-tw) 的 `dict.txt`，MIT License
- **Android / Jetpack Compose / Kotlin Coroutines**：Apache License 2.0

特別感謝原版三角輸入法的使用者與長期維護字碼表的開源社群——這套輸入法能在 Android 上重生，是站在你們的肩膀上完成的。
