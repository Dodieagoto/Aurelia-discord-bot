package dev.dodia.database

import org.jetbrains.exposed.v1.jdbc.Database
import java.io.File

class DataBase {
    companion object {
        lateinit var sqlite: Database
            private set
    }

    init {

        val dbFile = File("database.db")

        sqlite = Database.connect(
            url = "jdbc:sqlite:${dbFile.absolutePath}",
            driver = "org.sqlite.JDBC"
        )

        DatabaseMigration.migrate()
    }
}
