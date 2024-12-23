package com.abdav.giri_guide.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.abdav.giri_guide.constant.Message;
import com.abdav.giri_guide.constant.PathApi;
import com.abdav.giri_guide.model.request.LocationRouteRequest;
import com.abdav.giri_guide.model.request.LocationRouteUpdateRequest;
import com.abdav.giri_guide.model.response.CommonResponse;
import com.abdav.giri_guide.service.LocationRouteService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(PathApi.LOCATION_ROUTE_API)
@RequiredArgsConstructor
public class LocationRouteController {
    private final LocationRouteService locationRouteService;

    @PreAuthorize("hasAnyRole('CUSTOMER', 'GUIDE', 'ADMIN')")
    @GetMapping("")
    public ResponseEntity<?> getAllLocationRoute(
            @RequestParam(required = false, defaultValue = "") String title,
            @RequestParam(required = false, defaultValue = "5") Integer size,
            @RequestParam(required = false, defaultValue = "1") Integer page

    ) {

        return ResponseEntity.status(HttpStatus.OK).body(locationRouteService.getAllRoute(title, page, size));
    }

    @PreAuthorize("hasAnyRole('CUSTOMER', 'GUIDE', 'ADMIN')")
    @GetMapping("{locationRouteId}")
    public ResponseEntity<?> getLocationRouteDetail(@PathVariable String locationRouteId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(
                        Message.SUCCESS_FETCH, locationRouteService.getRouteDetail(locationRouteId)));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("")
    public ResponseEntity<?> createLocationRoute(
            @RequestBody @Validated LocationRouteRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommonResponse<>(Message.DATA_CREATED, locationRouteService.createRoute(request)));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PatchMapping("{locationRouteId}")
    public ResponseEntity<?> updateLocationRoute(
            @PathVariable String locationRouteId,
            @RequestBody @Validated LocationRouteUpdateRequest request

    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(
                        locationRouteId,
                        locationRouteService.updateRoute(locationRouteId, request)));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("{locationRouteId}")
    public ResponseEntity<?> deleteRoute(@PathVariable String locationRouteId) {
        locationRouteService.deleteRoute(locationRouteId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(
                        Message.SUCCESS_DELETE, null));
    }

}
