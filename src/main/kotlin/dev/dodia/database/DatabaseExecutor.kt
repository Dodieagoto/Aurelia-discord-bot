package dev.dodia.database

import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import dev.dodia.database.DataBase.Companion.db
import org.jetbrains.exposed.v1.core.Transaction
import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.ExecutorService
import kotlinx.coroutines.withContext
import java.util.concurrent.Executors

object DatabaseExecutor {
    private val executor: ExecutorService = Executors.newVirtualThreadPerTaskExecutor()
    private val dispatcher = executor.asCoroutineDispatcher()

    suspend fun <T> transaction(block: Transaction.() -> T): T =
        withContext(dispatcher) {
            transaction(db) {
                block()
            }
        }

    fun shutdown() {
        dispatcher.close()
        executor.shutdown()
    }
}
