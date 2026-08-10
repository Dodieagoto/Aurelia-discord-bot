package dev.dodia.events

import dev.dodia.Bot.Companion.jda
import dev.minn.jda.ktx.events.listener
import net.dv8tion.jda.api.events.session.ReadyEvent

object BotEvents {

    fun register() {
        jda.listener<ReadyEvent> {
            println("Bot started")
        }
    }

}