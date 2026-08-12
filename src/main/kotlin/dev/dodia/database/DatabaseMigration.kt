package dev.dodia.database

import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.TransactionManager
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import dev.dodia.database.DataBase.Companion.db
import dev.dodia.database.table.MembersTable


object DatabaseMigration {

    fun migrate() {
        val tables = arrayOf(
            MembersTable,
        )

        transaction(db) {

            TransactionManager.current().db.dialectMetadata.resetCaches()
            SchemaUtils.createStatements(*tables).forEach { statement -> exec(statement) }
            TransactionManager.current().db.dialectMetadata.resetCaches()
            SchemaUtils.addMissingColumnsStatements(*tables).forEach { statement -> exec(statement) }
            TransactionManager.current().db.dialectMetadata.resetCaches()
        }
    }
}