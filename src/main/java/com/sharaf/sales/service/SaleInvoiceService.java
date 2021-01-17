package com.sharaf.sales.service;

import com.sharaf.sales.domain.SaleInvoice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link SaleInvoice}.
 */
public interface SaleInvoiceService {

    /**
     * Save a saleInvoice.
     *
     * @param saleInvoice the entity to save.
     * @return the persisted entity.
     */
    SaleInvoice save(SaleInvoice saleInvoice);

    /**
     * Get all the saleInvoices.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SaleInvoice> findAll(Pageable pageable);


    /**
     * Get the "id" saleInvoice.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SaleInvoice> findOne(Long id);

    /**
     * Delete the "id" saleInvoice.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
