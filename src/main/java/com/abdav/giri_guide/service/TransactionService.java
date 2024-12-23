package com.abdav.giri_guide.service;

import com.abdav.giri_guide.entity.Transaction;
import com.abdav.giri_guide.model.request.TransactionRequest;
import com.abdav.giri_guide.model.response.CountTransactionResponse;
import com.abdav.giri_guide.model.response.TransactionDetailResponse;
import com.abdav.giri_guide.model.response.TransactionResponse;
import com.abdav.giri_guide.model.response.TransactionStatusResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TransactionService {
    TransactionStatusResponse createTransaction(TransactionRequest transactionRequest);
    TransactionStatusResponse updateTransactionStatus(String id, String status, String rejectedError);
    Page<TransactionDetailResponse> transactionList(Integer page, Integer size,String status, HttpServletRequest httpReq);
    TransactionDetailResponse getTransactionById(String id, HttpServletRequest httpReq);
    Page<TransactionResponse> findAllByStatus(List<String> stringList,String userId, Integer page, Integer size, String role, HttpServletRequest httpReq);
    CountTransactionResponse countAllStatusTransaction(Integer month, Integer year);

    Transaction getById(String id);
    void updateStatusFromPayment(Transaction transaction, String status);
    Long getTotalPrice(Transaction transaction);
}

