package dev.dodia.voice

object VoiceHubRegistry {

    private val ownedChannels = mutableMapOf<Long, Long>()

    fun register(channelId: Long, ownerId: Long) {
        ownedChannels[channelId] = ownerId
    }

    fun unregister(channelId: Long) {
        ownedChannels.remove(channelId)
    }

    fun ownerOf(channelId: Long): Long? {
        return ownedChannels[channelId]
    }

    fun isTemp(channelId: Long): Boolean {
        return ownedChannels.containsKey(channelId)
    }

}