package com.toan.toanshop.controller;

import com.toan.toanshop.Exception.ResourceNotFoundException;
import com.toan.toanshop.Request.AddProductRequest;
import com.toan.toanshop.Request.UpdateProductRequest;
import com.toan.toanshop.model.Product;
import com.toan.toanshop.response.ApiResponse;
import com.toan.toanshop.service.ProductService;

import org.hibernate.ResourceClosedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/products")
public class ProductController {
    @Autowired private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(new ApiResponse("Found all success!", products));
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long id) {
        try {
            Product product = productService.getProductById(id);
            return ResponseEntity.ok(new ApiResponse("Found Product!", product));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/product/add")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductRequest productRequest) {
        try {
            Product product = productService.addProduct(productRequest);
            return ResponseEntity.ok(new ApiResponse("Add product success", product));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Faild add product: " + e.getMessage(), null));
        }
    }

    @PutMapping("/product/update/{id}")
    public ResponseEntity<ApiResponse> updateProduct(
            @RequestBody UpdateProductRequest productRequest, @PathVariable Long id) {
        try {
            Product updatedProduct = productService.updateProduct(productRequest, id);
            return ResponseEntity.ok(new ApiResponse("updatd product success", updatedProduct));
        } catch (ResourceClosedException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Product not found", null));
        }
    }

    @DeleteMapping("/product/delete/{id}")
    public ResponseEntity<ApiResponse> deleteProductById(@PathVariable Long id) {
        try {
            productService.deleteProductByid(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new ApiResponse("Product deleted", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getProductByBrandAndName(
            @PathVariable String brandName, @PathVariable String productName) {
        List<Product> products = productService.getProductsByBrandAndName(brandName, productName);
        return ResponseEntity.ok(new ApiResponse("success", products));
    }


}
