package com.furkanbacin.product.service;

import com.furkanbacin.product.dto.ReadProductDTO;
import com.furkanbacin.product.dto.ReadProductForCustomerDTO;
import com.furkanbacin.product.dto.WriteProductDTO;
import com.furkanbacin.product.model.Product;
import com.furkanbacin.product.model.ProductPage;
import com.furkanbacin.product.model.ProductSearchCriteria;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {

    ReadProductDTO saveProduct(WriteProductDTO writeProductDto);

    List<ReadProductDTO> getAllProducts();

    ReadProductForCustomerDTO getProductById(int id);

    boolean deleteProductById(int id);

    ReadProductDTO updateProduct(int id, WriteProductDTO writeProductDto);

    Page<Product> getProductsByFiltering(ProductPage productPage, ProductSearchCriteria productSearchCriteria);
}
