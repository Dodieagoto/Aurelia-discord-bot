package dev.dodia.messages

import dev.minn.jda.ktx.messages.MessageCreate
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent

object PlayerJoinMessages {

    fun sendFirstJoinMessage(event: GuildMemberJoinEvent) {

        event.user.openPrivateChannel().queue { privateChannel ->
            privateChannel.sendMessage(
                MessageCreate(useComponentsV2 = true) {
                    container {
                        text(
                            """
                                ## Добро пожаловать в первые!
                                -# todo: сделать красивое сообщение
                            """.trimIndent()
                        )
                    }
                }
            ).queue()
        }

    }

    fun sendReturnJoinMessage(event: GuildMemberJoinEvent) {

        event.user.openPrivateChannel().queue { privateChannel ->
            privateChannel.sendMessage(
                MessageCreate(useComponentsV2 = true) {
                    container {
                        text(
                            """
                                С возвращением на наш проект!
                                -# todo: сделать красивое сообщение
                            """.trimIndent()
                        )
                    }
                }
            ).queue()
        }
    }

}