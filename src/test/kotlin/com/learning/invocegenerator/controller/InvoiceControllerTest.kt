package com.learning.invocegenerator.controller

import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class InvoiceControllerTest {

//    lateinit var mockMvc: MockMvc
//
//    @InjectMocks
//    lateinit var invoiceController: InvoiceController
//
//    @Mock
//    lateinit var invoiceServiceRepository: InvoiceRepository
//
//    @Before
//    fun setUp() {
//        MockitoAnnotations.initMocks(this)
//        mockMvc = MockMvcBuilders.standaloneSetup(invoiceController).build()
//    }
//
//    @Test
//    fun testGetAllInvoicesWillReturnCollectionOfInvoiceObject() {
//        val invoiceItem: Set<InvoiceItem> = emptySet()
//        val invoice = Invoice(1, "You", invoiceItem, "23-05-2018")
//        val invoiceList: List<Invoice> = listOf(invoice)
//        given(invoiceServiceRepository.findAll()).willReturn(invoiceList)
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/invoice/"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo { result ->
//                    val invoiceJson = result.getResponse().getContentAsString()
//                    val mapper = jacksonObjectMapper()
//                    val resultInvoiceList: List<Invoice> = mapper.readValue(invoiceJson)
//                    assertEquals(invoiceList, resultInvoiceList)
//                }
//        BDDMockito.verify(invoiceServiceRepository, BDDMockito.times(1)).findAll()
//        Mockito.verifyNoMoreInteractions(invoiceServiceRepository)
//    }
//
//    @Test
//    fun testGetInvoiceByIdWillReturnAnInvoiceObject() {
//        val invoiceItem: Set<InvoiceItem> = emptySet()
//        val invoice = Invoice(1, "You", invoiceItem, "12-09-2018")
//        var optionalInvoice = Optional.of(invoice)
//        given(invoiceServiceRepository.findById(1)).willReturn(optionalInvoice)
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/invoice/1"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo { result ->
//                    val invoiceJson = result.getResponse().getContentAsString()
//                    val mapper = jacksonObjectMapper()
//                    val resultInvoice: Invoice = mapper.readValue(invoiceJson)
//                    assertEquals(invoice, resultInvoice)
//                }
//        BDDMockito.verify(invoiceServiceRepository, BDDMockito.times(1)).findById(1)
//        Mockito.verifyNoMoreInteractions(invoiceServiceRepository)
//    }
}