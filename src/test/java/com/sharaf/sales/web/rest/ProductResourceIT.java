package com.sharaf.sales.web.rest;

import com.sharaf.sales.SalesManagementApplication;
import com.sharaf.sales.domain.Product;
import com.sharaf.sales.repository.ProductRepository;
import com.sharaf.sales.service.ProductService;

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

/**
 * Integration tests for the {@link ProductResource} REST controller.
 */
@SpringBootTest(classes = SalesManagementApplication.class)
@AutoConfigureMockMvc
public class ProductResourceIT {

	private static final String DEFAULT_NAME = "AAAAAAAAAA";
	private static final String UPDATED_NAME = "BBBBBBBBBB";

	private static final String DEFAULT_DESCRIPTION = "CCCCCCCCCCCCCCCCCCC";
	private static final String UPDATED_DESCRIPTION = "DDDDDDDDDDDDDDDDDDDD";

	private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
	private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ProductService productService;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private MockMvc restProductMockMvc;

	private Product product;

	/**
	 * Create an entity for this test.
	 *
	 * This is a static method, as tests for other entities might also need it, if
	 * they test an entity which requires the current entity.
	 */
	public static Product createEntity(EntityManager em) {
		Product product = new Product().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION)
				.creationDate(DEFAULT_CREATION_DATE);
		return product;
	}

	/**
	 * Create an updated entity for this test.
	 *
	 * This is a static method, as tests for other entities might also need it, if
	 * they test an entity which requires the current entity.
	 */
	public static Product createUpdatedEntity(EntityManager em) {
		Product product = new Product().name(UPDATED_NAME).description(UPDATED_DESCRIPTION)
				.creationDate(UPDATED_CREATION_DATE);
		return product;
	}

	@BeforeEach
	public void initTest() {
		product = createEntity(entityManager);
	}

	@Test
	@Transactional
	public void createProduct() throws Exception {
		int databaseSizeBeforeCreate = productRepository.findAll().size();
		// Create the Product
		restProductMockMvc.perform(post("/api/products").contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(product))).andExpect(status().isCreated());

		// Validate the Product in the database
		List<Product> productList = productRepository.findAll();
		assertThat(productList).hasSize(databaseSizeBeforeCreate + 1);
		Product testProduct = productList.get(productList.size() - 1);
		assertThat(testProduct.getName()).isEqualTo(DEFAULT_NAME);
		assertThat(testProduct.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
		assertThat(testProduct.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
	}

	@Test
	@Transactional
	public void createProductWithExistingId() throws Exception {
		int databaseSizeBeforeCreate = productRepository.findAll().size();

		// Create the Product with an existing ID
		product.setId(1L);

		// An entity with an existing ID cannot be created, so this API call must fail
		restProductMockMvc.perform(post("/api/products").contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(product))).andExpect(status().isBadRequest());

		// Validate the Product in the database
		List<Product> productList = productRepository.findAll();
		assertThat(productList).hasSize(databaseSizeBeforeCreate);
	}

	@Test
	@Transactional
	public void getAllProducts() throws Exception {
		// Initialize the database
		productRepository.saveAndFlush(product);

		// Get all the productList
		restProductMockMvc.perform(get("/api/products?sort=id,desc")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(product.getId().intValue())))
				.andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
				.andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
				.andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())));
	}

	@Test
	@Transactional
	public void getProduct() throws Exception {
		// Initialize the database
		productRepository.saveAndFlush(product);

		// Get the product
		restProductMockMvc.perform(get("/api/products/{id}", product.getId())).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.id").value(product.getId().intValue()))
				.andExpect(jsonPath("$.name").value(DEFAULT_NAME))
				.andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
				.andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()));
	}

	@Test
	@Transactional
	public void getNonExistingProduct() throws Exception {
		// Get the product
		restProductMockMvc.perform(get("/api/products/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
	}

	@Test
	@Transactional
	public void updateProduct() throws Exception {
		// Initialize the database
		productService.save(product);

		int databaseSizeBeforeUpdate = productRepository.findAll().size();

		// Update the product
		Product updatedProduct = productRepository.findById(product.getId()).get();
		// Disconnect from session so that the updates on updatedProduct are not
		// directly saved in db
		entityManager.detach(updatedProduct);
		updatedProduct.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).creationDate(UPDATED_CREATION_DATE);

		restProductMockMvc.perform(put("/api/products").contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(updatedProduct))).andExpect(status().isOk());

		// Validate the Product in the database
		List<Product> productList = productRepository.findAll();
		assertThat(productList).hasSize(databaseSizeBeforeUpdate);
		Product testProduct = productList.get(productList.size() - 1);
		assertThat(testProduct.getName()).isEqualTo(UPDATED_NAME);
		assertThat(testProduct.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
		assertThat(testProduct.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
	}

	@Test
	@Transactional
	public void updateNonExistingProduct() throws Exception {
		int databaseSizeBeforeUpdate = productRepository.findAll().size();

		// If the entity doesn't have an ID, it will throw BadRequestAlertException
		restProductMockMvc.perform(put("/api/products").contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(product))).andExpect(status().isBadRequest());

		// Validate the Product in the database
		List<Product> productList = productRepository.findAll();
		assertThat(productList).hasSize(databaseSizeBeforeUpdate);
	}

	@Test
	@Transactional
	public void deleteProduct() throws Exception {
		// Initialize the database
		productService.save(product);

		int databaseSizeBeforeDelete = productRepository.findAll().size();

		// Delete the product
		restProductMockMvc.perform(delete("/api/products/{id}", product.getId()).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());

		// Validate the database contains one less item
		List<Product> productList = productRepository.findAll();
		assertThat(productList).hasSize(databaseSizeBeforeDelete - 1);
	}
}
