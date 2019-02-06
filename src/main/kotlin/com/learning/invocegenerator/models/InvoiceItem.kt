package com.learning.invocegenerator.models

class InvoiceItem (
    var id: Int = 0,

    val invoiceId: Int,

    val productId: Int,

    val quantity: Int
)
