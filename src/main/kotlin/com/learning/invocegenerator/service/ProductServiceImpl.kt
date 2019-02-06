package com.learning.invocegenerator.service

import com.learning.invocegenerator.`custom-db-util`.CustomH2JDBC
import com.learning.invocegenerator.models.Product
import org.springframework.stereotype.Service

@Service("productService")
class ProductServiceImpl : ProductService {

    override fun save(product: Product): Int {
        val sql = "INSERT INTO PRODUCT(name, unit_price) VALUES('"+ product.name +"', "+ product.unitPrice +");"
        val productId = CustomH2JDBC.executeUpdateStatement(sql)
        return productId as Int
    }
}