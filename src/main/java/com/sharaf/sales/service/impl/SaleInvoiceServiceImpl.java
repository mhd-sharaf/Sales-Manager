package com.sharaf.sales.service.impl;

import com.sharaf.sales.service.SaleInvoiceService;
import com.sharaf.sales.domain.SaleInvoice;
import com.sharaf.sales.domain.Transaction;
import com.sharaf.sales.repository.SaleInvoiceRepository;
import com.sharaf.sales.repository.TransactionRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

/**
 * Service Implementation for managing {@link SaleInvoice}.
 */
@Service
@Transactional
public class SaleInvoiceServiceImpl implements SaleInvoiceService {

	private final Logger log = LoggerFactory.getLogger(SaleInvoiceServiceImpl.class);

	private final SaleInvoiceRepository saleInvoiceRepository;
	private final TransactionRepository transactionRepository;

	public SaleInvoiceServiceImpl(SaleInvoiceRepository saleInvoiceRepository,
			TransactionRepository transactionRepository) {
		this.saleInvoiceRepository = saleInvoiceRepository;
		this.transactionRepository = transactionRepository;
	}

	@Override
	@Transactional
	public SaleInvoice save(SaleInvoice saleInvoice) {
		log.debug("Request to save SaleInvoice : {}", saleInvoice);

		Double total = 0D;
		Set<Transaction> transactions = saleInvoice.getTransactions();

		for (Iterator iterator = transactions.iterator(); iterator.hasNext();) {
			Transaction transaction = (Transaction) iterator.next();
			transaction.setSaleInvoice(saleInvoice);
			transactionRepository.save(transaction);
			total += transaction.getPrice() * transaction.getQuantity();
		}

		saleInvoice.setTransactions(transactions);
		saleInvoice.setTotal(total);
		return saleInvoiceRepository.save(saleInvoice);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<SaleInvoice> findAll(Pageable pageable) {
		log.debug("Request to get all SaleInvoices");
		return saleInvoiceRepository.findAll(pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<SaleInvoice> findOne(Long id) {
		log.debug("Request to get SaleInvoice : {}", id);
		return saleInvoiceRepository.findById(id);
	}

	@Override
	public void delete(Long id) {
		log.debug("Request to delete SaleInvoice : {}", id);
		saleInvoiceRepository.deleteById(id);
	}
}
