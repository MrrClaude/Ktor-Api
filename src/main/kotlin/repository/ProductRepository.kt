package com.example.repository

import com.example.model.Product
import kotlinx.serialization.json.Json
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

object ProductRepository {
    private val json = Json { prettyPrint = true }
    private val dataFile: Path = Paths.get("data", "products.json")
    private val products = loadProducts().toMutableList()
    private var nextId = (products.maxOfOrNull { it.id } ?: 0) + 1

    init {
        if (!Files.exists(dataFile)) {
            saveProducts()
        }
    }

    @Synchronized
    fun getAll(): List<Product> = products.toList()

    @Synchronized
    fun getById(id: Int): Product? = products.find { it.id == id }

    @Synchronized
    fun add(product: Product): Product {
        val new = product.copy(id = nextId++)
        products.add(new)
        saveProducts()
        return new
    }

    @Synchronized
    fun update(id: Int, product: Product): Product? {
        val index = products.indexOfFirst { it.id == id }
        if (index == -1) return null
        val updated = product.copy(id = id)
        products[index] = updated
        saveProducts()
        return updated
    }

    @Synchronized
    fun delete(id: Int): Boolean {
        val removed = products.removeIf { it.id == id }
        if (removed) {
            saveProducts()
        }
        return removed
    }

    private fun loadProducts(): List<Product> {
        return try {
            if (!Files.exists(dataFile)) return emptyList()
            val content = Files.readString(dataFile)
            if (content.isBlank()) return emptyList()
            json.decodeFromString<List<Product>>(content)
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun saveProducts() {
        Files.createDirectories(dataFile.parent)
        Files.writeString(dataFile, json.encodeToString(products))
    }
}
