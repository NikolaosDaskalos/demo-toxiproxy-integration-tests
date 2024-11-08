package org.example.demo.toxiproxy.model;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@Table(name = "order_events")
public class OrderEvent {
    @Id
    @GeneratedValue
    Long id;

    Order order;

    @Column(name = "time_created")
    @CreationTimestamp
    LocalDateTime timeCreated;
}
