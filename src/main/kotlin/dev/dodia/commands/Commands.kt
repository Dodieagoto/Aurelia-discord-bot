package dev.dodia.commands

import dev.dodia.Bot.Companion.jda
import dev.dodia.messages.ProfilePanel
import dev.minn.jda.ktx.events.onCommand
import dev.minn.jda.ktx.interactions.commands.*

object Commands {

    fun register() {
        jda.updateCommands {
            slash("профиль", "Показать ваш профиль сервера") {
                restrict(
                    guild = true,
                )
            }
        }.queue()

        jda.onCommand("профиль") { event ->
            ProfilePanel.send(event)
        }

    }

}