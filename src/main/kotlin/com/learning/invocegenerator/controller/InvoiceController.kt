package com.learning.invocegenerator.controller

import com.learning.invocegenerator.Service.InvoiceService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin(origins = arrayOf("http://localhost:3000"), maxAge = 3000)
@RequestMapping("/api/invoice")
class InvoiceController{

    @Autowired
    lateinit var invoiceService: InvoiceService

    @GetMapping("/")
    fun getAllInvoices() =
            invoiceService.findAll()

    @GetMapping("/{id}")
    fun getInvoiceById(@PathVariable id: Int) =
            invoiceService.findById(id)
}
