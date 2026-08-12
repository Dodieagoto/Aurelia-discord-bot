package dev.dodia.events

import dev.dodia.Bot.Companion.jda
import dev.dodia.database.repository.MemberRepository
import dev.dodia.messages.PanelMessage
import dev.dodia.messages.PlayerJoinMessages
import dev.minn.jda.ktx.events.listener
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

object PlayerEvents {

    fun register(){

        jda.listener<GuildMemberJoinEvent> { event ->

            if (!MemberRepository.exists(event.member.id)) {

                PlayerJoinMessages.sendFirstJoinMessage(event)
                MemberRepository.addMember(event.member.id)

            } else {
                PlayerJoinMessages.sendReturnJoinMessage(event)
            }
        }

        jda.listener<MessageReceivedEvent>{ event ->

            if (event.author.id != "945694824173027408") return@listener
            if (event.message.contentRaw == "!отправить_панель") {

                event.message.delete().queue()
                event.channel.sendMessage(PanelMessage.message).queue()

            }

            if (event.message.contentRaw == "!доб"){
                event.message.delete().queue()
                MemberRepository.addMember(event.member!!.id)
            }

            if (event.message.contentRaw == "!удл"){
                event.message.delete().queue()
                MemberRepository.removeMember(event.member!!.id)
            }

            if (event.message.contentRaw == "!+"){
                event.message.delete().queue()
                MemberRepository.addReward(
                    discordId = event.member!!.id,
                    exp = 100000
                )
            }
        }
    }
}