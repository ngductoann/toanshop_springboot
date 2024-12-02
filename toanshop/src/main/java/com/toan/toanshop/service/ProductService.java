package com.toan.toanshop.service;

import com.toan.toanshop.request.AddProductRequest;
import com.toan.toanshop.request.UpdateProductRequest;
import com.toan.toanshop.model.Product;

import java.util.List;

public interface ProductService {
    Product addProduct(AddProductRequest request);

    Product getProductById(Long id);

    void deleteProductByid(Long id);

    Product updateProduct(UpdateProductRequest request, Long id);

    List<Product> getAllProducts();

    List<Product> getProductsByCategory(String category);

    List<Product> getProductsByBrand(String brand);

    List<Product> getProductsByCategoryNameAndBrand(String category, String brand);

    List<Product> getProductsByName(String name);

    List<Product> getProductsByCategoryNameAndName(String category, String name);

    List<Product> getProductsByBrandAndName(String brand, String name);

    Long countProductsByBrandAndName(String brand, String name);
}
