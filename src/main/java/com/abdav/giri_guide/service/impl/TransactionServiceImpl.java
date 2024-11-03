package com.abdav.giri_guide.service.impl;

import com.abdav.giri_guide.constant.ETransactionStatus;
import com.abdav.giri_guide.entity.*;
import com.abdav.giri_guide.mapper.TransactionMapper;
import com.abdav.giri_guide.model.request.HikerDetailRequest;
import com.abdav.giri_guide.model.request.TransactionRequest;
import com.abdav.giri_guide.model.response.TransactionResponse;
import com.abdav.giri_guide.model.response.TransactionStatusResponse;
import com.abdav.giri_guide.repository.*;
import com.abdav.giri_guide.service.TransactionService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final MountainsRepository mountainsRepository;
    private final CustomerRepository customerRepository;
    private final HikingPointRepository hikingPointRepository;
    private final TransactionHikerRepository transactionHikerRepository;
    private final TourGuideRepository tourGuideRepository;
    @Value("${app.giri-guide.admin-cost}")
    private Double adminCost;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TransactionStatusResponse createTransaction(TransactionRequest transactionRequest) {
        Customer customer = customerRepository.findById(transactionRequest.customerId()).orElseThrow(EntityNotFoundException::new);
        HikingPoint hikingPointReq = hikingPointRepository.findByIdAndDeletedDateIsNull(transactionRequest.hikingPointId()).orElseThrow(EntityNotFoundException::new);
        Mountains mountain = mountainsRepository.findById(hikingPointReq.getMountain().getId()).orElseThrow(EntityNotFoundException::new);
        TourGuide tourGuide = tourGuideRepository.findById(transactionRequest.guideId()).orElseThrow(() -> new EntityNotFoundException("Tour Guide not found"));

        Transaction transaction = Transaction.builder()
                .customer(customer)
                .tourGuide(tourGuide)
                .hikingPoint(hikingPointReq)
                .startDate(transactionRequest.startDate())
                .endDate(transactionRequest.endDate())
                .status(ETransactionStatus.WAITING_APPROVE)
                .porterQty(transactionRequest.porterQty())
                .adminCost(adminCost)
                .build();
        transactionRepository.saveAndFlush(transaction);

        List<TransactionHiker> hikers = new ArrayList<>();
        for (HikerDetailRequest hiker : transactionRequest.hikerDetails()) {
            TransactionHiker transactionHiker = TransactionHiker.builder()
                    .fullName(hiker.fullName())
                    .nik(hiker.nik())
                    .birthDate(hiker.birthDate())
                    .transaction(transaction)
                    .build();
            hikers.add(transactionHiker);
        }
        transactionHikerRepository.saveAllAndFlush(hikers);
        transaction.setTransactionHikers(hikers);
        Long days = ChronoUnit.DAYS.between(transaction.getStartDate(), transaction.getEndDate());

        Double totalTourguidePrice = tourGuide.getPrice() * days;
        Double totalPorterPrice = calculatePorterPrice(tourGuide.getPricePorter(), transactionRequest.porterQty(), days);
        Double totalAdditionalPrice = calculateAdditionalPrice(tourGuide, hikers.size(), days);
        Double totalSimaksiPrice = calculateSimaksiPrice(mountain, hikers.size());
        Double totalEntryPrice = hikingPointReq.getPrice() * hikers.size() * days;
        Double totalPrice = totalPorterPrice + totalTourguidePrice + totalAdditionalPrice + totalEntryPrice + totalSimaksiPrice + adminCost;

        System.out.println("Total price porter = " + totalPorterPrice);
        System.out.println("Price tourGuide = " + totalTourguidePrice);
        System.out.println("Total additionalPrice = " + totalAdditionalPrice);
        System.out.println("TotalSimaksiPrice = " + totalSimaksiPrice);
        System.out.println("Total entryPrice = " + totalEntryPrice);
        System.out.println("Total Price = " + totalPrice);


        transaction.setTotalPorterPrice(totalPorterPrice);
        transaction.setTotalTourGuidePrice(totalTourguidePrice);
        transaction.setAdditionalPriceTourGuide(totalAdditionalPrice);
        transaction.setTotalSimaksiPrice(totalSimaksiPrice);
        transaction.setTotalEntryPrice(totalEntryPrice);
        transaction.setTotalPrice(totalPrice);

        transactionRepository.saveAndFlush(transaction);

        return new TransactionStatusResponse(transaction.getStatus().toString());
    }

    private Double calculatePorterPrice(Double porterRate, Integer porterQty, Long days){
        return porterRate * porterQty * days;
    }

    private Double calculateSimaksiPrice(Mountains mountains, int hikerQty){
        return mountains.isUseSimaksi() ? mountains.getPriceSimaksi() * hikerQty : 0.0;
    }

    private Double calculateAdditionalPrice(TourGuide tourGuide, int hikerQty, Long days){
        if(hikerQty > tourGuide.getMaxHiker()){
            int additionalHiker = hikerQty - tourGuide.getMaxHiker();
            return tourGuide.getAdditionalPrice() * additionalHiker * days;
        }
        return 0.0;
    }
}
