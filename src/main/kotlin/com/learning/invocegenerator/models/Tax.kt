package com.learning.invocegenerator.models

data class Tax (
        var taxId: Int = 0,
        val type: String,
        val rate: Double
)