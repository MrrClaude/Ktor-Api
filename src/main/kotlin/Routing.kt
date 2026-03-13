package com.example

import com.example.model.Product
import com.example.repository.ProductRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {

    // Global error handling (keep from original)
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respondText(
                text = "500: $cause",
                status = HttpStatusCode.InternalServerError
            )
        }
    }

    routing {
        route("/api/products") {

            // GET all products
            get {
                call.respond(ProductRepository.getAll())
            }

            // GET product by ID
            get("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                    ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                val product = ProductRepository.getById(id)
                    ?: return@get call.respond(HttpStatusCode.NotFound, "Not found")
                call.respond(product)
            }

            // POST add new product
            post {
                val product = call.receive<Product>()
                call.respond(HttpStatusCode.Created, ProductRepository.add(product))
            }

            // PUT update product
            put("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                    ?: return@put call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                val product = call.receive<Product>()
                val updated = ProductRepository.update(id, product)
                    ?: return@put call.respond(HttpStatusCode.NotFound, "Not found")
                call.respond(updated)
            }

            // DELETE product
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