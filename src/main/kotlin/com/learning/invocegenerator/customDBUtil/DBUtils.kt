package com.learning.invocegenerator.customDBUtil

import com.learning.invocegenerator.models.Invoice
import com.learning.invocegenerator.models.Product
import java.util.ArrayList

class DBUtils {
    companion object {
        fun getProductFromListById(productList: ArrayList<Product>, productId: Int): Product? {
            return productList.filter({ obj ->
                obj.productId == productId
            }).firstOrNull()
        }

        fun getInvoiceFromListById(invoiceList: ArrayList<Invoice>, invoiceId: Int): Invoice? {
            return invoiceList.filter({ obj ->
                obj.invoiceId == invoiceId
            }).firstOrNull()
        }
    }
}