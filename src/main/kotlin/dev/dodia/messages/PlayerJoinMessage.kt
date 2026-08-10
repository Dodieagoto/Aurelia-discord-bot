package dev.dodia.messages

import dev.minn.jda.ktx.messages.MessageCreate
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent

object PlayerJoinMessage {

    fun send(event: GuildMemberJoinEvent) {
        val channel = event.getGuild().getTextChannelById("1536345577669795950")

        channel?.sendMessage(MessageCreate(useComponentsV2 = true) {

            container {
                text("""
                    > ## 👋 <@&${event.member.id}> Добро пожаловать!
                """.trimIndent()) //todo: сделать красивое сообщение
            }

        })

    }
}