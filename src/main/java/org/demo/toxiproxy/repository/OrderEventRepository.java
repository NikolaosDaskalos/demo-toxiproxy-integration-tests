package org.demo.toxiproxy.repository;

import org.demo.toxiproxy.model.OrderEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderEventRepository extends JpaRepository<OrderEvent, UUID> {

}
