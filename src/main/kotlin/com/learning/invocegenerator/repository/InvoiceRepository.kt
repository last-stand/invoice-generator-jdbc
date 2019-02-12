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
import java.util.*

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
            val invoiceItemRowMapper = InvoiceItemRowMapper()
            val invoiceItem = invoiceItemRowMapper.mapRow(resultSet, rowNum)
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
            val invoiceRowMapper = InvoiceRowMapper()
            val invoiceItemRowMapper = InvoiceItemRowMapper()
            var row = 0
            while (resultSet.next()) {
                val invoiceId = resultSet.getInt("invoice_id")
                val invoice = getInvoiceFromListById(invoiceList, invoiceId)
                if(invoice == null) {
                    invoiceList.add(invoiceRowMapper.mapRow(resultSet, row++))
                }
                else {
                    invoice.invoiceItem.add(invoiceItemRowMapper.mapRow(resultSet, row++))
                }
            }
            return invoiceList
        }
    }

    fun getInvoiceFromListById(invoiceList: ArrayList<Invoice>, invoiceId: Int): Invoice? {
       return invoiceList.filter({ obj ->
            obj.invoiceId == invoiceId
        }).firstOrNull()
    }

    fun findAll(): List<Invoice> {
        val sql = "SELECT invoice.invoice_id, invoice.customer, invoice.date, " +
        "product.product_id, product.name as product, product.unit_price, " +
        "invoice_item.invoice_item_id, invoice_item.quantity, " +
        "tax.tax_type, tax.rate " +
        "FROM invoice " +
        "   INNER JOIN invoice_item " +
        "ON invoice.invoice_id=invoice_item.invoice_id " +
        "   INNER JOIN product " +
        "ON invoice_item.product_id=product.product_id " +
        "   INNER JOIN tax " +
        "ON tax.product_id = product.product_id " +
        "   ORDER BY invoice.invoice_id"
        return jdbcTemplate.query(sql, InvoiceResultSetExtractor())!!
    }

    fun findById(id: Int): List<Invoice> {
        val sql = "SELECT invoice.invoice_id, invoice.customer, invoice.date, " +
                "product.product_id, product.name as product, product.unit_price, " +
                "invoice_item.invoice_item_id, invoice_item.quantity, " +
                "tax.tax_type, tax.rate " +
                "FROM invoice " +
                "   INNER JOIN invoice_item " +
                "ON invoice.invoice_id=invoice_item.invoice_id " +
                "   INNER JOIN product " +
                "ON invoice_item.product_id=product.product_id " +
                "   INNER JOIN tax " +
                "ON tax.product_id = product.product_id " +
                "   WHERE invoice_item.invoice_id = ?"
        return jdbcTemplate.query(sql, arrayOf(id), InvoiceResultSetExtractor())!!
    }
}
