package com.toan.toanshop.controller;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;

import com.toan.toanshop.Exception.ResourceNotFoundException;
import com.toan.toanshop.model.Product;
import com.toan.toanshop.request.AddProductRequest;
import com.toan.toanshop.request.UpdateProductRequest;
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
import org.springframework.web.bind.annotation.RequestParam;
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
        try {
            List<Product> products = productService.getAllProducts();
            return ResponseEntity.ok(new ApiResponse("Success", products));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Unexcepted erro", e.getMessage()));
        }
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long id) {
        try {
            Product product = productService.getProductById(id);
            return ResponseEntity.ok(new ApiResponse("Success", product));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse("Not found", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Unexcepted error", e.getMessage()));
        }
    }

    @PostMapping("/product/add")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductRequest productRequest) {
        try {
            Product product = productService.addProduct(productRequest);
            return ResponseEntity.ok(new ApiResponse("Success", product));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Unexcepted error", e.getMessage()));
        }
    }

    @PutMapping("/product/update/{id}")
    public ResponseEntity<ApiResponse> updateProduct(
            @RequestBody UpdateProductRequest productRequest, @PathVariable Long id) {
        try {
            Product updatedProduct = productService.updateProduct(productRequest, id);
            return ResponseEntity.ok(new ApiResponse("Success", updatedProduct));
        } catch (ResourceClosedException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse("Not found", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Unexcepted error", e.getMessage()));
        }
    }

    @DeleteMapping("/product/delete/{id}")
    public ResponseEntity<ApiResponse> deleteProductById(@PathVariable Long id) {
        try {
            productService.deleteProductByid(id);
            return ResponseEntity.status(NO_CONTENT).body(new ApiResponse("Success", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse("Unexcepted error", e.getMessage()));
        }
    }

    @GetMapping("/product/brand-name")
    public ResponseEntity<ApiResponse> getProductsByBrandAndName(
            @RequestParam String brandName, @RequestParam String productName) {
        try {
            List<Product> products =
                    productService.getProductsByBrandAndName(brandName, productName);

            return ResponseEntity.ok(new ApiResponse("success", products));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/product/category-brand")
    public ResponseEntity<ApiResponse> getProductsByCategoryAndBrand(
            @RequestParam String categoryName, @RequestParam String brand) {
        try {
            List<Product> products =
                    productService.getProductsByCategoryNameAndName(categoryName, brand);

            return ResponseEntity.ok(new ApiResponse("Success", products));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Unexcepted error", null));
        }
    }

    @GetMapping("/product/{name}")
    public ResponseEntity<ApiResponse> getProductsByName(@PathVariable String name) {
        try {
            List<Product> products = productService.getProductsByName(name);
            return ResponseEntity.ok(new ApiResponse("success", products));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Unexcepted error", e.getMessage()));
        }
    }

    @GetMapping("product/by-brand")
    public ResponseEntity<ApiResponse> findProductByBrand(@RequestParam String brand) {
        try {
            List<Product> products = productService.getProductsByBrand(brand);
            return ResponseEntity.ok(new ApiResponse("success", products));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse("Not found", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Unexcepted error", e.getMessage()));
        }
    }

    @GetMapping("/product/by-category")
    public ResponseEntity<ApiResponse> findProductsByCategory(@PathVariable String categoryName) {
        try {
            List<Product> products = productService.getProductsByCategory(categoryName);
            return ResponseEntity.ok(new ApiResponse("success", products));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse("Not found", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Unexcepted error", e.getMessage()));
        }
    }

    public ResponseEntity<ApiResponse> countProductsByBrandAndName(
            @RequestParam String brandName, @RequestParam String productName) {
        try {
            Long productCount = productService.countProductsByBrandAndName(brandName, productName);
            return ResponseEntity.ok(new ApiResponse("Success", productCount));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Unexcepted error", e.getMessage()));
        }
    }
}
