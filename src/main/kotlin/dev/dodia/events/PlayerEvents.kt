package dev.dodia.events

import dev.dodia.Bot.Companion.jda
import dev.dodia.database.repository.MemberRepository
import dev.dodia.messages.PanelMessage
import dev.dodia.messages.PlayerJoinMessage
import dev.minn.jda.ktx.events.listener
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

object PlayerEvents {

    fun register(){

        jda.listener<GuildMemberJoinEvent> { event ->
            runCatching {
                PlayerJoinMessage.send(event)
                MemberRepository.addMember(event.member.id)
            }.onFailure {
                println("Ошибка в GuildMemberJoinEvent: ${it.stackTraceToString()}")
            }
        }

        jda.listener<MessageReceivedEvent>{ event ->

            if (event.author.id != "945694824173027408") return@listener
            if (event.message.contentRaw == "!отправить_панель") {

                event.message.delete().queue()
                event.channel.sendMessage(PanelMessage.message).queue()

            }

            if (event.message.contentRaw == "!доб"){
                MemberRepository.addMember(event.member!!.id)
            }

            if (event.message.contentRaw == "!абд"){
                MemberRepository.addCoinsToMember(event.member!!.id, 100L)
            }
        }

    }

}