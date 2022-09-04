package com.furkanbacin.product.controller;

import com.furkanbacin.product.dto.ReadProductDTO;
import com.furkanbacin.product.dto.ReadProductForCustomerDTO;
import com.furkanbacin.product.dto.WriteProductDTO;
import com.furkanbacin.product.model.Product;
import com.furkanbacin.product.model.ProductPage;
import com.furkanbacin.product.model.ProductSearchCriteria;
import com.furkanbacin.product.service.ProductServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductServiceImpl productService;

    public ProductController(ProductServiceImpl productService) {
        this.productService = productService;
    }

    @PostMapping("/save-product")
    @Operation(summary = " it is saving new product")
    public ResponseEntity<ReadProductDTO> saveProduct(@RequestBody WriteProductDTO writeProductDto) {
        log.info("saveProduct endpoint ran with {}", writeProductDto.getName());
        ReadProductDTO resultProduct = productService.saveProduct(writeProductDto);
        return new ResponseEntity<ReadProductDTO>(resultProduct, HttpStatus.CREATED);
    }

    @GetMapping("/get-all-products")
    @Operation(summary = " it will fetch all products")
    public ResponseEntity<List<ReadProductDTO>> getAllProducts() {
        log.info("getAllProducts endpoint ran");
        List<ReadProductDTO> products = productService.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/get-products-by-filtering")
    @Operation(summary = "it will fetch products by filtering")
    public ResponseEntity<Page<Product>> getProductsByFiltering(ProductPage productPage,
                                                                ProductSearchCriteria productSearchCriteria){
        log.info("getProductsByFiltering endpoint ran");
        return new ResponseEntity<>(productService.getProductsByFiltering(productPage, productSearchCriteria),
                HttpStatus.OK);
    }

    @GetMapping("/get-product-by-id/{id}")
    @Operation(summary = " it will fetch product")
    public ResponseEntity<ReadProductForCustomerDTO> getProductById(@PathVariable int id) {
        log.info("getProductById endpoint ran with {}", id);
        ReadProductForCustomerDTO readProductForCartDto = productService.getProductById(id);
        return new ResponseEntity<>(readProductForCartDto, HttpStatus.OK);
    }

    @DeleteMapping("/delete-product/{id}")
    @Operation(summary = " it will delete product with id")
    public ResponseEntity<Boolean> deleteProduct(@PathVariable int id) {
        log.info("deleteProduct endpoint ran{}", id);
        Boolean status = productService.deleteProductById(id);
        return new ResponseEntity<>(status, HttpStatus.ACCEPTED);
    }

    @PutMapping("/update-product/{id}")
    @Operation(summary = " it will update product")
    public ResponseEntity<ReadProductDTO> updateProductById(@PathVariable int id, @RequestBody WriteProductDTO writeProductDto) {
        log.info("updateProductById endpoint ran with {}", writeProductDto.getName());
        ReadProductDTO resultProduct = productService.updateProduct(id, writeProductDto);
        return new ResponseEntity<>(resultProduct, HttpStatus.OK);
    }
}