package com.furkanbacin.customer.controller;

import com.furkanbacin.customer.dto.ReadCustomerDTO;
import com.furkanbacin.customer.dto.ReadProductForCustomerDTO;
import com.furkanbacin.customer.dto.WriteCustomerDTO;
import com.furkanbacin.customer.model.Customer;
import com.furkanbacin.customer.model.CustomerPage;
import com.furkanbacin.customer.model.CustomerSearchCriteria;
import com.furkanbacin.customer.service.CustomerServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerServiceImpl customerService;

    public CustomerController(CustomerServiceImpl customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/add-customer")
    @Operation(summary = "it will saving new customer")
    public ResponseEntity<ReadCustomerDTO> addCustomer(@RequestBody WriteCustomerDTO writeCustomerDTO) {
        log.info("addCustomer endpoint ran with {}.", writeCustomerDTO.getName());
        ReadCustomerDTO resultCustomer = customerService.addCustomer(writeCustomerDTO);
        return new ResponseEntity<>(resultCustomer, HttpStatus.CREATED);
    }

    @GetMapping("/get-customers")
    @Operation(summary = "it will fetch all customer")
    public ResponseEntity<List<ReadCustomerDTO>> getCustomers() {
        log.info("getCustomers endpoint ran.");
        List<ReadCustomerDTO> customers = customerService.getCustomers();
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    @GetMapping("/get-customer-by-filtering")
    @Operation(summary = "it will fetch customer by filtering")
    public ResponseEntity<Page<Customer>> getCustomersByFiltering(CustomerPage customerPage,
                                                                  CustomerSearchCriteria customerSearchCriteria) {
        log.info("getCustomersByFiltering endpoint ran.");
        return new ResponseEntity<>(customerService.getCustomersByFiltering(customerPage, customerSearchCriteria),
                HttpStatus.OK);
    }

    @GetMapping("/get-customer-by-id/{id}")
    @Operation(summary = "it will fetch customer")
    public ResponseEntity<ReadCustomerDTO> getCustomerById(@PathVariable int id) {
        log.info("getCustomerById endpoint ran with id {}.", id);
        ReadCustomerDTO readCustomerDTO = customerService.getCustomerById(id);
        return new ResponseEntity<>(readCustomerDTO, HttpStatus.OK);
    }

    @GetMapping("/get-product-customer-by-id/{id}")
    @Operation(summary = "it will fetch product...")
    public ResponseEntity<ReadProductForCustomerDTO> getProductCustomer(@PathVariable int id) {
        log.info("getProductCustomer endpoint ran with {}.", id);
        ReadProductForCustomerDTO readProductForCustomerDTO = customerService.getProduct(id);
        return new ResponseEntity<>(readProductForCustomerDTO, HttpStatus.OK);
    }

    @DeleteMapping("/delete-customer-by-id/{id}")
    @Operation(summary = "it will delete customer")
    public ResponseEntity<Boolean> deleteCustomer(@PathVariable int id) {
        log.info("deleteCustomer endpoint ran.");
        boolean status = customerService.deleteCustomerById(id);
        return new ResponseEntity<>(status, HttpStatus.ACCEPTED);
    }

    @PutMapping("/update-customer/{id}")
    @Operation(summary = "it will update customer")
    public ResponseEntity<ReadCustomerDTO> updateCustomerById(@PathVariable int id,
                                                              @RequestBody WriteCustomerDTO writeCustomerDTO) {
        log.info("updateCustomerById endpoint ran with {}.", writeCustomerDTO.getName());
        ReadCustomerDTO resultCustomer = customerService.updateCustomer(id, writeCustomerDTO);
        return new ResponseEntity<>(resultCustomer, HttpStatus.OK);
    }
}
