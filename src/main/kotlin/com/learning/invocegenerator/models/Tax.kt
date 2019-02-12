package com.learning.invocegenerator.models

data class Tax (
        var taxId: Int = 0,
        val taxType: String,
        val rate: Double
)