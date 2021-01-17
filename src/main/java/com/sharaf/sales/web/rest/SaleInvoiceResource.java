package com.sharaf.sales.web.rest;

import com.sharaf.sales.domain.SaleInvoice;
import com.sharaf.sales.service.SaleInvoiceService;
import com.sharaf.sales.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.sharaf.sales.domain.SaleInvoice}.
 */
@RestController
@RequestMapping("/api")
public class SaleInvoiceResource {

    private final Logger log = LoggerFactory.getLogger(SaleInvoiceResource.class);

    private static final String ENTITY_NAME = "saleInvoice";

    @Value("${clientApp}")
    private String applicationName;

    private final SaleInvoiceService saleInvoiceService;

    public SaleInvoiceResource(SaleInvoiceService saleInvoiceService) {
        this.saleInvoiceService = saleInvoiceService;
    }

    /**
     * {@code POST  /sale-invoices} : Create a new saleInvoice.
     *
     * @param saleInvoice the saleInvoice to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new saleInvoice, or with status {@code 400 (Bad Request)} if the saleInvoice has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sale-invoices")
    public ResponseEntity<SaleInvoice> createSaleInvoice(@RequestBody SaleInvoice saleInvoice) throws URISyntaxException {
        log.debug("REST request to save SaleInvoice : {}", saleInvoice);
        if (saleInvoice.getId() != null) {
            throw new BadRequestAlertException("A new saleInvoice cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SaleInvoice result = saleInvoiceService.save(saleInvoice);
        return ResponseEntity.created(new URI("/api/sale-invoices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sale-invoices} : Updates an existing saleInvoice.
     *
     * @param saleInvoice the saleInvoice to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated saleInvoice,
     * or with status {@code 400 (Bad Request)} if the saleInvoice is not valid,
     * or with status {@code 500 (Internal Server Error)} if the saleInvoice couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sale-invoices")
    public ResponseEntity<SaleInvoice> updateSaleInvoice(@RequestBody SaleInvoice saleInvoice) throws URISyntaxException {
        log.debug("REST request to update SaleInvoice : {}", saleInvoice);
        if (saleInvoice.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SaleInvoice result = saleInvoiceService.save(saleInvoice);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, saleInvoice.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /sale-invoices} : get all the saleInvoices.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of saleInvoices in body.
     */
    @GetMapping("/sale-invoices")
    public ResponseEntity<List<SaleInvoice>> getAllSaleInvoices(Pageable pageable) {
        log.debug("REST request to get a page of SaleInvoices");
        Page<SaleInvoice> page = saleInvoiceService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /sale-invoices/:id} : get the "id" saleInvoice.
     *
     * @param id the id of the saleInvoice to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the saleInvoice, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sale-invoices/{id}")
    public ResponseEntity<SaleInvoice> getSaleInvoice(@PathVariable Long id) {
        log.debug("REST request to get SaleInvoice : {}", id);
        Optional<SaleInvoice> saleInvoice = saleInvoiceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(saleInvoice);
    }

    /**
     * {@code DELETE  /sale-invoices/:id} : delete the "id" saleInvoice.
     *
     * @param id the id of the saleInvoice to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sale-invoices/{id}")
    public ResponseEntity<Void> deleteSaleInvoice(@PathVariable Long id) {
        log.debug("REST request to delete SaleInvoice : {}", id);
        saleInvoiceService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
