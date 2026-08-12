package dev.dodia.database.model

import dev.dodia.database.table.MembersTable
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.v1.core.ResultRow
import kotlin.time.Clock
import kotlin.time.Instant

data class MemberModel(
    val discordId: String,
    val isFirstJoin: Boolean,
    val firstJoin: Instant = Clock.System.now(),
    val exp: Long = 0L,
    val lvl: Int = 1,
    val levelUp: Long = 100,
    val coins: Long = 0L,
    val diamonds: Long = 0L,
    val dmMessageIds: List<String> = emptyList(),
)

fun ResultRow.toMemberModel(): MemberModel {
    return MemberModel(
        discordId = this[MembersTable.discordId],
        isFirstJoin = this[MembersTable.isFirstJoin],
        firstJoin = this[MembersTable.firstJoin],
        exp = this[MembersTable.exp],
        lvl = this[MembersTable.lvl],
        levelUp = this[MembersTable.levelUp],
        coins = this[MembersTable.coins],
        diamonds = this[MembersTable.diamonds],
        dmMessageIds = Json.decodeFromString(this[MembersTable.dmMessageIds])
    )
}