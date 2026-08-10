package dev.dodia.database

import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.withContext
import dev.dodia.database.DataBase.Companion.sqlite
import org.jetbrains.exposed.v1.core.Transaction
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

object DatabaseExecutor {
    private val executor: ExecutorService = Executors.newVirtualThreadPerTaskExecutor()
    private val dispatcher = executor.asCoroutineDispatcher()

    suspend fun <T> transaction(block: Transaction.() -> T): T =
        withContext(dispatcher) {
            transaction(sqlite) {
                block()
            }
        }

    fun shutdown() {
        dispatcher.close()
        executor.shutdown()
    }
}
