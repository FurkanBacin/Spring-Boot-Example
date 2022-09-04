package com.furkanbacin.product.service;

import com.furkanbacin.product.dto.ReadProductDTO;
import com.furkanbacin.product.dto.ReadProductForCustomerDTO;
import com.furkanbacin.product.dto.WriteProductDTO;
import com.furkanbacin.product.model.Product;
import com.furkanbacin.product.model.ProductPage;
import com.furkanbacin.product.model.ProductSearchCriteria;
import com.furkanbacin.product.repository.ProductCriteriaRepository;
import com.furkanbacin.product.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductCriteriaRepository productCriteriaRepository;
    private final ModelMapper modelMapper = new ModelMapper();


    public ProductServiceImpl(ProductRepository productRepository, ProductCriteriaRepository productCriteriaRepository) {
        this.productRepository = productRepository;
        this.productCriteriaRepository = productCriteriaRepository;
    }

    @Override
    public Page<Product> getProductsByFiltering(ProductPage productPage, ProductSearchCriteria productSearchCriteria) {
        log.info("getProductsByFiltering method ran.");
        return productCriteriaRepository.findAllWithFilters(productPage, productSearchCriteria);
    }

    @Override
    @Transactional
    public ReadProductDTO saveProduct(WriteProductDTO writeProductDto) {
        log.info("saveProduct method ran with {}", writeProductDto.getName());
        Product product = modelMapper.map(writeProductDto, Product.class);
        return modelMapper.map(productRepository.save(product), ReadProductDTO.class);
    }

    @Override
    public List<ReadProductDTO> getAllProducts() {
        log.info("getAllProducts method ran.");
        List<Product> products = productRepository.findAll();
        List<ReadProductDTO> readProductDTOList = products.stream().map(product -> modelMapper.map(product, ReadProductDTO.class))
                .collect(Collectors.toList());
        return readProductDTOList;
    }

    @Override
    public ReadProductForCustomerDTO getProductById(int id) {
        log.info("getProductById method ran with {}", id);
        Product product = productRepository.getById(id);
        return modelMapper.map(product, ReadProductForCustomerDTO.class);
    }

    @Override
    @Transactional
    public boolean deleteProductById(int id) {
        log.info("deleteProductById method ran with {}", id);
        Optional<Product> product = productRepository.findById(id);

        if (product.isPresent()) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public ReadProductDTO updateProduct(int id, WriteProductDTO writeProductDto) {
        log.info("updateProduct method ran with {}", writeProductDto.getName());
        Optional<Product> resultProduct = productRepository.findById(id);
        if (resultProduct.isPresent()) {
            resultProduct.get().setName(writeProductDto.getName());
            return modelMapper.map(productRepository.save(resultProduct.get()), ReadProductDTO.class);
        }
        return null;
    }
}
