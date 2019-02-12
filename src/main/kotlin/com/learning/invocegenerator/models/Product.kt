package com.learning.invocegenerator.models

data class Product(
    var productId: Int = 0,

    val productName: String,

    val unitPrice: Double,

    val taxes: MutableSet<Tax>
)
