package dev.dodia.messages

import dev.minn.jda.ktx.messages.MessageCreate
import dev.minn.jda.ktx.messages.named
import net.dv8tion.jda.api.components.buttons.ButtonStyle
import net.dv8tion.jda.api.components.separator.Separator
import net.dv8tion.jda.api.entities.emoji.Emoji
import java.awt.Color

object PanelMessage {

    private val img = javaClass.getResourceAsStream("/panel.png")!!.named("panel.png")
    val message = MessageCreate(useComponentsV2 = true) {
        container {
            mediaGallery {
                item(img)
            }

            separator(isDivider = true, spacing = Separator.Spacing.LARGE)

            text("""
                > ## 📰 ПАНЕЛЬ ПОДДЕРЖКИ
                 
                 В этом канале вы можете обратиться к поддержке проекта по поводу конфликтных ситуаций или неисправности технической части сервера!
                 
            """.trimIndent())

            accentColor = Color(0x1599D1)
        }

        container {
            actionRow {
                button(
                    customId = "open-ticket-btn",
                    label = "Создать тикет",
                    style = ButtonStyle.PRIMARY,
                    emoji = Emoji.fromUnicode("\uD83D\uDCEC")
                )
            }

            accentColor = Color(0x1599D1)
        }

    }
}