package com.learning.invocegenerator.models

class InvoiceItem (
        var invoiceItemId: Int = 0,

        val product: Product,

        val quantity: Int
)
