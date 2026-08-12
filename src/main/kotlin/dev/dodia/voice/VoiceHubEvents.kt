package dev.dodia.voice

import dev.dodia.Bot.Companion.jda
import dev.minn.jda.ktx.events.listener
import dev.minn.jda.ktx.events.onButton
import net.dv8tion.jda.api.components.label.Label
import net.dv8tion.jda.api.components.textinput.TextInput
import net.dv8tion.jda.api.components.textinput.TextInputStyle
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.modals.Modal

object VoiceHubEvents {

    private const val TRIGGER_CHANNEL_ID = 1536082934291890206
    private const val TARGET_CATEGORY_ID = 1536083195781578832

    fun register() {

        jda.listener<GuildVoiceUpdateEvent> { event ->

            val left = event.channelLeft
            if (left is VoiceChannel && VoiceHubRegistry.isTemp(left.idLong) && left.members.isEmpty()) {
                left.delete().queue()
                VoiceHubRegistry.unregister(left.idLong)
            }

            val joined = event.channelJoined
            if (joined != null && joined.idLong == TRIGGER_CHANNEL_ID) {
                createChannel(event.member.id, event.guild)
            }

        }

        jda.onButton(""){ event ->

            val customId = event.componentId
            if (!customId.startsWith("voice-settings-btn:")) return@onButton

            val channelId = customId.substringAfter(":").toLong()
            val channel = event.guild?.getVoiceChannelById(channelId)

            if (channel == null || VoiceHubRegistry.ownerOf(channelId) != event.member?.idLong) {
                event.reply("Настраивать этот войс может только его создатель.").setEphemeral(true).queue()
                return@onButton
            }

            val nameInput = TextInput.create("voice-name", TextInputStyle.SHORT)
                .setValue(channel.name)
                .setRequired(false)
                .build()

            val limitInput = TextInput.create("voice-limit", TextInputStyle.SHORT)
                .setValue(channel.userLimit.toString())
                .setRequired(false)
                .build()

            val statusInput = TextInput.create("voice-status", TextInputStyle.SHORT)
                .setValue(channel.status)
                .setRequired(false)
                .build()

            val modal = Modal.create("voice-settings-modal:$channelId", "Настройки войса")
                .addComponents(
                    Label.of("Название", nameInput),
                    Label.of("Лимит участников (0 - без лимита)", limitInput),
                    Label.of("Статус", statusInput)
                )
                .build()

            event.replyModal(modal).queue()

        }

        jda.listener<ModalInteractionEvent> { event ->

            val customId = event.modalId
            if (!customId.startsWith("voice-settings-modal:")) return@listener

            val channelId = customId.substringAfter(":").toLong()
            val channel = event.guild?.getVoiceChannelById(channelId)

            if (channel == null || VoiceHubRegistry.ownerOf(channelId) != event.member?.idLong) {
                event.reply("Настраивать этот войс может только его создатель.").setEphemeral(true).queue()
                return@listener
            }

            val name = event.getValue("voice-name")?.asString?.takeIf { it.isNotBlank() }
            val limit = event.getValue("voice-limit")?.asString?.toIntOrNull()
            val status = event.getValue("voice-status")?.asString

            name?.let { channel.manager.setName(it).queue() }
            limit?.let { channel.manager.setUserLimit(it.coerceIn(0, 99)).queue() }
            status?.let { channel.modifyStatus(it).queue() }

            event.reply("Настройки войса обновлены.").setEphemeral(true).queue()

        }

    }

    private fun createChannel(ownerId: String, guild: Guild) {

        val category = guild.getCategoryById(TARGET_CATEGORY_ID) ?: return
        val owner = guild.getMemberById(ownerId) ?: return

        category.createVoiceChannel("Войс ${owner.effectiveName}").queue { channel ->
            VoiceHubRegistry.register(channel.idLong, owner.idLong)
            guild.moveVoiceMember(owner, channel).queue()
            channel.sendMessage(VoiceSettingsMessage.build(channel.idLong)).queue()
        }

    }

}