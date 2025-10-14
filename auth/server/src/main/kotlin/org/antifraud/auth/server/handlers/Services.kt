package org.antifraud.auth.server.handlers

import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingContext
import org.antifraud.auth.server.DI
import org.antifraud.auth.services.NoSuchServiceException
import org.antifraud.auth.services.Service
import org.antifraud.auth.services.ServiceRepository
import org.antifraud.auth.services.ServiceUpdate
import org.kodein.di.instance

val GetServicesHandler: suspend RoutingContext.() -> Unit = handler@{
    val serviceRepository = DI.instance<ServiceRepository>()
        .getAll()

    call.respond(serviceRepository)
}

val GetServiceHandler: suspend RoutingContext.() -> Unit = handler@{
    val serviceId = call.parameters["serviceId"]
    require(!serviceId.isNullOrBlank()) { "Invalid Service ID" }

    val service = DI.instance<ServiceRepository>()
        .get(serviceId) ?: throw NoSuchServiceException(serviceId)

    call.respond(service)
}

val ServiceRegistrationHandler: suspend RoutingContext.() -> Unit = handler@{
    val newService = call.receive<Service>()

    require(newService.serviceId.isNotBlank()) { "serviceId cannot be blank" }
    require(newService.name.isNotBlank()) { "name cannot be blank" }

    val service = DI.instance<ServiceRepository>()
        .register(newService)

    call.respond(service)
}

val UpdateServiceHandler: suspend RoutingContext.() -> Unit = handler@{
    val serviceId = call.parameters["serviceId"]
    require(!serviceId.isNullOrBlank()) { "Invalid Service ID" }

    val update = call.receive<ServiceUpdate>()

    if (update.name != null) {
        require(update.name!!.isNotBlank()) { "name cannot be blank" }
    }

    val updatedService = DI.instance<ServiceRepository>()
        .update(serviceId, update)

    call.respond(updatedService)
}

val DeleteServiceHandler: suspend RoutingContext.() -> Unit = handler@{
    val serviceId = call.parameters["serviceId"]
    require(!serviceId.isNullOrBlank()) { "Invalid Service ID" }

    val deletedService = DI.instance<ServiceRepository>()
        .delete(serviceId)

    call.respond(deletedService)
}
