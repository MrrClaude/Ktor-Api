package com.example.routes

import com.example.model.Product
import com.example.repository.ProductRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.productRoutes() {
    routing {
        route("/api/products") {

            get {
                call.respond(ProductRepository.getAll())
            }

            get("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                    ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                val product = ProductRepository.getById(id)
                    ?: return@get call.respond(HttpStatusCode.NotFound, "Not found")
                call.respond(product)
            }

            post {
                val product = call.receive<Product>()
                call.respond(HttpStatusCode.Created, ProductRepository.add(product))
            }

            put("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                    ?: return@put call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                val product = call.receive<Product>()
                val updated = ProductRepository.update(id, product)
                    ?: return@put call.respond(HttpStatusCode.NotFound, "Not found")
                call.respond(updated)
            }

            delete("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                    ?: return@delete call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                if (ProductRepository.delete(id))
                    call.respond(HttpStatusCode.NoContent)
                else
                    call.respond(HttpStatusCode.NotFound, "Not found")
            }
        }
    }
}