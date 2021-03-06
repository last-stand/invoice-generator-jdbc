package com.learning.invocegenerator.controller

import com.learning.invocegenerator.repository.InvoiceRepository
import org.springframework.http.MediaType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@CrossOrigin(origins = arrayOf("http://localhost:3000"), maxAge = 3000)
@RequestMapping(value = arrayOf("","/api/invoice"))
class InvoiceController{

    @Autowired
    lateinit var invoiceRepository: InvoiceRepository

    @GetMapping(path = arrayOf("", "/"))
    fun getAllInvoices() =
            invoiceRepository.findAllInvoices()

    @GetMapping("/{id}")
    fun getInvoiceById(@PathVariable id: Int) =
            invoiceRepository.findInvoiceById(id)

    @GetMapping("/tax")
    fun getAllProductsWithTaxes() =
            invoiceRepository.findAllProductWithTaxes()
}