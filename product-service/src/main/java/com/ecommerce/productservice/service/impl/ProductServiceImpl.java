package com.ecommerce.productservice.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ecommerce.common.events.ProductEvent;
//import com.ecommerce.productservice.dto.ProductEvent;
import com.ecommerce.productservice.entity.Product;
//import com.ecommerce.productservice.kafka.ProductProducer;
import com.ecommerce.productservice.repository.ProductRepository;
import com.ecommerce.productservice.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	private ProductRepository productRepository;
//	private ProductProducer productProducer;

//	public ProductServiceImpl(ProductRepository productRepository,
//			ProductProducer productProducer) {
//		this.productRepository = productRepository;
//		this.productProducer = productProducer;
//	}
	
	public ProductServiceImpl(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}


	@Override
	public Product createProduct(Product product) {
		Product saved = productRepository.save(product);

//	    ProductEvent event = new ProductEvent(
//	            saved.getId(),
//	            saved.getName(),
//	            saved.getPrice()
//	    );

//	    productProducer.sendProductEvent(event);

	    return saved;
	}

	@Override
	public List<Product> getAllProducts() {
		return productRepository.findAll();
	}


	@Override
	public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }
}
