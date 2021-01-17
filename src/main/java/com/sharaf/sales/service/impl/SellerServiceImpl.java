package com.sharaf.sales.service.impl;

import com.sharaf.sales.service.SellerService;
import com.sharaf.sales.domain.Seller;
import com.sharaf.sales.repository.SellerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Seller}.
 */
@Service
@Transactional
public class SellerServiceImpl implements SellerService {

    private final Logger log = LoggerFactory.getLogger(SellerServiceImpl.class);

    private final SellerRepository sellerRepository;

    public SellerServiceImpl(SellerRepository sellerRepository) {
        this.sellerRepository = sellerRepository;
    }

    @Override
    public Seller save(Seller seller) {
        log.debug("Request to save Seller : {}", seller);
        return sellerRepository.save(seller);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Seller> findAll() {
        log.debug("Request to get all Sellers");
        return sellerRepository.findAll();
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<Seller> findOne(Long id) {
        log.debug("Request to get Seller : {}", id);
        return sellerRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Seller : {}", id);
        sellerRepository.deleteById(id);
    }
}
