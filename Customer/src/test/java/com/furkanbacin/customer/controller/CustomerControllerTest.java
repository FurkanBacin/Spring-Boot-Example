package com.furkanbacin.customer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.furkanbacin.customer.dto.ReadCustomerDTO;
import com.furkanbacin.customer.dto.WriteCustomerDTO;
import com.furkanbacin.customer.service.CustomerServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerServiceImpl customerService;

    @Test
    void addCustomer() throws Exception {
        ReadCustomerDTO readCustomerDTO = new ReadCustomerDTO(1, "Furkan",1);
        WriteCustomerDTO writeCustomerDTO = new WriteCustomerDTO("Furkan",1);

        when(customerService.addCustomer(writeCustomerDTO)).thenReturn(readCustomerDTO);

        final var resultAction = mockMvc.perform(MockMvcRequestBuilders.post("/customer/add-customer")
                .content(asJsonString(readCustomerDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        resultAction.andExpect(status().isCreated()).andExpect(jsonPath("$.id", is(readCustomerDTO.getId())));
    }

    @Test
    void getCustomers() throws Exception {

        List<ReadCustomerDTO> readCustomerDTOList = new ArrayList<>();
        ReadCustomerDTO readCustomerDTOOne = new ReadCustomerDTO(1, "Furkan",1);
        ReadCustomerDTO readCustomerDTOTwo = new ReadCustomerDTO(2, "Ali",1);
        readCustomerDTOList.add(readCustomerDTOOne);
        readCustomerDTOList.add(readCustomerDTOTwo);

        when(customerService.getCustomers()).thenReturn(readCustomerDTOList);
        RequestBuilder request = MockMvcRequestBuilders.get("/customer/get-customers")
                .contentType(MediaType.APPLICATION_JSON);

        final var resultAction = mockMvc.perform(request);

        resultAction.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(readCustomerDTOList.get(0).getId())))
                .andExpect(jsonPath("$.[1].id", is(readCustomerDTOList.get(1).getId())));
    }

    @Test
    void getCustomerById() throws Exception {
        final var id = 1;
        ReadCustomerDTO readCustomerDTO = new ReadCustomerDTO(1, "Furkan",1);

        when(customerService.getCustomerById(id)).thenReturn(readCustomerDTO);

        final var resultAction = mockMvc.perform(MockMvcRequestBuilders.get("/customer/get-customer-by-id/" + id)
                .accept(MediaType.APPLICATION_JSON));

        resultAction.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Furkan"));
    }

    @Test
    void deleteCustomer() throws Exception {
        final var id = 1;
        boolean status = true;

        when(customerService.deleteCustomerById(id)).thenReturn(status);
        final var resultAction = mockMvc.perform(MockMvcRequestBuilders.delete("/customer/delete-customer-by-id/" + id));
        resultAction.andExpect(status().isAccepted());
    }

    @Test
    void updateCustomerById() throws Exception {
        final var id = 1;
        ReadCustomerDTO readCustomerDTO = new ReadCustomerDTO(1, "Furkan",1);
        WriteCustomerDTO writeCustomerDTO = new WriteCustomerDTO("Ali",1);

        when(customerService.updateCustomer(id, writeCustomerDTO)).thenReturn(readCustomerDTO);

        final var expectedDTO = customerService.updateCustomer(id, writeCustomerDTO);

        final var resultAction = mockMvc.perform(MockMvcRequestBuilders.put("/customer/update-customer/" + id)
                .content(asJsonString(expectedDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        resultAction.andExpect(status().isOk());
    }


    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
