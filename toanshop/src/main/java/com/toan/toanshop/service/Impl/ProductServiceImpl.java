package com.toan.toanshop.service.Impl;

import com.toan.toanshop.Exception.ResourceNotFoundException;
import com.toan.toanshop.request.AddProductRequest;
import com.toan.toanshop.request.UpdateProductRequest;
import com.toan.toanshop.model.Category;
import com.toan.toanshop.model.Product;
import com.toan.toanshop.repository.CategoryRepository;
import com.toan.toanshop.repository.ProductRepository;
import com.toan.toanshop.service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired private final ProductRepository productRepository;
    @Autowired private final CategoryRepository categoryRepository;

    public ProductServiceImpl(
            ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Product addProduct(AddProductRequest request) {
        /* NOTE:
            Add product:
                1. check if the category is found in the DB
                2. If yes, set it as the new product category
                3. If no, the save it as a new category
                4. The set as the new product category
        */
        // Optional<Category> optionalCategory =
        //         categoryRepository.findByName(request.getCategory().getName());

        // Category category;
        // if (!optionalCategory.isPresent()) {
        //     category = categoryRepository.save(new Category(request.getCategory().getName()));
        // } else {
        //     category = optionalCategory.get();
        // }

        Category category =
                Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
                        .orElseGet(
                                () -> {
                                    return categoryRepository.save(
                                            new Category(request.getCategory().getName()));
                                });

        return productRepository.save(createProduct(request, category));
    }

    private Product createProduct(AddProductRequest request, Category category) {
        return new Product(
                request.getName(),
                request.getBrand(),
                request.getDescription(),
                request.getPrice(),
                request.getInventory(),
                category);
    }

    @Override
    public Product getProductById(Long id) {
        /* NOTE:
            Get Product:
                1. find product by id (name, ...)
                2. If found return Object else throw Exception
        */
        return productRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found!"));
    }

    @Override
    public void deleteProductByid(Long id) {
        /* NOTE:
            Delete Product:
                1. Verify whether the object exists
                2. If the object exists, delete it; else throw a new exception.
        */

        // Optional<Product> optionalProdcut = productRepository.findById(id);

        // if (optionalProdcut.isPresent()) { //  check object exists
        //     productRepository.delete(optionalProdcut.get());
        // } else {
        //     throw new ProductNotFoundException("Product not found!");
        // }
        // OR code:

        productRepository
                .findById(id)
                .ifPresentOrElse(
                        productRepository::delete,
                        () -> {
                            throw new ResourceNotFoundException("Product not found!");
                        });
        ;
    }

    @Override
    public Product updateProduct(UpdateProductRequest request, Long productId) {
        /* NOTE:
            Update Product:
                1. Check if the Object exists in db.
                2. If false throw a new exception; Else update the object and save to db
        */
        // Optional<Product> optionalProduct = productRepository.findById(productId);

        // if (!optionalProduct.isPresent()) {
        //     throw new ProductNotFoundException("Product not found!");
        // }

        // Product updatedProduct = updateExistingProduct(optionalProduct.get(), request);
        // return productRepository.save(updatedProduct);

        // lambda function
        return productRepository
                .findById(productId)
                .map(existingProduct -> updateExistingProduct(existingProduct, request))
                .map(productRepository::save)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found!"));
    }

    private Product updateExistingProduct(Product existingProduct, UpdateProductRequest request) {
        existingProduct.setName(request.getName());
        existingProduct.setDescription(request.getDescription());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setInventory(request.getInventory());
        existingProduct.setBrand(request.getBrand());

        Category category = categoryRepository.findByName(request.getCategory().getName());
        existingProduct.setCategory(category);
        return existingProduct;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByCategoryNameAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category, brand);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductsByCategoryNameAndName(String category, String name) {
        return productRepository.findByCategoryNameAndName(category, name);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand, name);
    }

    public Long countProductsByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand, name);
    }
}
