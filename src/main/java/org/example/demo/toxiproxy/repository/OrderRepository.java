package org.example.demo.toxiproxy.repository;

import org.example.demo.toxiproxy.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
