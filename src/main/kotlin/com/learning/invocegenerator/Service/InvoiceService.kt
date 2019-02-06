package com.learning.invocegenerator.Service

import com.learning.invocegenerator.models.InvoiceResult
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

    internal inner class InvoiceResultRowMapper : RowMapper<InvoiceResult> {
        @Throws(SQLException::class)
        override fun mapRow(resultSet: ResultSet, rowNum: Int): InvoiceResult {
            return InvoiceResult(invoiceId= resultSet.getInt("invoice_id"),
                    customer = resultSet.getString("customer"),
                    date = resultSet.getString("date"),
                    product = resultSet.getString("product"),
                    quantity = resultSet.getString("quantity").toInt()
            )
        }

    }

    fun findAll(): List<InvoiceResult> {
        val sql = "SELECT invoice.id as invoice_id, invoice.customer," +
                "product.name as product, invoice_item.quantity, invoice.date " +
                "FROM invoice " +
                "INNER JOIN invoice_item " +
                "ON invoice.id=invoice_item.invoice_item_id " +
                "INNER JOIN product " +
                "ON invoice_item.product_id=product.id"
        return jdbcTemplate.query(sql, InvoiceResultRowMapper())
    }

    fun findById(id: Int): List<InvoiceResult> {
        val sql = "SELECT invoice.id as invoice_id, invoice.customer, " +
                "product.name as product, invoice_item.quantity, invoice.date " +
                "FROM invoice " +
                "INNER JOIN invoice_item " +
                "ON invoice.id=invoice_item.invoice_item_id " +
                "INNER JOIN product " +
                "ON invoice_item.product_id=product.id " +
                "where invoice_item.invoice_item_id = " + id
        return jdbcTemplate.query(sql, InvoiceResultRowMapper())
    }
}