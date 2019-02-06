package com.learning.invocegenerator.service

import com.learning.invocegenerator.models.InvoiceItem

interface InvoiceItemService {
    fun save(invoiceItem: InvoiceItem): Int
}