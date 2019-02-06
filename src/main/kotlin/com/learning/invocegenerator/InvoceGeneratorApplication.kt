package com.learning.invocegenerator

import com.learning.invocegenerator.models.Invoice
import com.learning.invocegenerator.models.InvoiceItem
import com.learning.invocegenerator.models.Product
import com.learning.invocegenerator.service.InvoiceItemService
import com.learning.invocegenerator.service.InvoiceService
import com.learning.invocegenerator.service.ProductService
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class InvoceGeneratorApplication {

    @Bean
    fun databaseInitlizer(invoiceItemService: InvoiceItemService,
                          productService: ProductService,
                          invoiceService: InvoiceService) = CommandLineRunner {

        val butter = Product( name= "Butter", unitPrice = 20.0)
        val knife = Product(name= "Knife", unitPrice= 30.0)
        val milk = Product(name= "Milk", unitPrice= 26.50)
        val productId1 = productService.save(butter)
        val productId2 = productService.save(knife)
        val productId3 = productService.save(milk)

        val invoice1 = Invoice(customer = "Me")
        val invoice2 = Invoice(customer = "You")
        val invoiceId1 = invoiceService.save(invoice1)
        val invoiceId2 = invoiceService.save(invoice2)

        val invoiceItem1 = InvoiceItem(invoiceId = invoiceId1, productId = productId1, quantity = 5)
        val invoiceItem2 = InvoiceItem(invoiceId = invoiceId1, productId = productId2, quantity = 1)
        val invoiceItem3 = InvoiceItem(invoiceId = invoiceId1, productId = productId3, quantity = 2)
        val invoiceItem4 = InvoiceItem(invoiceId = invoiceId2, productId = productId3, quantity = 8)
        invoiceItemService.save(invoiceItem1)
        invoiceItemService.save(invoiceItem2)
        invoiceItemService.save(invoiceItem3)
        invoiceItemService.save(invoiceItem4)
    }
}

fun main(args: Array<String>) {
    runApplication<InvoceGeneratorApplication>(*args)
}
