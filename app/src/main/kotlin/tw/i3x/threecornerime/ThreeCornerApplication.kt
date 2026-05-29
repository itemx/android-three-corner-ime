package tw.i3x.threecornerime

import android.app.Application
import android.util.Log
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import tw.i3x.threecornerime.ime.AssociationDictionary
import tw.i3x.threecornerime.ime.CinTable

class ThreeCornerApplication : Application() {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    val cinTableDeferred = CompletableDeferred<CinTable>()
    val associationDictDeferred = CompletableDeferred<AssociationDictionary>()

    override fun onCreate() {
        super.onCreate()

        // Load CIN table
        scope.launch {
            try {
                val table = CinTable()
                assets.open("3corner.cin").use { table.load(it) }
                Log.i(TAG, "CinTable loaded: ${table.size} entries")
                cinTableDeferred.complete(table)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to load CinTable", e)
                cinTableDeferred.completeExceptionally(e)
            }
        }

        // Load association dictionary
        scope.launch {
            try {
                val dict = AssociationDictionary()
                assets.open("association.txt").use { dict.load(it) }
                Log.i(TAG, "AssociationDictionary loaded: ${dict.size} entries")
                associationDictDeferred.complete(dict)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to load AssociationDictionary", e)
                associationDictDeferred.completeExceptionally(e)
            }
        }
    }

    companion object {
        private const val TAG = "ThreeCornerApp"
    }
}
