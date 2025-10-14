import org.antifraud.auth.services.SecretKeyStore
import org.antifraud.auth.services.ServiceStore
import kotlinx.coroutines.test.runTest
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.junit.jupiter.api.Test
import org.kodein.di.instance

class DatabaseTest {

    @Test
    fun generateDatabaseSchema() = runTest {
        transaction(DI.instance<Database>()) {
            val queries = SchemaUtils.createStatements(ServiceStore, SecretKeyStore)

            queries.forEach { println("$it;") }
        }
    }
}