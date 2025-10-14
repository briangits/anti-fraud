import com.mysql.cj.jdbc.Driver
import org.antifraud.auth.keys.config.DBConfig
import org.jetbrains.exposed.v1.jdbc.Database
import org.kodein.di.DI
import org.kodein.di.bindSingleton

val DI = DI.direct {
    bindSingleton {
        Database.connect(
            url = "jdbc:mysql://${DBConfig.host}:${DBConfig.port}/${DBConfig.name}",
            driver = Driver::class.qualifiedName ?: "com.mysql.cj.jdbc.Driver",
            user = DBConfig.username,
            password = DBConfig.password
        )
    }
}