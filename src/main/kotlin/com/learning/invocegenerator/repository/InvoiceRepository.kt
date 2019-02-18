package com.learning.invocegenerator.repository

import com.learning.invocegenerator.customDBUtil.DBUtils
import com.learning.invocegenerator.models.Invoice
import com.learning.invocegenerator.models.InvoiceItem
import com.learning.invocegenerator.models.Product
import com.learning.invocegenerator.models.Tax
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.PreparedStatementCreator
import org.springframework.jdbc.core.PreparedStatementSetter
import org.springframework.jdbc.core.ResultSetExtractor
import org.springframework.stereotype.Repository
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.util.*


@Repository
class InvoiceRepository {

    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate

    internal inner class TaxRowMapper {

        fun mapRow(resultSet: ResultSet): Tax {
            val tax = Tax(
                    taxId = resultSet.getInt("tax_id"),
                    taxType = resultSet.getString("tax_type"),
                    rate = resultSet.getDouble("rate")
            )
            return tax
        }

    }

    internal inner class ProductRowMapper {
        val taxRowMapper = TaxRowMapper()

         fun mapRow(resultSet: ResultSet): Product {
            val tax = taxRowMapper.mapRow(resultSet)
            val taxList = mutableSetOf(tax)

            val product = Product(
                    productId = resultSet.getInt("product_id"),
                    productName = resultSet.getString("product_name"),
                    unitPrice = resultSet.getDouble("unit_price"),
                    taxes = taxList
            )
            return product
        }
    }

    internal inner class InvoiceItemRowMapper {

         fun mapRow(resultSet: ResultSet, productList: List<Product>): InvoiceItem {
            val product = DBUtils.getProductFromListById(
                    productList,
                    resultSet.getInt("product_id")
            )
            return InvoiceItem(
                    invoiceItemId = resultSet.getInt("invoice_item_id"),
                    product = product!!,
                    quantity = resultSet.getInt("quantity")

            )
        }

    }

    internal inner class InvoiceRowMapper  {
        val invoiceItemRowMapper = InvoiceItemRowMapper()

         fun mapRow(resultSet: ResultSet, productList: List<Product>): Invoice {
             val invoiceItem = invoiceItemRowMapper.mapRow(resultSet, productList)
             val invoiceItemList = mutableSetOf(invoiceItem)
            return Invoice(invoiceId= resultSet.getInt("invoice_id"),
                    customer = resultSet.getString("customer"),
                    date = resultSet.getString("date"),
                    invoiceItem = invoiceItemList
            )
        }
    }

    internal inner class InvoiceResultSetExtractor : ResultSetExtractor<List<Invoice>>{
        val invoiceList = arrayListOf<Invoice>()
        val invoiceRowMapper = InvoiceRowMapper()
        val invoiceItemRowMapper = InvoiceItemRowMapper()

        @Throws(SQLException::class, DataAccessException::class)
        override fun extractData(resultSet: ResultSet): List<Invoice> {
            val productList = fetchProductsByIds(resultSet)

            while (resultSet.next()) {
                val invoiceId = resultSet.getInt("invoice_id")
                val invoice = DBUtils.getInvoiceFromListById(invoiceList, invoiceId)
                if(invoice == null) {
                    invoiceList.add(invoiceRowMapper.mapRow(resultSet, productList))
                }
                else {
                    invoice.invoiceItem.add(invoiceItemRowMapper.mapRow(resultSet, productList))
                }
            }

            resultSet.close()
            return invoiceList
        }

        private fun fetchProductsByIds(resultSet: ResultSet):  List<Product>{
            resultSet.afterLast()
            val productIds = mutableListOf<Int>()
            while (resultSet.previous()) {
                val productId = resultSet.getInt("product_id")
                productIds.add(productId)
            }

            return findProductsWithTaxesById(productIds)
        }
    }

    internal inner class ProductResultSetExtractor : ResultSetExtractor<List<Product>>{
        val productList = arrayListOf<Product>()
        val productRowMapper = ProductRowMapper()
        val taxRowMapper = TaxRowMapper()

        @Throws(SQLException::class, DataAccessException::class)
        override fun extractData(resultSet: ResultSet): List<Product> {
            while (resultSet.next()) {
                val productId = resultSet.getInt("product_id")
                val product = DBUtils.getProductFromListById(productList, productId)
                if(product == null) {
                    productList.add(productRowMapper.mapRow(resultSet))
                }
                else {
                    product.taxes.add(taxRowMapper.mapRow(resultSet))
                }
            }
            return productList
        }
    }

    private fun getPreparedStatementCreator(sql: String): PreparedStatementCreator {
        return object : PreparedStatementCreator {

            @Throws(SQLException::class)
            override fun createPreparedStatement(connection: Connection): PreparedStatement {
                val statement = connection.prepareStatement(
                        sql,
                        ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY
                )
                return statement
            }

        }
    }

    fun findAllInvoices(): List<Invoice> {
        val sql = "SELECT invoice.invoice_id, invoice.customer, invoice.date, " +
                "invoice_item.invoice_item_id, invoice_item.product_id, invoice_item.quantity " +
                "FROM invoice " +
                "   INNER JOIN invoice_item " +
                "ON invoice.invoice_id=invoice_item.invoice_id " +
                "   ORDER BY invoice.invoice_id"
        return jdbcTemplate.query(getPreparedStatementCreator(sql), InvoiceResultSetExtractor())!!
    }

    fun findAllProductWithTaxes(): List<Product> {
        val sql = "SELECT  product.product_id, product.name as product_name, product.unit_price, " +
                "tax.tax_id, tax.tax_type, tax.rate " +
                "FROM product " +
                "   INNER JOIN tax " +
                "ON product.product_id = tax.product_id"
        return jdbcTemplate.query(sql, ProductResultSetExtractor())!!
    }

    fun findProductsWithTaxesById(product_ids: List<Int>): List<Product> {
        val inClauseTemplate = product_ids.joinToString {"?"}
        val sql = "SELECT  product.product_id, product.name as product_name, product.unit_price, " +
                "tax.tax_id, tax.tax_type, tax.rate " +
                "FROM product " +
                "   INNER JOIN tax " +
                "ON product.product_id = tax.product_id " +
                "   INNER JOIN invoice_item " +
                "ON product.product_id = invoice_item.product_id " +
                "   WHERE product.product_id IN (" + inClauseTemplate + ")"
        return jdbcTemplate.query(sql, product_ids.toTypedArray(), ProductResultSetExtractor())!!
    }

    fun findInvoiceById(invoice_id: Int): List<Invoice> {
        val sql = "SELECT invoice.invoice_id, invoice.customer, invoice.date, " +
                "invoice_item.invoice_item_id, invoice_item.product_id, invoice_item.quantity " +
                "FROM invoice " +
                "   INNER JOIN invoice_item " +
                "ON invoice.invoice_id=invoice_item.invoice_id " +
                "   WHERE invoice.invoice_id = ?"
        return jdbcTemplate.query(
                getPreparedStatementCreator(sql),
                PreparedStatementSetter {ps: PreparedStatement -> ps.setInt(1, invoice_id) },
                InvoiceResultSetExtractor()
        )!!
    }
}