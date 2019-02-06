package com.learning.invocegenerator.repository

import com.learning.invocegenerator.models.Invoice

interface InvoiceRepository {
    fun save(invoice: Invoice): Int
}
