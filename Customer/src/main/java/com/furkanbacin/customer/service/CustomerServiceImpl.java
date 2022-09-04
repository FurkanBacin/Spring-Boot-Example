package com.furkanbacin.customer.service;

import com.furkanbacin.customer.dto.ReadCustomerDTO;
import com.furkanbacin.customer.dto.ReadProductForCustomerDTO;
import com.furkanbacin.customer.dto.WriteCustomerDTO;
import com.furkanbacin.customer.exception.ApiRequestException;
import com.furkanbacin.customer.model.Customer;
import com.furkanbacin.customer.model.CustomerPage;
import com.furkanbacin.customer.model.CustomerSearchCriteria;
import com.furkanbacin.customer.repository.CustomerCriteriaRepository;
import com.furkanbacin.customer.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerCriteriaRepository customerCriteriaRepository;
    private final ModelMapper modelmapper = new ModelMapper();
    private final RestTemplateService restTemplateService;

    public CustomerServiceImpl(CustomerRepository customerRepository,
                               CustomerCriteriaRepository customerCriteriaRepository,
                               RestTemplateService restTemplateService) {
        this.customerRepository = customerRepository;
        this.customerCriteriaRepository = customerCriteriaRepository;
        this.restTemplateService = restTemplateService;
    }

    @Override
    @Transactional
    public ReadCustomerDTO addCustomer(WriteCustomerDTO writeCustomerDTO) {
        log.debug("Saving new customer to the database");
        Customer customer = modelmapper.map(writeCustomerDTO, Customer.class);
        return modelmapper.map(customerRepository.save(customer), ReadCustomerDTO.class);
    }

    @Override
    public ReadCustomerDTO getCustomerById(int id) {
        log.debug("Fetching customer");
        Customer customer = customerRepository.getReferenceById(id);
        return modelmapper.map(customer, ReadCustomerDTO.class);
    }

    @Override
    public List<ReadCustomerDTO> getCustomers() {
        log.debug("Fetching all customers");
        List<Customer> customers = customerRepository.findAll();
        List<ReadCustomerDTO> readCustomerDTOList = customers.stream().map(customer -> modelmapper.map(customer, ReadCustomerDTO.class))
                .collect(Collectors.toList());
        return readCustomerDTOList;
    }

    @Override
    @Transactional
    public boolean deleteCustomerById(int id) {
        log.info("Deleting customer {}", id);
        Optional<Customer> customer = customerRepository.findById(id);
        if (customer.isPresent()) {
            customerRepository.deleteById(id);
            return true;
        }
        throw new ApiRequestException("Musteri bulunamadi");
    }

    @Override
    @Transactional
    public ReadCustomerDTO updateCustomer(int id, WriteCustomerDTO writeCustomerDTO) {
        log.info("Updating customer {} ", writeCustomerDTO.getName());
        Optional<Customer> resultCustomer = customerRepository.findById(id);
        if (resultCustomer.isPresent()) {
            resultCustomer.get().setName(writeCustomerDTO.getName());
            return modelmapper.map(customerRepository.save(resultCustomer.get()), ReadCustomerDTO.class);
        }
        throw new ApiRequestException("Musteri bulunamadi");
    }

    @Override
    public ReadProductForCustomerDTO getProduct(int id) {
        Customer customer = customerRepository.getReferenceById(id);
        ReadProductForCustomerDTO readProductForCustomerDTO = restTemplateService.getProduct(customer.getProductId());
        return readProductForCustomerDTO;
    }

    @Override
    public Page<Customer> getCustomersByFiltering(CustomerPage customerPage, CustomerSearchCriteria customerSearchCriteria) {
        return customerCriteriaRepository.findAllWithFilters(customerPage, customerSearchCriteria);
    }
}
