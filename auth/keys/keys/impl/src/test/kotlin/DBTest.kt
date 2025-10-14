
import org.antifraud.auth.keys.KeyStore
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.junit.jupiter.api.Test
import org.kodein.di.instance

class DBTest {

    @Test
    fun generateDBSchema() = transaction(DI.instance<Database>()) {
        val queries = SchemaUtils.createStatements(KeyStore)

        queries.forEach { query ->
            println("$query;")
        }
    }

}