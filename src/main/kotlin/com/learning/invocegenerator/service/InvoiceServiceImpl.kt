package com.learning.invocegenerator.service

import com.learning.invocegenerator.`custom-db-util`.CustomH2JDBC
import com.learning.invocegenerator.models.Invoice
import org.springframework.stereotype.Service

@Service
class InvoiceServiceImpl : InvoiceService {

    override fun save(invoice: Invoice): Int {
        val sql = "INSERT INTO INVOICE(customer, date) VALUES('"+ invoice.customer +"', '"+ invoice.date +"');"
        val invoiceId = CustomH2JDBC.executeUpdateStatement(sql)
        return invoiceId as Int
    }

//    override fun findAll(): Set<Invoice> {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override fun findById(id: Int): Invoice {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
}