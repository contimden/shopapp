package com.project.shopapp.controllers;

import com.project.shopapp.dtos.*;
import com.project.shopapp.exceptions.DataNotFaundException;
import com.project.shopapp.models.OrderDetail;
import com.project.shopapp.responses.OrderDetailResponse;
import com.project.shopapp.services.OrderDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/order_details")
@RequiredArgsConstructor
public class OrderdetailController {

    private final OrderDetailService orderDetailService;
    @PostMapping("")
    public ResponseEntity<?> createOrderDetail(
            @Valid @RequestBody OrderDetailDTO orderDetailDTO) {
        try {
            OrderDetail newOrderDetail =  orderDetailService.createOrderDetail(orderDetailDTO);
            return ResponseEntity.ok().body(OrderDetailResponse.fromOrderDetail(newOrderDetail));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetail(
            @Valid @PathVariable Long id) throws Exception {
        OrderDetail orderDetail = orderDetailService.getOrderDetail(id);
        return ResponseEntity.ok().body(OrderDetailResponse.fromOrderDetail(orderDetail));
        //return ResponseEntity.ok(orderDetail);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getOrderDetails(
            @Valid @PathVariable("orderId") Long orderId) {
        List<OrderDetail> orderDetails = orderDetailService.findByOrderId(orderId);
        List<OrderDetailResponse> orderDetailResponses = orderDetails
                .stream()
                .map(OrderDetailResponse::fromOrderDetail)
                .toList();
        return ResponseEntity.ok(orderDetailResponses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderDetail(
            @Valid @PathVariable Long id,
            @Valid @RequestBody OrderDetailDTO orderDetailData) {
        try {
            OrderDetail orderDetail = orderDetailService.updateOrderDetail(id, orderDetailData);
            return ResponseEntity.ok().body(orderDetail);
        } catch (DataNotFaundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> DeleteOrderDetail(
            @Valid @PathVariable("id") Long id) {
        orderDetailService.deleteById(id);
        return ResponseEntity.ok().body("Delete Order Detail with id: "+id+" successfully");
        //return ResponseEntity.noContent().build();
    }

}
