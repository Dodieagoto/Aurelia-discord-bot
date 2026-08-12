package dev.dodia.messages

import dev.dodia.Bot.Companion.jda
import dev.dodia.database.repository.MemberRepository
import dev.minn.jda.ktx.messages.MessageCreate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object LevelUpMessage {
    fun send(discordId: String, oldLvl: Int, lvl: Int, exp: Long, levelUp: Long) {
        jda.retrieveUserById(discordId).queue { user ->
            user.openPrivateChannel().queue { privateChannel ->
                privateChannel.sendMessage(
                    MessageCreate(useComponentsV2 = true) {
                        container {
                            text(
                                """
                                    > ## 🔺 Ваш уровень повышен! ${user.asMention}
                                   
                                    ### $oldLvl >> $lvl
                                    -# exp: $exp / $levelUp
                                    
                                """.trimIndent()
                            )
                        }
                    }
                ).queue { msg ->
                    CoroutineScope(Dispatchers.Default).launch {
                        MemberRepository.addDmMessageId(discordId, msg.id)
                    }
                }
            }
        }
    }
}