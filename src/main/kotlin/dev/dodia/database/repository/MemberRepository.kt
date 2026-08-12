package dev.dodia.database.repository

import dev.dodia.database.DatabaseExecutor
import dev.dodia.database.model.toMemberModel
import dev.dodia.database.table.MembersTable
import dev.dodia.messages.LevelUpMessage
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.greaterEq
import org.jetbrains.exposed.v1.core.plus
import org.jetbrains.exposed.v1.jdbc.deleteAll
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.update
import org.jetbrains.exposed.v1.jdbc.upsert
import kotlin.time.Clock

object MemberRepository {

    suspend fun getMember(id: String) =
        DatabaseExecutor.transaction {
            val result = MembersTable
                .selectAll()
                .where { MembersTable.discordId eq id }
                .singleOrNull()
                ?.toMemberModel()
            result
        }

    suspend fun addMember(discordId: String) =
        DatabaseExecutor.transaction {
            MembersTable.upsert(MembersTable.discordId) {
                it[MembersTable.discordId] = discordId
                it[isFirstJoin] = false
                it[firstJoin] = Clock.System.now()
            }
        }

    suspend fun removeMember(discordId: String) {
        DatabaseExecutor.transaction {
            MembersTable
                .deleteWhere { MembersTable.discordId eq discordId }
        }
    }

    suspend fun addReward(
        discordId: String,
        exp: Long = 0,
        coins: Long = 0,
        diamonds: Long = 0,

    ){
        DatabaseExecutor.transaction {
            MembersTable.update(
                where = { MembersTable.discordId eq discordId },
            ) {
                it[MembersTable.exp] = MembersTable.exp + exp
                it[MembersTable.coins] = MembersTable.coins + coins
                it[MembersTable.diamonds] = MembersTable.diamonds + diamonds
            }
        }

        checkLevelUpdate(discordId)
    }

    private suspend fun checkLevelUpdate(discordId: String) {



        DatabaseExecutor.transaction {
            val member = MembersTable.selectAll()
                .where { MembersTable.discordId eq discordId }
                .singleOrNull() ?: return@transaction

            val exp = member[MembersTable.exp]
            val oldLvl = member[MembersTable.lvl]
            var lvl = member[MembersTable.lvl]
            var levelUp = member[MembersTable.levelUp]

            while (exp >= levelUp) {
                val newLevelUp = (levelUp + levelUp / 2)
                val newLvl = lvl + 1

                MembersTable.update(
                    where = { (MembersTable.discordId eq discordId) and (MembersTable.exp greaterEq MembersTable.levelUp) }
                ) {
                    it[MembersTable.levelUp] = newLevelUp
                    it[MembersTable.lvl] = MembersTable.lvl + 1
                    it[MembersTable.exp] = 0
                }

                lvl = newLvl
                levelUp = newLevelUp
                println("LevelUp: $levelUp")
            }

            if (lvl != oldLvl) {
                LevelUpMessage.send(
                    discordId = member.toMemberModel().discordId,
                    oldLvl = oldLvl,
                    exp = exp,
                    levelUp = levelUp,
                    lvl = lvl
                )
            }
        }
    }

    suspend fun exists(discordId: String): Boolean =
        DatabaseExecutor.transaction {
            !MembersTable
                .select(MembersTable.id)
                .where { MembersTable.discordId eq discordId }
                .empty()
        }

    suspend fun addDmMessageId(discordId: String, messageId: String) {
        DatabaseExecutor.transaction {
            val current = MembersTable.select(MembersTable.dmMessageIds)
                .where { MembersTable.discordId eq discordId }
                .singleOrNull()
                ?.get(MembersTable.dmMessageIds)
                ?.let { Json.decodeFromString<List<String>>(it) }
                ?: emptyList()

            val updated = current + messageId

            MembersTable.update(where = { MembersTable.discordId eq discordId }) {
                it[dmMessageIds] = Json.encodeToString(updated)
            }
        }
    }

    suspend fun getDmMessageIds(discordId: String) =
        DatabaseExecutor.transaction {
            MembersTable.select(MembersTable.dmMessageIds)
                .where { MembersTable.discordId eq discordId }
                .singleOrNull()
                ?.get(MembersTable.dmMessageIds)
                ?.let { Json.decodeFromString<List<String>>(it) }
                ?: emptyList()
        }

}
