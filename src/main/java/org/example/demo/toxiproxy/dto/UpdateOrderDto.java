package org.example.demo.toxiproxy.dto;

public record UpdateOrderDto(
        String item,
        Integer quantity,
        String userInfo) {
}
