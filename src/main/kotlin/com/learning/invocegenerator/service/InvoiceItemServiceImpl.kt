package com.learning.invocegenerator.service

import com.learning.invocegenerator.`custom-db-util`.CustomH2JDBC
import com.learning.invocegenerator.models.InvoiceItem
import org.springframework.stereotype.Service

@Service
class InvoiceItemServiceImpl : InvoiceItemService {

    override fun save(invoiceItem: InvoiceItem): Int {
        val sql = "INSERT INTO INVOICE_ITEM(invoice_item_id, product_id, quantity)" +
                     "VALUES("+ invoiceItem.invoiceId +", "+ invoiceItem.productId +" ,"+ invoiceItem.quantity +");"
        val invoiceItemId = CustomH2JDBC.executeUpdateStatement(sql)
        return invoiceItemId as Int
    }
}