package dev.dodia.database

import io.github.cdimascio.dotenv.Dotenv
import org.jetbrains.exposed.v1.jdbc.Database
import java.io.File

class DataBase {
    companion object {
        lateinit var db: Database
            private set
    }

    init {
        db = Database.connect(
            url = "jdbc:postgresql://178.215.238.139:5432/aure",
            driver = "org.postgresql.Driver",
            user = Dotenv.load().get("DB_USER", ""),
            password = Dotenv.load().get("DB_PASSWORD", "")
        )

        DatabaseMigration.migrate()
    }
}
