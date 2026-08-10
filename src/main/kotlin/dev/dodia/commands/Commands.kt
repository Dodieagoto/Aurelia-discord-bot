package dev.dodia.commands

import dev.dodia.Bot.Companion.jda
import dev.minn.jda.ktx.interactions.commands.*

object Commands {

    fun register() {
        jda.updateCommands {
            slash("управление_участниками", "Управление участниками тикета") {
                restrict(
                    guild = true,
                )
            }
        }
    }

}