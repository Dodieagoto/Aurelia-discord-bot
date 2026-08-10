package dev.dodia.voice

import dev.minn.jda.ktx.messages.MessageCreate
import net.dv8tion.jda.api.components.buttons.ButtonStyle
import net.dv8tion.jda.api.entities.emoji.Emoji
import java.awt.Color

object VoiceSettingsMessage {

    fun build(channelId: Long) = MessageCreate(useComponentsV2 = true) {
        container {
            text("""
                > ## 🔊 Настройка Голосового канала
                
                Нажмите на кнопку ниже, чтобы изменить название, лимит участников и статус вашего войса.
                
            """.trimIndent())

            actionRow {
                button(
                    customId = "voice-settings-btn:$channelId",
                    label = "Настроить войс",
                    style = ButtonStyle.PRIMARY,
                    emoji = Emoji.fromUnicode("⚙️")
                )
            }

            accentColor = Color(0x1599D1)
        }
    }

}