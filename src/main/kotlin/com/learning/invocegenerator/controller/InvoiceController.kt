package com.learning.invocegenerator.controller

import com.learning.invocegenerator.repository.InvoiceRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin(origins = arrayOf("http://localhost:3000"), maxAge = 3000)
@RequestMapping(value = arrayOf("","/api/invoice"))
class InvoiceController{

    @Autowired
    lateinit var invoiceRepository: InvoiceRepository

    @GetMapping(path = arrayOf("", "/"))
    fun getAllInvoices() =
            invoiceRepository.findAll()

    @GetMapping("/{id}")
    fun getInvoiceById(@PathVariable id: Int) =
            invoiceRepository.findById(id)
}
