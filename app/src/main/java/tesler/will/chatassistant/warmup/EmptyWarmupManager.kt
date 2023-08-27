package tesler.will.chatassistant.warmup

class EmptyWarmupManager : IWarmupManager {
    override suspend fun warmup() {}
}
