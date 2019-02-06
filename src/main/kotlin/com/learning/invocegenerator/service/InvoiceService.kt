package com.learning.invocegenerator.service

import com.learning.invocegenerator.models.Invoice

interface InvoiceService {
    fun save(invoice: Invoice): Int
//    fun findAll(): Set<Invoice>
//    fun findById(id: Int): Invoice
}
