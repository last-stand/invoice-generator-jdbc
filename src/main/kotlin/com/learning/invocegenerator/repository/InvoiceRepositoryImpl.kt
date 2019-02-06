package com.learning.invocegenerator.repository

import com.learning.invocegenerator.`custom-db-util`.CustomH2JDBC
import com.learning.invocegenerator.models.Invoice
import org.springframework.stereotype.Repository

@Repository
class InvoiceRepositoryImpl : InvoiceRepository {

    override fun save(invoice: Invoice): Int {
        val sql = "INSERT INTO INVOICE(customer, date) VALUES('"+ invoice.customer +"', '"+ invoice.date +"');"
        val invoiceId = CustomH2JDBC.executeUpdateStatement(sql)
        return invoiceId as Int
    }
}