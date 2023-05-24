package com.example.springsecurityapplication.services;

import com.example.springsecurityapplication.models.Order;
import com.example.springsecurityapplication.models.Product;
import com.example.springsecurityapplication.repositories.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // Возвращаем всех товаров - SELECT * FROM PRODUCT;
    public List<Order> getAllOrder(){
        return orderRepository.findAll();
    }

    public Order getOrderId(int id){
        Optional<Order> optionalOrder = orderRepository.findById(id);
        return optionalOrder.orElse(null);
    }

    @Transactional
    public void editOrder(int id, Order order){
        order.setId(id);
        orderRepository.save(order);
    }

    public List<Order>  findByEndNumberOrderByDataDesc(String number){
        return orderRepository.findByEndNumberOrderByDataDesc(number);
    }

    public List<Order> getAllOrderOrderByDateTimeDesc(){
        return orderRepository.findAllOrderOrderByDateTimeDesc();
    }

}
