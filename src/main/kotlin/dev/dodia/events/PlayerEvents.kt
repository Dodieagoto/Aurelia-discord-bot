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

        jda.listener<GuildMemberJoinEvent>{ event ->

            MemberRepository.addMember(event.member)
            PlayerJoinMessage.send(event)

        }

        jda.listener<MessageReceivedEvent>{ event ->

            if (event.author.id != "945694824173027408") return@listener
            if (event.message.contentRaw != "!отправить_панель") return@listener

            event.message.delete().queue()

            event.channel.sendMessage(PanelMessage.message).queue()

        }

    }

}