package com.learning.invocegenerator.repository

import com.learning.invocegenerator.customDBUtil.DBUtils
import com.learning.invocegenerator.models.Invoice
import com.learning.invocegenerator.models.InvoiceItem
import com.learning.invocegenerator.models.Product
import com.learning.invocegenerator.models.Tax
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

//    val invoiceRowMapper = BeanPropertyRowMapper.newInstance(Invoice::class.java)
//    val invoiceItemRowMapper = BeanPropertyRowMapper.newInstance(InvoiceItem::class.java)
//    val productRowMapper = BeanPropertyRowMapper.newInstance(Product::class.java)
//    val taxRowMapper = BeanPropertyRowMapper(Tax::class.java)

    internal inner class TaxRowMapper : RowMapper<Tax> {
        @Throws(SQLException::class)
        override fun mapRow(resultSet: ResultSet, rowNum: Int): Tax {
            val tax = Tax(
                    taxId = resultSet.getInt("tax_id"),
                    taxType = resultSet.getString("tax_type"),
                    rate = resultSet.getDouble("rate")
            )
            return tax
        }

    }

    internal inner class ProductRowMapper : RowMapper<Product> {

        @Throws(SQLException::class)
        override fun mapRow(resultSet: ResultSet, rowNum: Int): Product {
            val taxRowMapper = TaxRowMapper()
            val tax = taxRowMapper.mapRow(resultSet, rowNum)
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

    internal inner class InvoiceItemRowMapper : RowMapper<InvoiceItem> {

        @Throws(SQLException::class)
        override fun mapRow(resultSet: ResultSet, rowNum: Int): InvoiceItem {
            val productRowMapper = ProductRowMapper()
            val product = productRowMapper.mapRow(resultSet, rowNum)

            return InvoiceItem(
                    invoiceItemId = resultSet.getInt("invoice_item_id"),
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
            return Invoice(invoiceId= resultSet.getInt("invoice_id"),
                    customer = resultSet.getString("customer"),
                    date = resultSet.getString("date"),
                    invoiceItem = invoiceItemList
            )
        }

    }

    internal inner class InvoiceResultSetExtractor : ResultSetExtractor<List<Invoice>>{

        @Throws(SQLException::class, DataAccessException::class)
        override fun extractData(resultSet: ResultSet): List<Invoice> {
            val invoiceList = arrayListOf<Invoice>()
            val invoiceRowMapper = InvoiceRowMapper()
            val invoiceItemRowMapper = InvoiceItemRowMapper()
            var row = 0
            while (resultSet.next()) {
                val invoiceId = resultSet.getInt("invoice_id")
                val invoice = DBUtils.getInvoiceFromListById(invoiceList, invoiceId)
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

    internal inner class ProductResultSetExtractor : ResultSetExtractor<List<Product>>{

        @Throws(SQLException::class, DataAccessException::class)
        override fun extractData(resultSet: ResultSet): List<Product> {
            val productList = arrayListOf<Product>()
            val productRowMapper = ProductRowMapper()
            val taxRowMapper = TaxRowMapper()
            var row = 0
            while (resultSet.next()) {
                val productId = resultSet.getInt("product_id")
                val product = DBUtils.getProductFromListById(productList, productId)
                if(product == null) {
                    productList.add(productRowMapper.mapRow(resultSet, row++))
                }
                else {
                    product.taxes.add(taxRowMapper.mapRow(resultSet,row++))
                }
            }
            return productList
        }
    }

    fun findAll(): List<Invoice> {
        val sql = "SELECT invoice.invoice_id as invoice_id, invoice.customer as customer, invoice.date as date, " +
                "product.product_id as product_id, product.name as product_name, product.unit_price as unit_price, " +
                "invoice_item.invoice_item_id as invoice_item_id, invoice_item.quantity as quantity, " +
                "tax.tax_id as tax_id, tax.tax_type as tax_type, tax.rate as rate " +
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
        val sql = "SELECT invoice.invoice_id as invoice_id, invoice.customer as customer, invoice.date as date, " +
                "product.product_id as product_id, product.name as product_name, product.unit_price as unit_price, " +
                "invoice_item.invoice_item_id as invoice_item_id, invoice_item.quantity as quantity, " +
                "tax.tax_id as tax_id, tax.tax_type as tax_type, tax.rate as rate " +
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
