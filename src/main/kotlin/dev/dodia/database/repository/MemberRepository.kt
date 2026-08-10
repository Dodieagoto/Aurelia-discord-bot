package dev.dodia.database.repository

import dev.dodia.database.DatabaseExecutor
import dev.dodia.database.model.toMemberModel
import dev.dodia.database.table.MembersTable
import net.dv8tion.jda.api.entities.Member
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.upsert
import kotlin.time.Clock

object MemberRepository {

    suspend fun getMember(id: String) =
        DatabaseExecutor.transaction {

            MembersTable
                .select(
                    MembersTable.id,
                    MembersTable.exp,
                    MembersTable.coins,
                    MembersTable.diamonds,
                    MembersTable.firstJoin,
                )
                .where { MembersTable.discordId eq id }
                .singleOrNull()
                ?.toMemberModel()
        }

    suspend fun addMember(member: Member) {
        DatabaseExecutor.transaction {
            MembersTable.upsert(MembersTable.discordId) {
                it[discordId] = member.id
                it[isFirstJoin] = false
                it[firstJoin] = Clock.System.now()
            }
        }
    }

}

