package com.abdav.giri_guide.service;

import com.abdav.giri_guide.entity.Customer;
import com.abdav.giri_guide.model.request.CustomerRequest;
import com.abdav.giri_guide.model.response.CustomerResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface CustomerService {
    void createCustomer(Customer customer);
    Page<CustomerResponse> customerList(Integer page, Integer size);
    CustomerResponse getCustomerById(String id);
    void deleteCustomerById(String id);
    CustomerResponse getCustomerByUserId(String userId);
    CustomerResponse updateCustomer(String id, CustomerRequest customerRequest);
    CustomerResponse uploadProfileImage(String id, MultipartFile file);
}