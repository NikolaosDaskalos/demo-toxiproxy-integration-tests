package org.example.demo.toxiproxy.repository;

import org.example.demo.toxiproxy.model.OrderEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderEventRepository extends JpaRepository<OrderEvent, Long> {
}
