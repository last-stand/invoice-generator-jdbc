package com.learning.invocegenerator.Service

import com.learning.invocegenerator.models.Invoice
import com.learning.invocegenerator.models.InvoiceItem
import com.learning.invocegenerator.models.Product
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Service
import java.sql.ResultSet
import java.sql.SQLException

@Service
class InvoiceService {
    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate

    internal inner class InvoiceRowMapper : RowMapper<Invoice> {
        @Throws(SQLException::class)
        override fun mapRow(resultSet: ResultSet, rowNum: Int): Invoice {
            val product = Product(
                    name = resultSet.getString("product"),
                    unitPrice = resultSet.getDouble("unit_price")
            )
            val invoiceItem =  InvoiceItem(
                    product = product,
                    quantity = resultSet.getInt("quantity")

            )
            val invoiceItemList: Set<InvoiceItem> = setOf(invoiceItem)
            return Invoice(id= resultSet.getInt("invoice_id"),
                    customer = resultSet.getString("customer"),
                    date = resultSet.getString("date"),
                    invoiceItem = invoiceItemList
            )
        }

    }

    fun findAll(): List<Invoice> {
        val sql = "SELECT invoice.id as invoice_id, invoice.customer," +
                "product.name as product, product.unit_price, invoice_item.quantity, invoice.date " +
                "FROM invoice " +
                "INNER JOIN invoice_item " +
                "ON invoice.id=invoice_item.invoice_item_id " +
                "INNER JOIN product " +
                "ON invoice_item.product_id=product.id"
        return jdbcTemplate.query(sql, InvoiceRowMapper())
    }

    fun findById(id: Int): List<Invoice> {
        val sql = "SELECT invoice.id as invoice_id, invoice.customer, " +
                "product.name as product, product.unit_price, invoice_item.quantity, invoice.date " +
                "FROM invoice " +
                "INNER JOIN invoice_item " +
                "ON invoice.id=invoice_item.invoice_item_id " +
                "INNER JOIN product " +
                "ON invoice_item.product_id=product.id " +
                "where invoice_item.invoice_item_id = " + id
        return jdbcTemplate.query(sql, InvoiceRowMapper())
    }
}