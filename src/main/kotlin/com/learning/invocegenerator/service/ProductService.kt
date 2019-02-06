package com.learning.invocegenerator.service

import com.learning.invocegenerator.models.Product

interface ProductService {
    fun save(product: Product): Int
}