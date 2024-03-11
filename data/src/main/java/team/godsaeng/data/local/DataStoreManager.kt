package team.godsaeng.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreManager @Inject constructor(private val dataStore: DataStore<Preferences>) {
    suspend fun storeStringData(
        key: String,
        data: String
    ) {
        dataStore.edit {
            it[stringPreferencesKey(key)] = data
        }
    }

    fun loadStringData(key: String): Flow<String?> = dataStore.data.map {
        it[stringPreferencesKey(key)]
    }

    suspend fun removeData() {
        dataStore.edit {
            it.clear()
        }
    }

    companion object {
        const val KEY_OAUTH_PLATFORM = "key_oauth_platform"
    }
}