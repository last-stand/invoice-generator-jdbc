package com.learning.invocegenerator.repository

import com.learning.invocegenerator.models.InvoiceItem

interface InvoiceItemRepository {
    fun save(invoiceItem: InvoiceItem): Int
}