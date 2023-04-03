package tesler.will.chatassistant.store

import kotlinx.coroutines.flow.Flow

interface ISettingsService {
    fun observeSettings(): Flow<Settings>
    suspend fun currentSettings(): Settings

    suspend fun updateIsMute(isMute: Boolean)
}
