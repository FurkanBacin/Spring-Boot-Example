package com.furkanbacin.customer.service;

import com.furkanbacin.customer.dto.ReadProductForCustomerDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestTemplateService {

    private final RestTemplate restTemplate;

    public RestTemplateService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ReadProductForCustomerDTO getProduct(int id){

        ReadProductForCustomerDTO readProductForCustomerDTO =restTemplate
                .getForObject("http://localhost:9001/product/get-product-by-id/" +id
                        , ReadProductForCustomerDTO.class);
        return  readProductForCustomerDTO;
    }

}
