import org.antifraud.auth.services.ServiceRepository
import org.antifraud.auth.services.client.ServiceRepositoryImpl
import org.antifraud.auth.services.client.config.AuthCredentials
import org.kodein.di.DI
import org.kodein.di.bindSingleton

val DI = DI.direct {
    bindSingleton<ServiceRepository> {
        ServiceRepositoryImpl(
            serviceId = AuthCredentials.serviceId,
            secretKey = AuthCredentials.secretKey
        )
    }
}