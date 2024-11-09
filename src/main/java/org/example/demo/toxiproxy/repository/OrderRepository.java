package org.example.demo.toxiproxy.repository;

import java.util.List;
import org.example.demo.toxiproxy.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o WHERE o.publishedToKafka is NULL ORDER BY o.createdTime ASC")
    List<Order> findNotPublishedOrders();
}
