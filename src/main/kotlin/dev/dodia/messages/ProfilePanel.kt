package dev.dodia.messages

import dev.dodia.database.repository.MemberRepository
import dev.minn.jda.ktx.messages.MessageCreate
import dev.minn.jda.ktx.interactions.components.Thumbnail
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.CommandInteraction

object ProfilePanel {
    suspend fun send(event: SlashCommandInteractionEvent) {

        val member = MemberRepository.getMember(event.member!!.id)

        event.channel.sendMessage(
            MessageCreate(useComponentsV2 = true){
                container {
                    section {
                        accessory = Thumbnail(event.member?.avatar!!.url)

                        text(
                            """
                                ## 👋 Добро пожаловать в ваш профиль <@${event.member!!.id}>
                                
                                - ### Ваш уровень: //todo: сделать деление exp для уровня
                                -# всего exp: ${member?.exp}
                                
                                - 🪙 Монет: ${member?.coins}
                                - 💎 Алмазов: ${member?.diamonds}
                                
                            """.trimIndent()
                        )
                    }
                }
            }
        )

    }

}