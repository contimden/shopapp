package com.project.shopapp.services;

import com.project.shopapp.dtos.OrderDetailDTO;
import com.project.shopapp.exceptions.DataNotFaundException;
import com.project.shopapp.models.Order;
import com.project.shopapp.models.OrderDetail;
import com.project.shopapp.models.Product;
import com.project.shopapp.repositories.OrderDetailRepository;
import com.project.shopapp.repositories.OrderRepository;
import com.project.shopapp.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailService implements IOrderDetailService{

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository productRepository;

    @Override
    public OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO) throws Exception {
        Order order =  orderRepository.findById(orderDetailDTO.getOrderId())
                .orElseThrow(() -> new DataNotFaundException(
                        "Cannot find Order with id: " + orderDetailDTO.getOrderId()
                ));
        Product product = productRepository.findById(orderDetailDTO.getProductId())
                .orElseThrow(() -> new DataNotFaundException(
                        "Cannot find Product with id: " + orderDetailDTO.getProductId()
                ));
        OrderDetail orderDetail = OrderDetail.builder()
                .order(order)
                .product(product)
                .numberOfProduct(orderDetailDTO.getNumberOfProducts())
                .price(orderDetailDTO.getPrice())
                .totalMoney(orderDetailDTO.getTotalMoney())
                .color(orderDetailDTO.getColor())
                .build();
        return orderDetailRepository.save(orderDetail);
    }

    @Override
    public OrderDetail getOrderDetail(Long id) throws Exception {
        return orderDetailRepository.findById(id)
                .orElseThrow(() ->new DataNotFaundException("Cannot find OrderDetail with id: " +id));
    }

    @Override
    public OrderDetail updateOrderDetail(Long id, OrderDetailDTO orderDetailDTO)
            throws DataNotFaundException {
        // TÌm xem order detail có tồn tại hay không
        OrderDetail existingOrderDetail = orderDetailRepository.findById(id)
                .orElseThrow(() -> new DataNotFaundException("Cannot find order detail with id: " + id));
        Order existingOrder = orderRepository.findById(orderDetailDTO.getOrderId())
                .orElseThrow(() -> new DataNotFaundException("Cannot find order detail with id: " + id));
        Product existingProduct = productRepository.findById(orderDetailDTO.getProductId())
                .orElseThrow(() -> new DataNotFaundException(
                        "Cannot find Product with id: " + orderDetailDTO.getProductId()
                ));
        existingOrderDetail.setPrice(orderDetailDTO.getPrice());
        existingOrderDetail.setNumberOfProduct(orderDetailDTO.getNumberOfProducts());
        existingOrderDetail.setTotalMoney(orderDetailDTO.getTotalMoney());
        existingOrderDetail.setColor(orderDetailDTO.getColor());
        existingOrderDetail.setOrder(existingOrder);
        existingOrderDetail.setProduct(existingProduct);
        return orderDetailRepository.save(existingOrderDetail);
    }

    @Override
    public void deleteById(Long id) {
        orderDetailRepository.deleteById(id);
    }

    @Override
    public List<OrderDetail> findByOrderId(Long orderId) {
        return orderDetailRepository.findByOrderId(orderId);
    }
}
