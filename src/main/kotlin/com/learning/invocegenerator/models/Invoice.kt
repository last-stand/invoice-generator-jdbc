package com.learning.invocegenerator.models

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class Invoice(
    var id: Int = 0,

    val customer: String,

    val invoiceItem: Set<InvoiceItem>,

    val date: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss"))

)