package org.demo.toxiproxy.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.demo.toxiproxy.dto.BasicResponseDto;
import org.demo.toxiproxy.dto.CreateOrderDto;
import org.demo.toxiproxy.dto.OrdersDto;
import org.demo.toxiproxy.dto.ResponseOrderDto;
import org.demo.toxiproxy.dto.UpdateOrderDto;
import org.demo.toxiproxy.service.OrderService;
import org.demo.toxiproxy.service.exception.BadRequestException;
import org.demo.toxiproxy.service.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<OrdersDto> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<CreateOrderDto> getOrder(@PathVariable Long orderId) throws NotFoundException {
        return ResponseEntity.ok(orderService.getOrder(orderId));
    }

    @PostMapping
    public ResponseEntity<ResponseOrderDto> createOrder(@RequestBody @Valid CreateOrderDto orderDto) throws BadRequestException {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(orderDto));
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<CreateOrderDto> updateOrder(
            @PathVariable Long orderId,
            @RequestBody @Valid UpdateOrderDto updateOrderDto) throws NotFoundException {
        return new ResponseEntity<>(orderService.updateOrder(orderId, updateOrderDto), HttpStatus.OK);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<BasicResponseDto> deleteOrder(@PathVariable Long orderId) throws BadRequestException, NotFoundException {
        return new ResponseEntity<>(orderService.deleteOrder(orderId), HttpStatus.OK);
    }
}
