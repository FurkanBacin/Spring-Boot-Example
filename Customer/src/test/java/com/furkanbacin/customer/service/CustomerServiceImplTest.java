package com.furkanbacin.customer.service;

import com.furkanbacin.customer.dto.ReadCustomerDTO;
import com.furkanbacin.customer.dto.WriteCustomerDTO;
import com.furkanbacin.customer.model.Customer;
import com.furkanbacin.customer.repository.CustomerRepository;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceImplTest {

    @InjectMocks
    CustomerServiceImpl customerService;

    ModelMapper modelMapper;

    @Mock
    CustomerRepository customerRepository;

    @BeforeEach
    void init() {
        this.modelMapper = new ModelMapper();
    }

    @Test
    public void test_add_customer() {

        //GIVEN
        WriteCustomerDTO writeCustomerDTO = new WriteCustomerDTO("Ali",1);
        final var expectedCustomer = modelMapper.map(writeCustomerDTO, Customer.class);
        final var mockCustomer = new Customer(1, "Ali",1);
        ReadCustomerDTO expectedCustomerReadDTO = modelMapper.map(mockCustomer, ReadCustomerDTO.class);
        when(customerRepository.save(any(Customer.class))).thenReturn(mockCustomer);

        //WHEN
        ReadCustomerDTO customerDTO = customerService.addCustomer(writeCustomerDTO);

        //THEN
        assertEquals(expectedCustomerReadDTO, customerDTO);
        final var addAC = ArgumentCaptor.forClass(Customer.class);
        verify(customerRepository).save(addAC.capture());
        assertThat(addAC.getValue()).isEqualTo(expectedCustomer);
    }

    @Test
    public void test_get_customer_by_id() {
        //GIVEN
        final var id = 1;
        Customer mockCustomer = new Customer(1, "Ali",1);
        when(customerRepository.getReferenceById(id)).thenReturn(mockCustomer);
        final var expectedCustomerReadDTO = modelMapper.map(mockCustomer, ReadCustomerDTO.class);

        //WHEN
        final var customerReadDTO = customerService.getCustomerById(id);

        //THEN
        assertThat(customerReadDTO).isEqualTo(expectedCustomerReadDTO);
        verify(customerRepository).getReferenceById(id);
    }

    @Test
    public void test_get_customers() {

        //GIVEN
        Customer mockCustomerOne = new Customer(1, "ALi",1);
        Customer mockCustomerTwo = new Customer(2, "Furkan",1);

        final var expectedCustomerReadOne = modelMapper.map(mockCustomerOne, ReadCustomerDTO.class);
        final var expectedCustomerReadTwo = modelMapper.map(mockCustomerTwo, ReadCustomerDTO.class);
        final var expectedCustomerReadDTOS = Arrays.asList(expectedCustomerReadOne, expectedCustomerReadTwo);

        when(customerRepository.findAll()).thenReturn(Arrays.asList(mockCustomerOne, mockCustomerTwo));

        //WHEN
        final var customers = customerService.getCustomers();

        //THEN
        assertThat(customers.get(0)).isEqualTo(expectedCustomerReadOne);
        assertThat(customers.get(1)).isEqualTo(expectedCustomerReadTwo);

        IntStream.range(0, expectedCustomerReadDTOS.size()).forEach(index -> {
            assertThat(expectedCustomerReadDTOS.get(index)).isEqualTo(customers.get(index));
        });
        verify(customerRepository).findAll();
    }

    @Test
    public void test_delete_customer_if_customer_exists() {

        //GIVEN
        final var id = 1;
        Customer mockCustomer = new Customer(1, "Ali",1);
        Optional<Customer> customerOptional = Optional.of(mockCustomer);
        when(customerRepository.findById(id)).thenReturn(customerOptional);

        //WHEN
        final var expectedStatus = customerService.deleteCustomerById(id);

        //THEN
        assertTrue(expectedStatus);
        verify(customerRepository, times(1)).deleteById(id);
    }

    /*
    @Test
    public void test_delete_customer_by_id_if_customer_not_exists() {

        //GIVEN
        Customer mockCustomer = new Customer(1, "Ali");
        final var id = 2;
        ApiException apiException = null;

        //WHEN
        final var expectedStatus = customerService.deleteCustomerById(id);
        String expectedMessage = "Musteri bulunamadi";
        String actualMessage = apiException.getMessage();

        //THEN
        assertTrue(actualMessage.contains(expectedMessage));
        verify(customerRepository, times(0)).deleteById(id);
    }
    */

    @Test
    public void test_update_customer_if_is_present_customer() {

        //GIVEN
        final var id = 1;
        Customer mockCustomer = new Customer(1, "Ali",1);
        Optional<Customer> customerOptional = Optional.of(mockCustomer);

        WriteCustomerDTO writeCustomerDTO = new WriteCustomerDTO("Ali",1);
        customerOptional.get().setName(writeCustomerDTO.getName());

        ReadCustomerDTO expectedReadCustomerDTO = modelMapper.map(customerOptional, ReadCustomerDTO.class);

        when(customerRepository.findById(id)).thenReturn(customerOptional);
        when(customerRepository.save(any(Customer.class))).thenReturn(mockCustomer);

        //WHEN
        ReadCustomerDTO readCustomerDTO = customerService.updateCustomer(id, writeCustomerDTO);
        final var expectedCustomer = new Customer(1, "Ali",1);

        //THEN
        final var updateAc = ArgumentCaptor.forClass(Customer.class);
        verify(customerRepository).save(updateAc.capture());

        assertThat(updateAc.getValue()).isEqualTo(expectedCustomer);

        verify(customerRepository, times(1)).findById(id);
    }
}
