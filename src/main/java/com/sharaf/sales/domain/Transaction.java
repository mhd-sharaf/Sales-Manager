package com.sharaf.sales.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.Instant;

/**
 * A Transaction.
 */
@Entity
@Table(name = "transaction")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Transaction implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
	@SequenceGenerator(name = "sequenceGenerator")
	private Long id;

	@NotNull
	@Column(name = "quantity")
	private Long quantity;

	@NotNull
	@Column(name = "price")
	private Double price;

	@Column(name = "description")
	private String description;

	@Column(name = "creation_date")
	private Instant creationDate = Instant.now();

	@OneToOne
	@JoinColumn(unique = true)
	private Product product;

	@ManyToOne
	@JsonIgnoreProperties(value = "transactions", allowSetters = true)
	private SaleInvoice saleInvoice;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getQuantity() {
		return quantity;
	}

	public Transaction quantity(Long quantity) {
		this.quantity = quantity;
		return this;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public Double getPrice() {
		return price;
	}

	public Transaction price(Double price) {
		this.price = price;
		return this;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public Transaction description(String description) {
		this.description = description;
		return this;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Instant getCreationDate() {
		return creationDate;
	}

	public Transaction creationDate(Instant creationDate) {
		this.creationDate = creationDate;
		return this;
	}

	public void setCreationDate(Instant creationDate) {
		this.creationDate = creationDate;
	}

	public Product getProduct() {
		return product;
	}

	public Transaction product(Product product) {
		this.product = product;
		return this;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public SaleInvoice getSaleInvoice() {
		return saleInvoice;
	}

	public Transaction saleInvoice(SaleInvoice saleInvoice) {
		this.saleInvoice = saleInvoice;
		return this;
	}

	public void setSaleInvoice(SaleInvoice saleInvoice) {
		this.saleInvoice = saleInvoice;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Transaction)) {
			return false;
		}
		return id != null && id.equals(((Transaction) o).id);
	}

	@Override
	public int hashCode() {
		return 31;
	}

	// prettier-ignore
	@Override
	public String toString() {
		return "Transaction{" + "id=" + getId() + ", quantity=" + getQuantity() + ", price=" + getPrice()
				+ ", description='" + getDescription() + "'" + ", creationDate='" + getCreationDate() + "'" + "}";
	}
}
