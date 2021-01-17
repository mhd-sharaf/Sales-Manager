package com.sharaf.sales.repository;

import com.sharaf.sales.domain.SaleInvoice;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the SaleInvoice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SaleInvoiceRepository extends JpaRepository<SaleInvoice, Long> {
}
