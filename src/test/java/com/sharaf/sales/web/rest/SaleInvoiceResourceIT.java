package com.sharaf.sales.web.rest;

import com.sharaf.sales.SalesManagementApplication;
import com.sharaf.sales.domain.SaleInvoice;
import com.sharaf.sales.repository.SaleInvoiceRepository;
import com.sharaf.sales.service.SaleInvoiceService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sharaf.sales.domain.enumeration.InvoiceStatus;
/**
 * Integration tests for the {@link SaleInvoiceResource} REST controller.
 */
@SpringBootTest(classes = SalesManagementApplication.class)
@AutoConfigureMockMvc
public class SaleInvoiceResourceIT {

    private static final Double DEFAULT_TOTAL = 1D;
    private static final Double UPDATED_TOTAL = 2D;

    private static final Double DEFAULT_DISCOUNT = 1D;
    private static final Double UPDATED_DISCOUNT = 2D;

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final InvoiceStatus DEFAULT_STATUS = InvoiceStatus.DRAFT;
    private static final InvoiceStatus UPDATED_STATUS = InvoiceStatus.ACTIVATED;

    @Autowired
    private SaleInvoiceRepository saleInvoiceRepository;

    @Autowired
    private SaleInvoiceService saleInvoiceService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSaleInvoiceMockMvc;

    private SaleInvoice saleInvoice;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SaleInvoice createEntity(EntityManager em) {
        SaleInvoice saleInvoice = new SaleInvoice()
            .total(DEFAULT_TOTAL)
            .discount(DEFAULT_DISCOUNT)
            .creationDate(DEFAULT_CREATION_DATE)
            .status(DEFAULT_STATUS);
        return saleInvoice;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SaleInvoice createUpdatedEntity(EntityManager em) {
        SaleInvoice saleInvoice = new SaleInvoice()
            .total(UPDATED_TOTAL)
            .discount(UPDATED_DISCOUNT)
            .creationDate(UPDATED_CREATION_DATE)
            .status(UPDATED_STATUS);
        return saleInvoice;
    }

    @BeforeEach
    public void initTest() {
        saleInvoice = createEntity(em);
    }

    @Test
    @Transactional
    public void createSaleInvoice() throws Exception {
        int databaseSizeBeforeCreate = saleInvoiceRepository.findAll().size();
        // Create the SaleInvoice
        restSaleInvoiceMockMvc.perform(post("/api/sale-invoices")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(saleInvoice)))
            .andExpect(status().isCreated());

        // Validate the SaleInvoice in the database
        List<SaleInvoice> saleInvoiceList = saleInvoiceRepository.findAll();
        assertThat(saleInvoiceList).hasSize(databaseSizeBeforeCreate + 1);
        SaleInvoice testSaleInvoice = saleInvoiceList.get(saleInvoiceList.size() - 1);
        assertThat(testSaleInvoice.getTotal()).isEqualTo(DEFAULT_TOTAL);
        assertThat(testSaleInvoice.getDiscount()).isEqualTo(DEFAULT_DISCOUNT);
        assertThat(testSaleInvoice.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testSaleInvoice.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createSaleInvoiceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = saleInvoiceRepository.findAll().size();

        // Create the SaleInvoice with an existing ID
        saleInvoice.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSaleInvoiceMockMvc.perform(post("/api/sale-invoices")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(saleInvoice)))
            .andExpect(status().isBadRequest());

        // Validate the SaleInvoice in the database
        List<SaleInvoice> saleInvoiceList = saleInvoiceRepository.findAll();
        assertThat(saleInvoiceList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllSaleInvoices() throws Exception {
        // Initialize the database
        saleInvoiceRepository.saveAndFlush(saleInvoice);

        // Get all the saleInvoiceList
        restSaleInvoiceMockMvc.perform(get("/api/sale-invoices?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(saleInvoice.getId().intValue())))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].discount").value(hasItem(DEFAULT_DISCOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }
    
    @Test
    @Transactional
    public void getSaleInvoice() throws Exception {
        // Initialize the database
        saleInvoiceRepository.saveAndFlush(saleInvoice);

        // Get the saleInvoice
        restSaleInvoiceMockMvc.perform(get("/api/sale-invoices/{id}", saleInvoice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(saleInvoice.getId().intValue()))
            .andExpect(jsonPath("$.total").value(DEFAULT_TOTAL.doubleValue()))
            .andExpect(jsonPath("$.discount").value(DEFAULT_DISCOUNT.doubleValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingSaleInvoice() throws Exception {
        // Get the saleInvoice
        restSaleInvoiceMockMvc.perform(get("/api/sale-invoices/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSaleInvoice() throws Exception {
        // Initialize the database
        saleInvoiceService.save(saleInvoice);

        int databaseSizeBeforeUpdate = saleInvoiceRepository.findAll().size();

        // Update the saleInvoice
        SaleInvoice updatedSaleInvoice = saleInvoiceRepository.findById(saleInvoice.getId()).get();
        // Disconnect from session so that the updates on updatedSaleInvoice are not directly saved in db
        em.detach(updatedSaleInvoice);
        updatedSaleInvoice
            .total(UPDATED_TOTAL)
            .discount(UPDATED_DISCOUNT)
            .creationDate(UPDATED_CREATION_DATE)
            .status(UPDATED_STATUS);

        restSaleInvoiceMockMvc.perform(put("/api/sale-invoices")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedSaleInvoice)))
            .andExpect(status().isOk());

        // Validate the SaleInvoice in the database
        List<SaleInvoice> saleInvoiceList = saleInvoiceRepository.findAll();
        assertThat(saleInvoiceList).hasSize(databaseSizeBeforeUpdate);
        SaleInvoice testSaleInvoice = saleInvoiceList.get(saleInvoiceList.size() - 1);
        assertThat(testSaleInvoice.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testSaleInvoice.getDiscount()).isEqualTo(UPDATED_DISCOUNT);
        assertThat(testSaleInvoice.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testSaleInvoice.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingSaleInvoice() throws Exception {
        int databaseSizeBeforeUpdate = saleInvoiceRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSaleInvoiceMockMvc.perform(put("/api/sale-invoices")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(saleInvoice)))
            .andExpect(status().isBadRequest());

        // Validate the SaleInvoice in the database
        List<SaleInvoice> saleInvoiceList = saleInvoiceRepository.findAll();
        assertThat(saleInvoiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSaleInvoice() throws Exception {
        // Initialize the database
        saleInvoiceService.save(saleInvoice);

        int databaseSizeBeforeDelete = saleInvoiceRepository.findAll().size();

        // Delete the saleInvoice
        restSaleInvoiceMockMvc.perform(delete("/api/sale-invoices/{id}", saleInvoice.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SaleInvoice> saleInvoiceList = saleInvoiceRepository.findAll();
        assertThat(saleInvoiceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
