package dev.dodia

import dev.dodia.database.DataBase
import dev.dodia.events.BotEvents
import dev.dodia.events.PlayerEvents
import dev.minn.jda.ktx.jdabuilder.light
import io.github.cdimascio.dotenv.Dotenv
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.ChunkingFilter
import net.dv8tion.jda.api.utils.MemberCachePolicy
import org.jetbrains.exposed.v1.jdbc.Database

class Bot {
    companion object {
        lateinit var jda: JDA
            private set

        private val TOKEN = Dotenv.load().get("TOKEN").toString()

        @JvmStatic
        fun main(args : Array<String>) {


            jda = light(TOKEN){
                enableIntents(
                    GatewayIntent.GUILD_MESSAGES,
                    GatewayIntent.GUILD_MEMBERS,
                    GatewayIntent.MESSAGE_CONTENT,
                )

                setMemberCachePolicy(MemberCachePolicy.ALL)
                setChunkingFilter(ChunkingFilter.ALL)
            }


            DataBase()

            PlayerEvents.register()
            BotEvents.register()

            jda.awaitReady()

        }
    }
}аргуиа3уаищггамцпвгисапм3п