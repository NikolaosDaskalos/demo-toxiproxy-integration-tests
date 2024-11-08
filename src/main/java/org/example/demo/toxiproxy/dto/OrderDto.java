package org.example.demo.toxiproxy.dto;

public record OrderDto(
        String item,
        Integer quantity,
        String userInfo,
        String status
) {
}
