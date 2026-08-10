package dev.dodia.database.repository

import dev.dodia.database.DatabaseExecutor
import dev.dodia.database.model.toMemberModel
import dev.dodia.database.table.MembersTable
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.plus
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.update
import org.jetbrains.exposed.v1.jdbc.upsert
import kotlin.time.Clock

object MemberRepository {

    suspend fun getMember(id: String) =
        DatabaseExecutor.transaction {
            MembersTable
                .select(
                    MembersTable.id,
                    MembersTable.exp,
                    MembersTable.discordId,
                    MembersTable.coins,
                    MembersTable.diamonds,
                    MembersTable.firstJoin,
                    MembersTable.isFirstJoin,
                )
                .where { MembersTable.discordId eq id }
                .singleOrNull()
                ?.toMemberModel()
        }

    suspend fun addMember(discordId: String) =
        DatabaseExecutor.transaction {
            MembersTable.upsert(MembersTable.discordId) {
                it[MembersTable.discordId] = discordId
                it[isFirstJoin] = false
                it[firstJoin] = Clock.System.now()
            }
        }

    suspend fun addCoinsToMember(discordId: String, amount: Long){
        DatabaseExecutor.transaction {
            MembersTable.update(
                where = { MembersTable.discordId eq discordId },
            ) {
                it[coins] = coins + amount
            }

        }
    }
}
