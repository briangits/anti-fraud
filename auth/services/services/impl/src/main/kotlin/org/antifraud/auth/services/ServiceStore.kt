package org.antifraud.auth.services

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable
import org.jetbrains.exposed.v1.dao.LongEntity
import org.jetbrains.exposed.v1.dao.LongEntityClass

internal object ServiceStore : LongIdTable("Services") {
    val name = varchar("name", 32)
    val serviceId = varchar("serviceId", 32).uniqueIndex()
    val role = enumerationByName<ServiceRole>("role", 32)
}

class ServiceDAO(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<ServiceDAO>(ServiceStore)

    var name by ServiceStore.name
    var serviceId by ServiceStore.serviceId
    var role by ServiceStore.role
}

fun ServiceDAO.toService(): Service = Service(name, serviceId, role)
