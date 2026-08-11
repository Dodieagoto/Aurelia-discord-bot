package dev.dodia.database.repository

import dev.dodia.database.DatabaseExecutor
import dev.dodia.database.model.toMemberModel
import dev.dodia.database.table.MembersTable
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.plus
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
    }

    suspend fun exists(discordId: String): Boolean =
        DatabaseExecutor.transaction {
            !MembersTable
                .select(MembersTable.id)
                .where { MembersTable.discordId eq discordId }
                .empty()
        }

}
