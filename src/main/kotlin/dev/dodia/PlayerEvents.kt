package dev.dodia

import dev.dodia.Bot.Companion.jda
import dev.dodia.messages.PlayerJoinMessage
import dev.minn.jda.ktx.events.listener
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent


object PlayerEvents {

    fun register(){

        jda.listener<GuildMemberJoinEvent>{ event ->
            PlayerJoinMessage.send(event)
        }
    }

}