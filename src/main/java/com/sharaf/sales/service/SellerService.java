package com.sharaf.sales.service;

import com.sharaf.sales.domain.Seller;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Seller}.
 */
public interface SellerService {

    /**
     * Save a seller.
     *
     * @param seller the entity to save.
     * @return the persisted entity.
     */
    Seller save(Seller seller);

    /**
     * Get all the sellers.
     *
     * @return the list of entities.
     */
    List<Seller> findAll();


    /**
     * Get the "id" seller.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Seller> findOne(Long id);

    /**
     * Delete the "id" seller.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
