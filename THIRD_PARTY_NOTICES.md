# Third-Party Notices

本專案散布物中包含或衍生自下列第三方著作。各項授權與原始出處如下。

---

## 1. 三角編號字碼表 (`app/src/main/assets/3corner.cin`)

- **原始輸入法設計**：胡立人、張源渭、黃克東（美國王安電腦公司）
- **資料來源**：
  - CCDB（中國文字資料庫，行政院文化建設委員會資訊應用國字整理小組）
  - CI_UNI_V2.txt（國家圖書館 UNICODE 工作小組）
- **CIN 格式整理**：趙惟倫 `<bluebat@member.fsf.org>`, 2006
- **散布來源**：[chinese-opendesktop/cin-tables](https://github.com/chinese-opendesktop/cin-tables)（repo 整體採用 CC0-1.0）
- **檔案授權**：Public Domain（檔頭明示「授權方式: Public Domain」）

本專案原樣引用該檔案，未修改內容，並保留檔頭所有 `#` 註解。

---

## 2. 聯想詞庫 (`app/src/main/assets/association.txt`)

- **來源**：衍生自 [APCLab/jieba-tw](https://github.com/APCLab/jieba-tw) 的 `jieba/dict.txt`
- **衍生方式**：擷取原檔的「詞」與「頻率」兩欄（捨棄第三欄詞性），其餘內容未做修改
- **授權**：MIT License

原始授權全文如下：

```
The MIT License (MIT)

Copyright (c) 2013 Sun Junyi

Permission is hereby granted, free of charge, to any person obtaining a copy of
this software and associated documentation files (the "Software"), to deal in
the Software without restriction, including without limitation the rights to
use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
the Software, and to permit persons to whom the Software is furnished to do so,
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
```

---

## 3. Android / Kotlin 函式庫

執行期透過 Gradle 取得，APK 內含其編譯產物。皆為 Apache License 2.0，詳細條款見各自 POM 檔。

- AndroidX Core KTX (`androidx.core:core-ktx`) — Google
- AndroidX Lifecycle Runtime KTX (`androidx.lifecycle:lifecycle-runtime-ktx`) — Google
- AndroidX SavedState KTX (`androidx.savedstate:savedstate-ktx`) — Google
- Jetpack Compose BOM 及其元件（`androidx.compose.ui:ui`、`androidx.compose.material3:material3`、`androidx.compose.foundation:foundation`、`androidx.compose.ui:ui-tooling`） — Google
- Kotlin Coroutines (`org.jetbrains.kotlinx:kotlinx-coroutines-android`) — JetBrains

Apache License 2.0 全文：<https://www.apache.org/licenses/LICENSE-2.0>
