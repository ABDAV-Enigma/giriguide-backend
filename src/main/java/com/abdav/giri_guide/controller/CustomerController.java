package com.abdav.giri_guide.controller;

import com.abdav.giri_guide.constant.Message;
import com.abdav.giri_guide.constant.PathApi;
import com.abdav.giri_guide.model.response.CommonResponse;
import com.abdav.giri_guide.model.response.CommonResponseWithPage;
import com.abdav.giri_guide.model.response.CustomerResponse;
import com.abdav.giri_guide.model.response.PagingResponse;
import com.abdav.giri_guide.service.CustomerService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(PathApi.CUSTOMER_API)
@SecurityRequirement(name = "bearerAuth")
public class CustomerController {
    private final CustomerService customerService;
    private static String message;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping
    public ResponseEntity<CommonResponseWithPage<?>> getCustomerList(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "5") Integer size,
            HttpServletRequest httpReq
    ){
        Page<CustomerResponse> customerList = customerService.customerList(page, size, httpReq);
        PagingResponse paging = new PagingResponse(page, size, customerList.getTotalPages(), customerList.getTotalElements());
        message = Message.SUCCESS_FETCH;

        CommonResponseWithPage<?> response = new CommonResponseWithPage<>(message, customerList.getContent(), paging);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{customerId}")
    public ResponseEntity<?> deleteCustomer(@PathVariable String customerId) {
        customerService.deleteCustomerById(customerId);
        message = "Customer with id " + customerId + " " +Message.SUCCESS_DELETE;
        CommonResponse<?> response = new CommonResponse<>(message, null);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
