package tesler.will.chatassistant.store

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException

class SettingsService(private val context: Context) : ISettingsService {

    private object Keys {
        val IS_MUTE = booleanPreferencesKey("is_mute")
        val VOICE = stringPreferencesKey("voice")
        val SPEED = floatPreferencesKey("speed")
    }

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    }

    private fun mapSettings(preferences: Preferences): Settings {
        val isMute = preferences[Keys.IS_MUTE] ?: false
        val voice = preferences[Keys.VOICE]
        val speed = preferences[Keys.SPEED]
        return Settings(isMute, voice, speed)
    }

    override fun observeSettings(): Flow<Settings> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e("User Preferences", "Failed to read preferences", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            mapSettings(preferences)
        }

    override suspend fun currentSettings(): Settings {
        return mapSettings(context.dataStore.data.first().toPreferences())
    }

    override suspend fun updateIsMute(isMute: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[Keys.IS_MUTE] = isMute
        }
    }

    override suspend fun updateVoice(voice: String) {
        context.dataStore.edit { preferences ->
            preferences[Keys.VOICE] = voice
        }
    }

    override suspend fun updateSpeed(speed: Float) {
        context.dataStore.edit { preferences ->
            preferences[Keys.SPEED] = speed
        }
    }
}
