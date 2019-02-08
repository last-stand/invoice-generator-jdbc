package com.learning.invocegenerator.repository

import com.learning.invocegenerator.models.Invoice
import com.learning.invocegenerator.models.InvoiceItem
import com.learning.invocegenerator.models.Product
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.ResultSetExtractor
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.sql.SQLException

@Repository
class InvoiceRepository {

    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate

    internal inner class InvoiceItemRowMapper : RowMapper<InvoiceItem> {

        @Throws(SQLException::class)
        override fun mapRow(resultSet: ResultSet, rowNum: Int): InvoiceItem {
            val product = Product(
                    id = resultSet.getInt("product_id"),
                    name = resultSet.getString("product"),
                    unitPrice = resultSet.getDouble("unit_price")
            )
            return InvoiceItem(
                    id = resultSet.getInt("invoice_item_id"),
                    product = product,
                    quantity = resultSet.getInt("quantity")

            )
        }

    }

    internal inner class InvoiceRowMapper : RowMapper<Invoice> {

        @Throws(SQLException::class)
        override fun mapRow(resultSet: ResultSet, rowNum: Int): Invoice {
            val invoiceItem = InvoiceItemRowMapper().mapRow(resultSet, rowNum)
            val invoiceItemList = mutableSetOf(invoiceItem)
            return Invoice(id= resultSet.getInt("invoice_id"),
                    customer = resultSet.getString("customer"),
                    date = resultSet.getString("date"),
                    invoiceItem = invoiceItemList
            )
        }

    }

    internal inner class InvoiceResultSetExtractor : ResultSetExtractor<List<Invoice>>{

        @Throws(SQLException::class, DataAccessException::class)
        override fun extractData(resultSet: ResultSet): List<Invoice> {
            var invoiceList = arrayListOf<Invoice>()
            var currentInvoice: Invoice? = null
            var invoiceItem: InvoiceItem? = null
            var invoiceId: Int = -1
            var row = 0
            while (resultSet.next()) {
                if(currentInvoice == null || !invoiceId.equals(resultSet.getInt("invoice_id"))) {
                    invoiceId = resultSet.getInt("invoice_id")
                    currentInvoice = InvoiceRowMapper().mapRow(resultSet, row++)
                    invoiceList.add(currentInvoice)
                }
                else {
                    invoiceItem = InvoiceItemRowMapper().mapRow(resultSet, row++)
                    currentInvoice.invoiceItem.add(invoiceItem)
                }
            }
            return invoiceList
        }
    }

    fun findAll(): List<Invoice> {
        val sql = "SELECT invoice.id as invoice_id, invoice.customer," +
                "product.id as product_id, product.name as product, product.unit_price, " +
                "invoice_item.id as invoice_item_id, invoice_item.quantity, invoice.date " +
                "FROM invoice " +
                "INNER JOIN invoice_item " +
                "ON invoice.id=invoice_item.invoice_item_id " +
                "INNER JOIN product " +
                "ON invoice_item.product_id=product.id"
        return jdbcTemplate.query(sql, InvoiceResultSetExtractor())!!
    }

    fun findById(id: Int): List<Invoice> {
        val sql = "SELECT invoice.id as invoice_id, invoice.customer, " +
                "product.id as product_id, product.name as product, product.unit_price, " +
                "invoice_item.id as invoice_item_id, invoice_item.quantity, invoice.date " +
                "FROM invoice " +
                "INNER JOIN invoice_item " +
                "ON invoice.id=invoice_item.invoice_item_id " +
                "INNER JOIN product " +
                "ON invoice_item.product_id=product.id " +
                "where invoice_item.invoice_item_id = ?"
        return jdbcTemplate.query(sql, arrayOf(id), InvoiceResultSetExtractor())!!
    }
}
