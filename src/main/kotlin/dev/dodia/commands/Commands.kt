package dev.dodia.commands

import dev.dodia.Bot.Companion.jda
import dev.dodia.messages.ProfilePanel
import dev.minn.jda.ktx.events.listener
import dev.minn.jda.ktx.events.onCommand
import dev.minn.jda.ktx.interactions.commands.*
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

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