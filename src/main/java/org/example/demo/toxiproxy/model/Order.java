package org.example.demo.toxiproxy.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@Entity(name = "orders")
public class Order {
    @Id
    @GeneratedValue
    Long id;

    String item;

    Integer quantity;

    @Column(name = "user_info")
    String userInfo;

    String status;
}
