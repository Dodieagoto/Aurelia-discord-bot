package dev.dodia.messages

import dev.dodia.database.repository.MemberRepository
import dev.minn.jda.ktx.messages.MessageCreate
import dev.minn.jda.ktx.interactions.components.Thumbnail
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.CommandInteraction

object ProfilePanel {
    suspend fun send(event: CommandInteraction) {
        try {
            val discordId = event.member?.id ?: event.user.id

            val member = runCatching { MemberRepository.getMember(discordId) }
                .onFailure { println("Ошибка получения участника из БД: ${it.stackTraceToString()}") }
                .getOrNull()

            val avatarUrl = event.member?.effectiveAvatarUrl
                ?: event.user.effectiveAvatarUrl
                ?: event.user.defaultAvatarUrl

            event.reply(
                MessageCreate(useComponentsV2 = true) {
                    container {
                        section {
                            accessory = Thumbnail(avatarUrl)

                            text(
                                """
                                    ## 👋 Добро пожаловать в ваш профиль <@$discordId>
                                    
                                    - ### Ваш уровень: //todo: сделать деление exp для уровня
                                    -# всего exp: ${member?.exp ?: 0}
                                    
                                    - 🪙 Монет: ${member?.coins ?: 0}
                                    - 💎 Алмазов: ${member?.diamonds ?: 0}
                                    
                                """.trimIndent()
                            )
                        }
                    }
                }
            )
                .setEphemeral(true)
                .queue()
        } catch (e: Exception) {
            println("Ошибка в ProfilePanel.send: ${e.stackTraceToString()}")
            runCatching {
                event.reply("⚠️ Не удалось загрузить профиль. Попробуйте позже.").setEphemeral(true).queue()
            }
        }
    }
}