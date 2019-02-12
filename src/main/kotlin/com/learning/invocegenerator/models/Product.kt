package com.learning.invocegenerator.models

data class Product(
    var productId: Int = 0,

    val name: String,

    val unitPrice: Double,

    val taxes: MutableSet<Tax>
)
