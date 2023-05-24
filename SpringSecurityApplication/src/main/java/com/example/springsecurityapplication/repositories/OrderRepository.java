package com.example.springsecurityapplication.repositories;

import com.example.springsecurityapplication.models.Order;
import com.example.springsecurityapplication.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByPerson(Person person);

    // Поиск всех элементов и сортировка по дате (самая последняя дата сверху)
    @Query(value = "select * from orders order by date_time desc ", nativeQuery = true)
    List<Order> findAllOrderOrderByDateTimeDesc();

    // Поиск по номеру заказа (последние 4-е знака) и сортировка по дате (самая последняя дата сверху)
    @Query(value = "select * from orders where number like %?1 order by date_time desc ", nativeQuery = true)
    List<Order> findByEndNumberOrderByDataDesc(String str);
}
