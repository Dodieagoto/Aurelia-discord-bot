package dev.dodia.database.table

import org.jetbrains.exposed.v1.core.dao.id.IntIdTable
import org.jetbrains.exposed.v1.datetime.CurrentTimestamp
import org.jetbrains.exposed.v1.datetime.timestamp

object MembersTable : IntIdTable("players") {

    val discordId = varchar("discord_id", 32)
        .uniqueIndex()

    val isFirstJoin = bool("is_first_join")
        .default(true)

    val firstJoin = timestamp("first_join")
        .defaultExpression(CurrentTimestamp)

    val exp = long("exp")
        .default(0L)

    val lvl = integer("lvl")
        .default(1)

    val levelUp = long("upgrade_level")
        .default(100)

    val coins = long("coins")
        .default(0L)

    val diamonds = long("diamonds")
        .default(0L)

    val dmMessageIds = text("dm_message_ids")
        .default("[]")

}