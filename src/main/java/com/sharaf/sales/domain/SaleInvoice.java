package com.sharaf.sales.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.sharaf.sales.domain.enumeration.InvoiceStatus;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;


/**
 * A SaleInvoice.
 */
@Entity
@Table(name = "sale_invoice")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SaleInvoice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "total")
    private Double total;

    @Column(name = "discount")
    private Double discount;

    @Column(name = "creation_date")
    private Instant creationDate= Instant.now();

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private InvoiceStatus status=InvoiceStatus.DRAFT;

    @OneToOne
    @JoinColumn(unique = true)
    private Client client;

    @OneToOne
    @JoinColumn(unique = true)
    private Seller seller;

    @OneToMany(mappedBy = "saleInvoice")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Transaction> transactions = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getTotal() {
        return total;
    }

    public SaleInvoice total(Double total) {
        this.total = total;
        return this;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Double getDiscount() {
        return discount;
    }

    public SaleInvoice discount(Double discount) {
        this.discount = discount;
        return this;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public SaleInvoice creationDate(Instant creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public InvoiceStatus getStatus() {
        return status;
    }

    public SaleInvoice status(InvoiceStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(InvoiceStatus status) {
        this.status = status;
    }

    public Client getClient() {
        return client;
    }

    public SaleInvoice client(Client client) {
        this.client = client;
        return this;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Seller getSeller() {
        return seller;
    }

    public SaleInvoice seller(Seller seller) {
        this.seller = seller;
        return this;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public SaleInvoice transactions(Set<Transaction> transactions) {
        this.transactions = transactions;
        return this;
    }

    public SaleInvoice addTransactions(Transaction transaction) {
        this.transactions.add(transaction);
        transaction.setSaleInvoice(this);
        return this;
    }

    public SaleInvoice removeTransactions(Transaction transaction) {
        this.transactions.remove(transaction);
        transaction.setSaleInvoice(null);
        return this;
    }

    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SaleInvoice)) {
            return false;
        }
        return id != null && id.equals(((SaleInvoice) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "SaleInvoice{" +
            "id=" + getId() +
            ", total=" + getTotal() +
            ", discount=" + getDiscount() +
            ", creationDate='" + getCreationDate() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
