package tesler.will.chatassistant.store

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

class EmptySettingsService : ISettingsService {
    override fun observeSettings(): Flow<Settings> {
        return emptyFlow()
    }

    override suspend fun currentSettings(): Settings {
        return Settings("", 1f)
    }

    override suspend fun updateVoice(voice: String) {
    }

    override suspend fun updateSpeed(speed: Float) {
    }
}