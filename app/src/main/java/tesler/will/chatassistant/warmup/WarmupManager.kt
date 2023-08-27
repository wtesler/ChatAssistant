package tesler.will.chatassistant.warmup

import tesler.will.chatassistant.server.ApiService

class WarmupManager(private val apiService: ApiService): IWarmupManager {
    override suspend fun warmup() {
        apiService.warmup();
    }
}


