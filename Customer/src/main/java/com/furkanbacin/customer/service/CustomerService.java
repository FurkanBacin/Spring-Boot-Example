package com.furkanbacin.customer.service;

import com.furkanbacin.customer.dto.ReadCustomerDTO;
import com.furkanbacin.customer.dto.ReadProductForCustomerDTO;
import com.furkanbacin.customer.dto.WriteCustomerDTO;
import com.furkanbacin.customer.model.Customer;
import com.furkanbacin.customer.model.CustomerPage;
import com.furkanbacin.customer.model.CustomerSearchCriteria;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CustomerService {

    ReadCustomerDTO addCustomer(WriteCustomerDTO writeCustomerDTO);

    List<ReadCustomerDTO> getCustomers();

    ReadCustomerDTO getCustomerById(int id);

    boolean deleteCustomerById(int id);

    ReadCustomerDTO updateCustomer(int id, WriteCustomerDTO writeCustomerDTO);

    ReadProductForCustomerDTO getProduct(int id);

    Page<Customer> getCustomersByFiltering(CustomerPage customerPage, CustomerSearchCriteria customerSearchCriteria);


}
