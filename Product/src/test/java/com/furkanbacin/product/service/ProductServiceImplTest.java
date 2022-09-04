package com.furkanbacin.product.service;

import com.furkanbacin.product.dto.ReadProductDTO;
import com.furkanbacin.product.dto.ReadProductForCustomerDTO;
import com.furkanbacin.product.dto.WriteProductDTO;
import com.furkanbacin.product.model.Product;
import com.furkanbacin.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @InjectMocks
    ProductServiceImpl productService;

    ModelMapper modelMapper;

    @Mock
    ProductRepository productRepository;

    @BeforeEach
    void init() {
        this.modelMapper = new ModelMapper();
    }

    @Test
    public void test_get_product_by_id() {
        //GIVEN
        final var id = 1;
        Product mockProduct = new Product(1, "elma");
        when(productRepository.getById(id))
                .thenReturn(mockProduct);
        final var expectedProductReadDTO = modelMapper.map(mockProduct, ReadProductForCustomerDTO.class);

        //WHEN
        final var productReadDTO = productService.getProductById(id);

        //THEN
        assertThat(productReadDTO)
                .isEqualTo(expectedProductReadDTO);

        verify(productRepository).getById(id);
    }

    @Test
    public void test_save_product() {
        //GIVEN
        WriteProductDTO writeProductDto = new WriteProductDTO("elma");
        final var expectedProduct = modelMapper.map(writeProductDto, Product.class);
        final var mockProduct = new Product(1, "elma");
        ReadProductDTO expectedProductReadDTO = modelMapper.map(mockProduct, ReadProductDTO.class);

        when(productRepository.save(any(Product.class)))
                .thenReturn(mockProduct);

        //WHEN
        ReadProductDTO productDto = productService.saveProduct(writeProductDto);

        //THEN
        assertEquals(expectedProductReadDTO, productDto);

        final var saveAC = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(saveAC.capture());
        assertThat(saveAC.getValue())
                .isEqualTo(expectedProduct);
    }

    @Test
    public void test_get_all_products() {

        //GIVEN
        Product mockProduct1 = new Product(1, "elma");
        Product mockProduct2 = new Product(2, "armut");

        final var expectedProductRead1 = modelMapper.map(mockProduct1, ReadProductDTO.class);
        final var expectedProductRead2 = modelMapper.map(mockProduct2, ReadProductDTO.class);
        final var expectedProductReadDTOS = Arrays.asList(expectedProductRead1, expectedProductRead2);

        when(productRepository.findAll())
                .thenReturn(Arrays.asList(mockProduct1, mockProduct2));

        //WHEN
        final var products = productService.getAllProducts();

        //THEN
        assertThat(products.get(0)).isEqualTo(expectedProductRead1);
        assertThat(products.get(1)).isEqualTo(expectedProductRead2);

        IntStream.range(0, expectedProductReadDTOS.size()).forEach(index -> {
            assertThat(expectedProductReadDTOS.get(index)).isEqualTo(products.get(index));
        });

        verify(productRepository).findAll();
    }

    @Test
    public void test_delete_product_if_product_exists() {

        //GIVEN
        Product mockProduct = new Product(1, "elma");
        Optional<Product> productOptional = Optional.of(mockProduct);
        final var id = 1;
        when(productRepository.findById(id)).thenReturn(productOptional);

        //WHEN
        final var expectedStatus = productService.deleteProductById(id);

        //THEN
        assertTrue(expectedStatus);
        verify(productRepository, times(1)).deleteById(id);
    }

    @Test
    public void test_delete_product_by_id_if_product_not_exists() {

        //GIVEN
        Product mockProduct = new Product(1, "elma");
        final var id = 2;

        //WHEN
        final var expectedStatus = productService.deleteProductById(id);

        //THEN
        assertFalse(expectedStatus);
        verify(productRepository,times(0)).deleteById(id);
    }

    @Test
    public void test_update_product_if_is_present_product() {

        //GIVEN
        final var id = 1;
        Product mockProduct = new Product(1, "elma");
        Optional<Product> productOptional = Optional.of(mockProduct);

        WriteProductDTO writeProductDto = new WriteProductDTO("armut");
        productOptional.get().setName(writeProductDto.getName());

        ReadProductDTO expectedReadProductDTO = modelMapper.map(productOptional.get(), ReadProductDTO.class);

        when(productRepository.findById(id)).thenReturn(productOptional);
        when(productRepository.save(any(Product.class))).thenReturn(mockProduct);


        //WHEN
        ReadProductDTO readProductDto = productService.updateProduct(id,writeProductDto);
        final var expectedProduct = new Product(1,"armut");

        //THEN
        assertEquals(expectedReadProductDTO,readProductDto);

        final var updateAC = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(updateAC.capture());

        assertThat(updateAC.getValue())
                .isEqualTo(expectedProduct);
        verify(productRepository,times(1))
                .findById(id);
    }

}