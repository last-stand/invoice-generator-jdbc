package com.learning.invocegenerator.repository

import com.learning.invocegenerator.models.Product

interface ProductRepository {
    fun save(product: Product): Int
}