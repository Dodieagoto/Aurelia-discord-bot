package dev.dodia.commands

import dev.dodia.Bot.Companion.jda
import dev.dodia.database.repository.MemberRepository
import dev.dodia.database.table.MembersTable
import dev.dodia.messages.ProfilePanel
import dev.minn.jda.ktx.events.onCommand
import dev.minn.jda.ktx.interactions.commands.*
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.interactions.InteractionContextType
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions
import net.dv8tion.jda.api.interactions.commands.OptionType
import kotlin.math.exp

object Commands {

    fun register() {
        jda.updateCommands {
            slash("профиль", "Показать ваш профиль сервера") {
                restrict(
                    guild = true,
                )
            }
            slash("очистить", "Очистить историю сообщений с ботом") {
                setContexts(InteractionContextType.BOT_DM)

            }

            slash("обнулить", "удалить запись о игроке в базе данных"){
                restrict(
                    guild = true,
                )
                addOption(OptionType.USER, "target", "member")
                defaultPermissions = DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)
            }

            slash("выдать", "выдать валюту участнику") {
                restrict(
                    guild = true,
                )

                addOption(OptionType.USER, "target", "member")
                addOption(OptionType.INTEGER, "coins", "member")
                addOption(OptionType.INTEGER, "diamonds", "member")
                addOption(OptionType.INTEGER, "exp", "member")

                defaultPermissions = DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)
            }

        }.queue()

        jda.onCommand("профиль") { event ->
            ProfilePanel.send(event)
        }

        jda.onCommand("обнулить") { event ->
            event.getOption("target")?.let { target ->
                MemberRepository.removeMember(target.asUser.id)
            }

            event.reply(" - ✅ пользователь обнулён!")
                .setEphemeral(true)
                .queue()
        }

        jda.onCommand("выдать") { event ->
            event.getOption("target")?.let { target ->

                val coins = event.getOption("coins")?.asLong ?: 0
                val diamonds = event.getOption("diamonds")?.asLong ?: 0
                val exp = event.getOption("exp")?.asLong ?: 0

                event.reply("""
                > ### ✅ Пользователю ${target.asUser.asMention} выдано:
                
                - Алмазов: $diamonds
                - Монет: $coins
                - Опыта: $exp
          
            """.trimIndent())
                    .setEphemeral(true)
                    .queue()

                MemberRepository.addReward(
                    discordId = target.asUser.id,
                    exp = exp,
                    coins = coins,
                    diamonds = diamonds,
                )

            }
        }

    }

}