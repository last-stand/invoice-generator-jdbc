package com.learning.invocegenerator.models

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class InvoiceResult (
        val invoiceId: Int = 0,

        val customer: String,

        val product: String,

        val quantity: Int,

        val date: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss"))
)